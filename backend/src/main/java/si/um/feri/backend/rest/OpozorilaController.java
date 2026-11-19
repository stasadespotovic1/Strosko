package si.um.feri.backend.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.backend.dao.OpozoriloRepository;
import si.um.feri.backend.dto.OpozoriloDto;
import si.um.feri.backend.service.PreverjanjeBudgetaService;
import si.um.feri.backend.vao.Opozorilo;
import si.um.feri.backend.vao.Uporabnik;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/opozorila")
public class OpozorilaController {

    private final OpozoriloRepository dao;
    private final PreverjanjeBudgetaService preverjanje;

    public OpozorilaController(OpozoriloRepository dao, PreverjanjeBudgetaService preverjanje) {
        this.dao = dao;
        this.preverjanje = preverjanje;
    }

    /** Neprebrana opozorila (najnovejša prva); z ?vsa=true tudi prebrana. */
    @GetMapping
    public List<OpozoriloDto> getOpozorila(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u,
                                           @RequestParam(defaultValue = "false") boolean vsa) {
        List<Opozorilo> opozorila = vsa
                ? dao.findByUporabnikIdOrderByUstvarjenoDesc(u.getId())
                : dao.findByUporabnikIdAndPrebranoFalseOrderByUstvarjenoDesc(u.getId());
        return opozorila.stream().map(OpozoriloDto::iz).toList();
    }

    /** Ponovno preveri porabo v tekočem mesecu (npr. ob odprtju aplikacije). */
    @PostMapping("/preveri")
    public ResponseEntity<Void> preveri(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u) {
        preverjanje.preveri(u, LocalDate.now());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/prebrano")
    public ResponseEntity<Void> oznaciPrebrano(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u,
                                               @PathVariable int id) {
        return dao.findByIdAndUporabnikId(id, u.getId()).map(o -> {
            o.setPrebrano(true);
            dao.save(o);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/prebrano")
    public ResponseEntity<Void> oznaciVsePrebrano(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u) {
        dao.findByUporabnikIdAndPrebranoFalseOrderByUstvarjenoDesc(u.getId()).forEach(o -> {
            o.setPrebrano(true);
            dao.save(o);
        });
        return ResponseEntity.noContent().build();
    }
}
