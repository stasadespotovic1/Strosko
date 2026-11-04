package si.um.feri.backend.vao;

import jakarta.persistence.*;
import java.time.LocalDate;

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
}