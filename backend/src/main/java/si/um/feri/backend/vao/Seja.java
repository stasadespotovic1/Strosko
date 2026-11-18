package si.um.feri.backend.vao;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
public class Seja {

    @Id
    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(name = "uporabnik_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Uporabnik uporabnik;

    @Column(nullable = false)
    private Instant poteka;

    public Seja() {}

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Uporabnik getUporabnik() { return uporabnik; }
    public void setUporabnik(Uporabnik uporabnik) { this.uporabnik = uporabnik; }
    public Instant getPoteka() { return poteka; }
    public void setPoteka(Instant poteka) { this.poteka = poteka; }
}
