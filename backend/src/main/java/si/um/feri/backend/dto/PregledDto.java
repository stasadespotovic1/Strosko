package si.um.feri.backend.dto;

import java.util.List;

public record PregledDto(String mesec, double skupaj, double skupajPrejsnji, List<PostavkaPregleda> postavke) {}