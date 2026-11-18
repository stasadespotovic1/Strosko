package si.um.feri.backend.vao;

import jakarta.persistence.*;
import java.time.LocalDate;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Strosek {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String opis;
    private double znesek;
    private LocalDate datum;

    @ManyToOne
    @JoinColumn(name = "kategorija_id")
    private Kategorija kategorija;

    @ManyToOne(optional = false)
    @JoinColumn(name = "uporabnik_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Uporabnik uporabnik;

    public Strosek() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }
    public double getZnesek() { return znesek; }
    public void setZnesek(double znesek) { this.znesek = znesek; }
    public LocalDate getDatum() { return datum; }
    public void setDatum(LocalDate datum) { this.datum = datum; }
    public Kategorija getKategorija() { return kategorija; }
    public void setKategorija(Kategorija kategorija) { this.kategorija = kategorija; }
    public Uporabnik getUporabnik() { return uporabnik; }
    public void setUporabnik(Uporabnik uporabnik) { this.uporabnik = uporabnik; }
}