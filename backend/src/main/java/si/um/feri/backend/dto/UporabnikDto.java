package si.um.feri.backend.dto;

import si.um.feri.backend.vao.Uporabnik;
import si.um.feri.backend.vao.Vloga;

public record UporabnikDto(int id, String ime, String email, Vloga vloga) {

    public static UporabnikDto iz(Uporabnik u) {
        return new UporabnikDto(u.getId(), u.getIme(), u.getEmail(), u.getVloga());
    }
}
