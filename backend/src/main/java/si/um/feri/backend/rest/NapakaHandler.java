package si.um.feri.backend.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class NapakaHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validacija(MethodArgumentNotValidException e) {
        String sporocilo = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(f -> f.getDefaultMessage())
                .orElse("Neveljavni podatki.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", sporocilo));
    }

    // 401/403/409 ... iz kontrolerjev in AuthInterceptorja; sporočilo vrnemo v istem formatu { message }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> status(ResponseStatusException e) {
        String sporocilo = e.getReason() != null ? e.getReason() : "Napaka na strežniku.";
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", sporocilo));
    }
}
