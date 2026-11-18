package si.um.feri.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record PrijavaRequest(
        @NotBlank(message = "E-poštni naslov je obvezen.")
        String email,

        @NotBlank(message = "Geslo je obvezno.")
        String geslo
) {}
