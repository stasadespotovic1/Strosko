package si.um.feri.backend.vao;

import jakarta.persistence.*;

@Entity
public class Kategorija {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String naziv;
    private Double budzet;

    public Kategorija() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
    public Double getBudzet() { return budzet; }
    public void setBudzet(Double budzet) { this.budzet = budzet; }
} 