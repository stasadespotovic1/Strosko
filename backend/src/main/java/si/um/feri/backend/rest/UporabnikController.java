package si.um.feri.backend.rest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import si.um.feri.backend.dao.SejaRepository;
import si.um.feri.backend.dao.UporabnikRepository;
import si.um.feri.backend.dto.PrijavaRequest;
import si.um.feri.backend.dto.PrijavaResponse;
import si.um.feri.backend.dto.RegistracijaRequest;
import si.um.feri.backend.dto.UporabnikDto;
import si.um.feri.backend.vao.Seja;
import si.um.feri.backend.vao.Uporabnik;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Locale;
import java.util.logging.Logger;

@RestController
@RequestMapping("/auth")
public class UporabnikController {

    private static final Logger log = Logger.getLogger(UporabnikController.class.toString());
    private static final Duration TRAJANJE_SEJE = Duration.ofDays(7);
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UporabnikRepository dao;
    private final SejaRepository sejaDao;
    private final PasswordEncoder encoder;

    public UporabnikController(UporabnikRepository dao, SejaRepository sejaDao, PasswordEncoder encoder) {
        this.dao = dao;
        this.sejaDao = sejaDao;
        this.encoder = encoder;
    }

    private static String normaliziraj(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private PrijavaResponse ustvariSejo(Uporabnik u) {
        byte[] bajti = new byte[32];
        RANDOM.nextBytes(bajti);
        Seja s = new Seja();
        s.setToken(Base64.getUrlEncoder().withoutPadding().encodeToString(bajti));
        s.setUporabnik(u);
        s.setPoteka(Instant.now().plus(TRAJANJE_SEJE));
        sejaDao.save(s);
        return new PrijavaResponse(s.getToken(), UporabnikDto.iz(u));
    }

    @PostMapping("/registracija")
    public ResponseEntity<PrijavaResponse> registracija(@Valid @RequestBody RegistracijaRequest req) {
        String email = normaliziraj(req.email());
        if (dao.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Račun s tem e-poštnim naslovom že obstaja.");
        }
        Uporabnik u = new Uporabnik();
        u.setIme(req.ime().trim());
        u.setEmail(email);
        u.setGeslo(encoder.encode(req.geslo()));
        u = dao.save(u);
        log.info("Registriran nov uporabnik " + u.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ustvariSejo(u));
    }

    @PostMapping("/prijava")
    public PrijavaResponse prijava(@Valid @RequestBody PrijavaRequest req) {
        Uporabnik u = dao.findByEmail(normaliziraj(req.email()))
                .filter(x -> encoder.matches(req.geslo(), x.getGeslo()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Napačen e-poštni naslov ali geslo."));
        if (u.isBlokiran()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Račun je blokiran.");
        }
        return ustvariSejo(u);
    }

    @PostMapping("/odjava")
    public ResponseEntity<Void> odjava(@RequestAttribute(AuthInterceptor.ATRIBUT_TOKEN) String token) {
        sejaDao.deleteById(token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jaz")
    public UporabnikDto jaz(@RequestAttribute(AuthInterceptor.ATRIBUT_UPORABNIK) Uporabnik u) {
        return UporabnikDto.iz(u);
    }
}
