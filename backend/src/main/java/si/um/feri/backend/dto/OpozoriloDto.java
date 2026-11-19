package si.um.feri.backend.dto;

import si.um.feri.backend.vao.Opozorilo;
import si.um.feri.backend.vao.StopnjaOpozorila;

import java.time.Instant;

/** kategorijaId je null pri opozorilu za mesečni budget. */
public record OpozoriloDto(
        int id,
        Integer kategorijaId,
        String naziv,
        StopnjaOpozorila stopnja,
        double poraba,
        double budzet,
        double delez,
        int leto,
        int mesec,
        boolean prebrano,
        Instant ustvarjeno) {

    public static final String NAZIV_MESECNI_BUDZET = "Mesečni budget";

    public static OpozoriloDto iz(Opozorilo o) {
        boolean kategorija = o.getKategorija() != null;
        return new OpozoriloDto(
                o.getId(),
                kategorija ? o.getKategorija().getId() : null,
                kategorija ? o.getKategorija().getNaziv() : NAZIV_MESECNI_BUDZET,
                o.getStopnja(),
                o.getPoraba(),
                o.getBudzet(),
                o.getDelez(),
                o.getLeto(),
                o.getMesec(),
                o.isPrebrano(),
                o.getUstvarjeno());
    }
}
