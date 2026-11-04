package si.um.feri.backend.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class InfoController {
    @GetMapping("/info")
    public String pozdravi() {
        return "Zdravo";
    }
}
