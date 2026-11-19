package si.um.feri.backend.vao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Uporabnik {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String ime;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String geslo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Vloga vloga = Vloga.UPORABNIK;

    private boolean blokiran = false;
    // splošni mesečni budget (null = ni nastavljen)
    private Double mesecniBudzet;

    public Uporabnik() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getGeslo() { return geslo; }
    public void setGeslo(String geslo) { this.geslo = geslo; }
    public Vloga getVloga() { return vloga; }
    public void setVloga(Vloga vloga) { this.vloga = vloga; }
    public Double getMesecniBudzet() { return mesecniBudzet; }
    public void setMesecniBudzet(Double mesecniBudzet) { this.mesecniBudzet = mesecniBudzet; }
    public boolean isBlokiran() { return blokiran; }
    public void setBlokiran(boolean blokiran) { this.blokiran = blokiran; }
}
