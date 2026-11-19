package si.um.feri.backend.vao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

/**
 * Shranjeno opozorilo o porabi budgeta za določen mesec.
 * Kategorija je null pri opozorilu za mesečni budget.
 */
@Entity
public class Opozorilo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "uporabnik_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Uporabnik uporabnik;

    @ManyToOne
    @JoinColumn(name = "kategorija_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Kategorija kategorija;

    private int leto;
    private int mesec;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StopnjaOpozorila stopnja;

    private double poraba;
    private double budzet;
    private double delez;

    private boolean prebrano = false;
    private Instant ustvarjeno = Instant.now();

    public Opozorilo() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Uporabnik getUporabnik() { return uporabnik; }
    public void setUporabnik(Uporabnik uporabnik) { this.uporabnik = uporabnik; }
    public Kategorija getKategorija() { return kategorija; }
    public void setKategorija(Kategorija kategorija) { this.kategorija = kategorija; }
    public int getLeto() { return leto; }
    public void setLeto(int leto) { this.leto = leto; }
    public int getMesec() { return mesec; }
    public void setMesec(int mesec) { this.mesec = mesec; }
    public StopnjaOpozorila getStopnja() { return stopnja; }
    public void setStopnja(StopnjaOpozorila stopnja) { this.stopnja = stopnja; }
    public double getPoraba() { return poraba; }
    public void setPoraba(double poraba) { this.poraba = poraba; }
    public double getBudzet() { return budzet; }
    public void setBudzet(double budzet) { this.budzet = budzet; }
    public double getDelez() { return delez; }
    public void setDelez(double delez) { this.delez = delez; }
    public boolean isPrebrano() { return prebrano; }
    public void setPrebrano(boolean prebrano) { this.prebrano = prebrano; }
    public Instant getUstvarjeno() { return ustvarjeno; }
    public void setUstvarjeno(Instant ustvarjeno) { this.ustvarjeno = ustvarjeno; }
}
