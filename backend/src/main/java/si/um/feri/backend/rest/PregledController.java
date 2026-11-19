package si.um.feri.backend.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import si.um.feri.backend.dao.KategorijaRepository;
import si.um.feri.backend.dao.StrosekRepository;
import si.um.feri.backend.dto.PostavkaPregleda;
import si.um.feri.backend.dto.PregledDto;
import si.um.feri.backend.vao.Kategorija;
import si.um.feri.backend.vao.Strosek;
import si.um.feri.backend.vao.Uporabnik;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/pregled")
public class PregledController {

    private final StrosekRepository strosekDao;
    private final KategorijaRepository kategorijaDao;

    public PregledController(StrosekRepository strosekDao, KategorijaRepository kategorijaDao) {
        this.strosekDao = strosekDao;
        this.kategorijaDao = kategorijaDao;
    }

    @GetMapping
    public PregledDto getPregled(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u,
                                 @RequestParam(required = false) String mesec) {
        YearMonth ym = (mesec == null || mesec.isBlank()) ? YearMonth.now() : preberiMesec(mesec);
        List<Strosek> tekoci = stroskiZaMesec(u, ym);
        List<Strosek> prejsnji = stroskiZaMesec(u, ym.minusMonths(1));
        double skupaj = vsota(tekoci);

        List<PostavkaPregleda> postavke = new ArrayList<>();
        for (Kategorija k : kategorijaDao.findByUporabnikId(u.getId())) {
            double p = vsota(tekoci.stream().filter(s -> s.getKategorija() != null && s.getKategorija().getId() == k.getId()).toList());
            double pp = vsota(prejsnji.stream().filter(s -> s.getKategorija() != null && s.getKategorija().getId() == k.getId()).toList());
            postavke.add(new PostavkaPregleda(k.getId(), k.getNaziv(), k.getBudzet(), p, pp, delez(p, skupaj),
                    k.getBudzet() != null && p > k.getBudzet()));
        }

        double brez = vsota(tekoci.stream().filter(s -> s.getKategorija() == null).toList());
        double brezPrejsnji = vsota(prejsnji.stream().filter(s -> s.getKategorija() == null).toList());
        if (brez > 0 || brezPrejsnji > 0) {
            postavke.add(new PostavkaPregleda(null, "Brez kategorije", null, brez, brezPrejsnji, delez(brez, skupaj), false));
        }

        postavke.sort(Comparator.comparingDouble(PostavkaPregleda::porabljeno).reversed());
        return new PregledDto(ym.toString(), skupaj, vsota(prejsnji), postavke);
    }

    private YearMonth preberiMesec(String mesec) {
        try {
            return YearMonth.parse(mesec.trim());
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mesec mora biti v obliki LLLL-MM, npr. 2026-11.");
        }
    }

    private List<Strosek> stroskiZaMesec(Uporabnik u, YearMonth ym) {
        return strosekDao.findByUporabnikIdAndDatumBetween(u.getId(), ym.atDay(1), ym.atEndOfMonth());
    }

    private double vsota(List<Strosek> stroski) {
        double vsota = stroski.stream().mapToDouble(Strosek::getZnesek).sum();
        return Math.round(vsota * 100) / 100.0;
    }

    private double delez(double del, double skupaj) {
        return skupaj > 0 ? Math.round(del / skupaj * 1000) / 10.0 : 0;
    }
}