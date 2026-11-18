package si.um.feri.backend.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;
import si.um.feri.backend.dao.SejaRepository;
import si.um.feri.backend.vao.Seja;

import java.time.Instant;
import java.util.Optional;


@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String ATRIBUT_UPORABNIK = "uporabnik";
    public static final String ATRIBUT_TOKEN = "token";
    private static final String BEARER = "Bearer ";

    private final SejaRepository sejaDao;

    public AuthInterceptor(SejaRepository sejaDao) {
        this.sejaDao = sejaDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // CORS preflight nima glave Authorization
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        String glava = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (glava == null || !glava.startsWith(BEARER)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Niste prijavljeni.");
        }
        String token = glava.substring(BEARER.length()).trim();

        Optional<Seja> seja = sejaDao.findById(token);
        if (seja.isEmpty() || seja.get().getPoteka().isBefore(Instant.now())) {
            seja.ifPresent(sejaDao::delete);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Seja je potekla. Prijavite se znova.");
        }
        if (seja.get().getUporabnik().isBlokiran()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Račun je blokiran.");
        }

        request.setAttribute(ATRIBUT_UPORABNIK, seja.get().getUporabnik());
        request.setAttribute(ATRIBUT_TOKEN, token);
        return true;
    }
}
