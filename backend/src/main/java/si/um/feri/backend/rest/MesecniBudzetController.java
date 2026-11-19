package si.um.feri.backend.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import si.um.feri.backend.dao.UporabnikRepository;
import si.um.feri.backend.dto.MesecniBudzetDto;
import si.um.feri.backend.service.PreverjanjeBudgetaService;
import si.um.feri.backend.vao.Uporabnik;

import java.time.LocalDate;

@RestController
@RequestMapping("/mesecni-budzet")
public class MesecniBudzetController {

    private final UporabnikRepository dao;
    private final PreverjanjeBudgetaService preverjanje;

    public MesecniBudzetController(UporabnikRepository dao, PreverjanjeBudgetaService preverjanje) {
        this.dao = dao;
        this.preverjanje = preverjanje;
    }

    @GetMapping
    public MesecniBudzetDto get(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u) {
        return new MesecniBudzetDto(u.getMesecniBudzet());
    }

    /** Nastavi mesečni budget; budzet = null ga odstrani. */
    @PutMapping
    public MesecniBudzetDto put(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u,
                                @RequestBody MesecniBudzetDto vnos) {
        Double budzet = vnos.budzet();
        if (budzet != null && (budzet <= 0 || budzet.isNaN() || budzet.isInfinite())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mesečni budget mora biti večji od 0.");
        }
        u.setMesecniBudzet(budzet);
        dao.save(u);
        preverjanje.preveri(u, LocalDate.now());
        return new MesecniBudzetDto(budzet);
    }
}
