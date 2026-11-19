package si.um.feri.backend.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.backend.dao.KategorijaRepository;
import si.um.feri.backend.dao.StrosekRepository;
import si.um.feri.backend.service.PreverjanjeBudgetaService;
import si.um.feri.backend.vao.Kategorija;
import si.um.feri.backend.vao.Strosek;
import si.um.feri.backend.vao.Uporabnik;

import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;
import java.util.logging.Logger;


@RestController
@RequestMapping("/stroski")
public class StrosekController {

    private static final Logger log = Logger.getLogger(StrosekController.class.toString());

    private final StrosekRepository dao;
    private final KategorijaRepository kategorijaDao;
    private final PreverjanjeBudgetaService preverjanje;

    public StrosekController(StrosekRepository dao, KategorijaRepository kategorijaDao,
                             PreverjanjeBudgetaService preverjanje) {
        this.dao = dao;
        this.kategorijaDao = kategorijaDao;
        this.preverjanje = preverjanje;
    }

    // dovoljena je samo kategorija, ki pripada istemu uporabniku
    private boolean nastaviKategorijo(Strosek s, Uporabnik u) {
        if (s.getKategorija() == null) return true;
        Optional<Kategorija> k = kategorijaDao.findByIdAndUporabnikId(s.getKategorija().getId(), u.getId());
        k.ifPresent(s::setKategorija);
        return k.isPresent();
    }

    // znesek mora biti pozitiven, datum obvezen (po njem se strošek uvršča v mesec)
    private void preveriVnos(Strosek s) {
        if (s.getZnesek() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Znesek mora biti večji od 0.");
        }
        if (s.getDatum() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datum je obvezen.");
        }
    }

    @PostMapping
    public ResponseEntity<Strosek> postStrosek(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u,
                                               @RequestBody Strosek strosek) {
        log.info("Dodajanje stroška " + strosek.getOpis());
        preveriVnos(strosek);
        strosek.setId(0);
        strosek.setUporabnik(u);
        if (!nastaviKategorijo(strosek, u)) return ResponseEntity.badRequest().build();
        Strosek shranjen = dao.save(strosek);
        preverjanje.preveri(u, shranjen.getDatum());
        return ResponseEntity.status(HttpStatus.CREATED).body(shranjen);
    }

    @GetMapping
    public Iterable<Strosek> getVsiStroski(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u) {
        return dao.findByUporabnikId(u.getId());
    }

    @GetMapping("/iskanje")
    public Iterable<Strosek> isciStroske(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u,
                                         @RequestParam String q) {
        log.info("Iskanje stroškov: " + q);
        return dao.isci(u.getId(), q.trim());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Strosek> getStrosek(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u,
                                              @PathVariable int id) {
        return dao.findByIdAndUporabnikId(id, u.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Strosek> putStrosek(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u,
                                              @PathVariable int id, @RequestBody Strosek novi) {
        preveriVnos(novi);
        if (!nastaviKategorijo(novi, u)) return ResponseEntity.badRequest().build();
        return dao.findByIdAndUporabnikId(id, u.getId()).map(s -> {
            LocalDate staraDatum = s.getDatum();
            s.setOpis(novi.getOpis());
            s.setZnesek(novi.getZnesek());
            s.setKategorija(novi.getKategorija());
            s.setDatum(novi.getDatum());
            Strosek shranjen = dao.save(s);
            preverjanje.preveri(u, shranjen.getDatum());
            // strošek je lahko prestavljen v drug mesec: preveriti je treba tudi prejšnjega
            if (staraDatum != null && !YearMonth.from(staraDatum).equals(YearMonth.from(shranjen.getDatum()))) {
                preverjanje.preveri(u, staraDatum);
            }
            return ResponseEntity.ok(shranjen);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStrosek(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u,
                                              @PathVariable int id) {
        Optional<Strosek> s = dao.findByIdAndUporabnikId(id, u.getId());
        if (s.isEmpty()) return ResponseEntity.notFound().build();
        LocalDate datum = s.get().getDatum();
        dao.delete(s.get());
        preverjanje.preveri(u, datum);
        return ResponseEntity.noContent().build();
    }
}
