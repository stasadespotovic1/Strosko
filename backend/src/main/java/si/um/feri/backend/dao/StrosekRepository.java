package si.um.feri.backend.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import si.um.feri.backend.vao.Strosek;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StrosekRepository extends CrudRepository<Strosek, Integer> {

    List<Strosek> findByKategorijaId(int kategorijaId);

    List<Strosek> findByUporabnikId(int uporabnikId);

    Optional<Strosek> findByIdAndUporabnikId(int id, int uporabnikId);

    List<Strosek> findByUporabnikIdAndDatumBetween(int uporabnikId, LocalDate od, LocalDate doDatuma);

    @Query("SELECT s FROM Strosek s LEFT JOIN s.kategorija k " +
            "WHERE s.uporabnik.id = :uid AND (" +
            "LOWER(s.opis) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(k.naziv) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Strosek> isci(@Param("uid") int uporabnikId, @Param("q") String q);

    // vrstica: [kategorijaId, vsota zneskov]; stroški brez kategorije se ne štejejo
    @Query("SELECT s.kategorija.id, SUM(s.znesek) FROM Strosek s " +
           "WHERE s.uporabnik.id = :uid AND s.kategorija IS NOT NULL " +
           "AND s.datum >= :od AND s.datum < :konec GROUP BY s.kategorija.id")
    List<Object[]> porabaPoKategorijah(@Param("uid") int uporabnikId,
                                       @Param("od") LocalDate od,
                                       @Param("konec") LocalDate doKonca);

    // skupna poraba uporabnika v obdobju (vključno s stroški brez kategorije)
    @Query("SELECT COALESCE(SUM(s.znesek), 0.0) FROM Strosek s " +
           "WHERE s.uporabnik.id = :uid AND s.datum >= :od AND s.datum < :konec")
    Double skupnaPoraba(@Param("uid") int uporabnikId,
                        @Param("od") LocalDate od,
                        @Param("konec") LocalDate doKonca);
}