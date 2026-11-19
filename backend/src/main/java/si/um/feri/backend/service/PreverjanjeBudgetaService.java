package si.um.feri.backend.service;

import org.springframework.stereotype.Service;
import si.um.feri.backend.dao.KategorijaRepository;
import si.um.feri.backend.dao.OpozoriloRepository;
import si.um.feri.backend.dao.StrosekRepository;
import si.um.feri.backend.vao.Kategorija;
import si.um.feri.backend.vao.Opozorilo;
import si.um.feri.backend.vao.StopnjaOpozorila;
import si.um.feri.backend.vao.Uporabnik;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PU6 Preverjanje budgeta: za izbrani mesec primerja porabo z budgeti kategorij in z mesečnim budgetom
 * ter uskladi shranjena opozorila (PU7):
 * <ul>
 *   <li>opozorilo se ustvari, ko poraba doseže prag (80 % ali 100 %), in sicer največ enkrat na stopnjo, mesec in cilj,</li>
 *   <li>opozorilo, ki ga poraba ne dosega več (npr. po povišanju budgeta ali brisanju stroška), se izbriše,</li>
 *   <li>ob preseganju se opozorilo o približevanju označi kot prebrano.</li>
 * </ul>
 */
@Service
public class PreverjanjeBudgetaService {

    static final double PRAG_PRIBLIZEVANJE = 0.8;
    static final double PRAG_PRESEGANJE = 1.0;

    private static final Logger log = Logger.getLogger(PreverjanjeBudgetaService.class.toString());

    private final StrosekRepository strosekDao;
    private final KategorijaRepository kategorijaDao;
    private final OpozoriloRepository opozoriloDao;

    public PreverjanjeBudgetaService(StrosekRepository strosekDao,
                                     KategorijaRepository kategorijaDao,
                                     OpozoriloRepository opozoriloDao) {
        this.strosekDao = strosekDao;
        this.kategorijaDao = kategorijaDao;
        this.opozoriloDao = opozoriloDao;
    }

    /**
     * Preveri mesec, v katerega spada datum. Napaka pri preverjanju ne sme razveljaviti shranjenega stroška ali
     * budgeta, zato se samo zabeleži; preverjanje se ponovi ob naslednjem odprtju aplikacije (PU6, izjema).
     */
    public void preveri(Uporabnik u, LocalDate datum) {
        if (datum == null) return;
        try {
            uskladi(u, datum.getYear(), datum.getMonthValue());
        } catch (RuntimeException e) {
            log.log(Level.WARNING, "Preverjanje budgeta ni uspelo", e);
        }
    }

    /** Vrne stopnjo opozorila ali null, če poraba ne dosega praga. Budget mora biti večji od 0. */
    public static StopnjaOpozorila dolociStopnjo(double poraba, double budzet) {
        // majhen epsilon, da napake pri decimalnem seštevanju (npr. 0,1 + 0,2) ne premaknejo meje
        double delez = poraba / budzet + 1e-9;
        if (delez >= PRAG_PRESEGANJE) return StopnjaOpozorila.PRESEGANJE;
        if (delez >= PRAG_PRIBLIZEVANJE) return StopnjaOpozorila.PRIBLIZEVANJE;
        return null;
    }

    private void uskladi(Uporabnik u, int leto, int mesec) {
        LocalDate od = LocalDate.of(leto, mesec, 1);
        LocalDate konec = od.plusMonths(1);

        Map<Integer, Double> poraba = new HashMap<>();
        for (Object[] vrstica : strosekDao.porabaPoKategorijah(u.getId(), od, konec)) {
            poraba.put((Integer) vrstica[0], ((Number) vrstica[1]).doubleValue());
        }
        List<Opozorilo> obstojeca = opozoriloDao.findByUporabnikIdAndLetoAndMesec(u.getId(), leto, mesec);

        for (Kategorija k : kategorijaDao.findByUporabnikId(u.getId())) {
            uskladiCilj(u, k, leto, mesec, poraba.getOrDefault(k.getId(), 0.0), k.getBudzet(), obstojeca);
        }
        Double skupaj = strosekDao.skupnaPoraba(u.getId(), od, konec);
        uskladiCilj(u, null, leto, mesec, skupaj == null ? 0.0 : skupaj, u.getMesecniBudzet(), obstojeca);
    }

    /** Uskladi opozorila za eno kategorijo ali (kategorija == null) za mesečni budget. */
    private void uskladiCilj(Uporabnik u, Kategorija kategorija, int leto, int mesec,
                             double poraba, Double budzet, List<Opozorilo> obstojeca) {
        StopnjaOpozorila stopnja = (budzet == null || budzet <= 0) ? null : dolociStopnjo(poraba, budzet);

        boolean obstajaTrenutna = false;
        for (Opozorilo o : obstojeca) {
            if (!jeIsti(o, kategorija)) continue;
            if (stopnja == null || o.getStopnja().ordinal() > stopnja.ordinal()) {
                opozoriloDao.delete(o); // poraba praga ne dosega več
                continue;
            }
            if (o.getStopnja() == stopnja) {
                obstajaTrenutna = true;
                osvezi(o, poraba, budzet);
            } else if (!o.isPrebrano()) {
                o.setPrebrano(true); // nadomesti ga opozorilo višje stopnje
                opozoriloDao.save(o);
            }
        }

        if (stopnja != null && !obstajaTrenutna) {
            Opozorilo o = new Opozorilo();
            o.setUporabnik(u);
            o.setKategorija(kategorija);
            o.setLeto(leto);
            o.setMesec(mesec);
            o.setStopnja(stopnja);
            osvezi(o, poraba, budzet);
            // ob preseganju je opozorilo o približevanju zastarelo
            if (stopnja == StopnjaOpozorila.PRESEGANJE) {
                obstojeca.stream()
                        .filter(x -> jeIsti(x, kategorija) && !x.isPrebrano())
                        .forEach(x -> {
                            x.setPrebrano(true);
                            opozoriloDao.save(x);
                        });
            }
        }
    }

    private static boolean jeIsti(Opozorilo o, Kategorija kategorija) {
        if (kategorija == null) return o.getKategorija() == null;
        return o.getKategorija() != null && o.getKategorija().getId() == kategorija.getId();
    }

    /** Zapiše trenutne vrednosti v opozorilo; shrani samo, če se je kaj spremenilo. */
    private void osvezi(Opozorilo o, double poraba, double budzet) {
        double p = zaokrozi(poraba);
        double b = zaokrozi(budzet);
        double d = zaokrozi(poraba / budzet * 100);
        boolean novo = o.getId() == 0;
        if (novo || o.getPoraba() != p || o.getBudzet() != b || o.getDelez() != d) {
            o.setPoraba(p);
            o.setBudzet(b);
            o.setDelez(d);
            opozoriloDao.save(o);
        }
    }

    private static double zaokrozi(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
