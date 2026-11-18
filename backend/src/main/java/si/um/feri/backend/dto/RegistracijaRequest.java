package si.um.feri.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistracijaRequest(
        @NotBlank(message = "Ime je obvezno.")
        String ime,

        @NotBlank(message = "E-poštni naslov je obvezen.")
        @Email(message = "E-poštni naslov ni veljavne oblike.")
        String email,

        @NotBlank(message = "Geslo je obvezno.")
        @Size(min = 8, message = "Geslo mora imeti vsaj 8 znakov.")
        String geslo
) {}
