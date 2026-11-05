package si.um.feri.backend.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.backend.dao.KategorijaRepository;
import si.um.feri.backend.dao.StrosekRepository;
import si.um.feri.backend.vao.Kategorija;
import si.um.feri.backend.vao.Strosek;

import java.util.Optional;
import java.util.logging.Logger;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/stroski")
public class StrosekController {

    private static final Logger log = Logger.getLogger(StrosekController.class.toString());

    private final StrosekRepository dao;
    private final KategorijaRepository kategorijaDao;

    public StrosekController(StrosekRepository dao, KategorijaRepository kategorijaDao) {
        this.dao = dao;
        this.kategorijaDao = kategorijaDao;
    }

    private boolean nastaviKategorijo(Strosek s) {
        if (s.getKategorija() == null) return true;
        Optional<Kategorija> k = kategorijaDao.findById(s.getKategorija().getId());
        k.ifPresent(s::setKategorija);
        return k.isPresent();
    }

    @PostMapping
    public ResponseEntity<Strosek> postStrosek(@RequestBody Strosek strosek) {
        log.info("Dodajanje stroška " + strosek.getOpis());
        strosek.setId(0); 
        if (!nastaviKategorijo(strosek)) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(dao.save(strosek));
    }

    @GetMapping
    public Iterable<Strosek> getVsiStroski() {
        return dao.findAll();
    }

    @GetMapping("/iskanje")
    public Iterable<Strosek> isciStroske(@RequestParam String q) {
        log.info("Iskanje stroškov: " + q);
        return dao.isci(q.trim());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Strosek> getStrosek(@PathVariable int id) {
        return dao.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Strosek> putStrosek(@PathVariable int id, @RequestBody Strosek novi) {
        if (!nastaviKategorijo(novi)) return ResponseEntity.badRequest().build();
        return dao.findById(id).map(s -> {
            s.setOpis(novi.getOpis());
            s.setZnesek(novi.getZnesek());
            s.setKategorija(novi.getKategorija());
            s.setDatum(novi.getDatum());
            return ResponseEntity.ok(dao.save(s));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStrosek(@PathVariable int id) {
        if (!dao.existsById(id)) return ResponseEntity.notFound().build();
        dao.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
