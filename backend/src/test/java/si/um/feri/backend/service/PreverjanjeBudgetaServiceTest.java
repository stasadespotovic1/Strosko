package si.um.feri.backend.service;

import org.junit.jupiter.api.Test;
import si.um.feri.backend.vao.StopnjaOpozorila;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PreverjanjeBudgetaServiceTest {

    @Test
    void podPragomNiOpozorila() {
        assertNull(PreverjanjeBudgetaService.dolociStopnjo(60, 100));
        assertNull(PreverjanjeBudgetaService.dolociStopnjo(79.99, 100));
        assertNull(PreverjanjeBudgetaService.dolociStopnjo(0, 100));
    }

    @Test
    void ob80OdstotkihJePriblizevanje() {
        assertEquals(StopnjaOpozorila.PRIBLIZEVANJE, PreverjanjeBudgetaService.dolociStopnjo(80, 100));
        assertEquals(StopnjaOpozorila.PRIBLIZEVANJE, PreverjanjeBudgetaService.dolociStopnjo(99.99, 100));
    }

    @Test
    void obDosezenemBudgetuJePreseganje() {
        assertEquals(StopnjaOpozorila.PRESEGANJE, PreverjanjeBudgetaService.dolociStopnjo(100, 100));
        assertEquals(StopnjaOpozorila.PRESEGANJE, PreverjanjeBudgetaService.dolociStopnjo(105, 100));
    }

    @Test
    void decimalnoSestevanjeNePremakneMeje() {
        // 0,1 + 0,2 + 0,3 + 0,4 ni natanko 1,0 – meja 100 % mora kljub temu držati
        double poraba = 0.1 + 0.2 + 0.3 + 0.4;
        assertEquals(StopnjaOpozorila.PRESEGANJE, PreverjanjeBudgetaService.dolociStopnjo(poraba, 1.0));
    }

    @Test
    void stopnjiSePrimerjataPoVrstnemRedu() {
        // logika usklajevanja opozoril temelji na tem, da je PRESEGANJE višja stopnja od PRIBLIZEVANJE
        assertEquals(true, StopnjaOpozorila.PRESEGANJE.ordinal() > StopnjaOpozorila.PRIBLIZEVANJE.ordinal());
    }
}
