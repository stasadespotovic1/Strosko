package si.um.feri.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import si.um.feri.backend.dao.KategorijaRepository;
import si.um.feri.backend.dao.StrosekRepository;
import si.um.feri.backend.vao.Kategorija;

import java.util.logging.Logger;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/kategorije")
public class KategorijaController {

    private static final Logger log = Logger.getLogger(KategorijaController.class.toString());

    @Autowired
    KategorijaRepository dao;

    @Autowired
    StrosekRepository strosekDao;

    @GetMapping
    public Iterable<Kategorija> getVse() {
        return dao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Kategorija> getKategorija(@PathVariable int id) {
        return dao.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private boolean veljavenBudzet(Kategorija k) {
        return k.getBudzet() == null || k.getBudzet() >= 0;
    }

    @PostMapping
    public ResponseEntity<Kategorija> postKategorija(@RequestBody Kategorija k) {
        log.info("Dodajanje kategorije " + k.getNaziv());
        if (!veljavenBudzet(k)) return ResponseEntity.badRequest().build();
        k.setId(0); 
        return ResponseEntity.ok(dao.save(k));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Kategorija> putKategorija(@PathVariable int id, @RequestBody Kategorija nova) {
        if (!veljavenBudzet(nova)) return ResponseEntity.badRequest().build();
        return dao.findById(id).map(k -> {
            k.setNaziv(nova.getNaziv());
            k.setBudzet(nova.getBudzet());
            return ResponseEntity.ok(dao.save(k));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKategorija(@PathVariable int id) {
        if (!dao.existsById(id)) return ResponseEntity.notFound().build();
        strosekDao.findByKategorijaId(id).forEach(s -> s.setKategorija(null));
        dao.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
