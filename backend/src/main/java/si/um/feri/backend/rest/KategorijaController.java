package si.um.feri.backend.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import si.um.feri.backend.dao.KategorijaRepository;
import si.um.feri.backend.dao.StrosekRepository;
import si.um.feri.backend.service.PreverjanjeBudgetaService;
import si.um.feri.backend.vao.Kategorija;
import si.um.feri.backend.vao.Uporabnik;

import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/kategorije")
public class KategorijaController {

    private static final Logger log = Logger.getLogger(KategorijaController.class.toString());

    private final KategorijaRepository dao;
    private final StrosekRepository strosekDao;
    private final PreverjanjeBudgetaService preverjanje;

    public KategorijaController(KategorijaRepository dao, StrosekRepository strosekDao,
                                PreverjanjeBudgetaService preverjanje) {
        this.dao = dao;
        this.strosekDao = strosekDao;
        this.preverjanje = preverjanje;
    }

    @GetMapping
    public Iterable<Kategorija> getVse(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u) {
        return dao.findByUporabnikId(u.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Kategorija> getKategorija(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u,
                                                    @PathVariable int id) {
        return dao.findByIdAndUporabnikId(id, u.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private boolean veljavenBudzet(Kategorija k) {
        return k.getBudzet() == null || k.getBudzet() >= 0;
    }

    @PostMapping
    public ResponseEntity<Kategorija> postKategorija(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u,
                                                     @RequestBody Kategorija k) {
        log.info("Dodajanje kategorije " + k.getNaziv());
        if (!veljavenBudzet(k)) return ResponseEntity.badRequest().build();
        k.setId(0);
        k.setUporabnik(u);
        Kategorija shranjena = dao.save(k);
        preverjanje.preveri(u, LocalDate.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(shranjena);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Kategorija> putKategorija(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u,
                                                    @PathVariable int id, @RequestBody Kategorija nova) {
        if (!veljavenBudzet(nova)) return ResponseEntity.badRequest().build();
        return dao.findByIdAndUporabnikId(id, u.getId()).map(k -> {
            k.setNaziv(nova.getNaziv());
            k.setBudzet(nova.getBudzet());
            Kategorija shranjena = dao.save(k);
            // sprememba budgeta: preveri porabo v tekočem mesecu (PU2 include PU6)
            preverjanje.preveri(u, LocalDate.now());
            return ResponseEntity.ok(shranjena);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKategorija(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u,
                                                 @PathVariable int id) {
        Optional<Kategorija> k = dao.findByIdAndUporabnikId(id, u.getId());
        if (k.isEmpty()) return ResponseEntity.notFound().build();
        strosekDao.findByKategorijaId(id).forEach(s -> s.setKategorija(null));
        dao.delete(k.get());
        return ResponseEntity.noContent().build();
    }
}
