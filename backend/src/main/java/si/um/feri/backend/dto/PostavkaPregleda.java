package si.um.feri.backend.dto;

public record PostavkaPregleda(
        Integer kategorijaId,
        String naziv,
        Double budzet,
        double porabljeno,
        double porabljenoPrejsnji,
        double delez,
        boolean preseceno
) {}