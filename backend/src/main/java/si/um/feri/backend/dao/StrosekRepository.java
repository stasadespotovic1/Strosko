package si.um.feri.backend.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import si.um.feri.backend.vao.Strosek;

import java.util.List;

public interface StrosekRepository extends CrudRepository<Strosek, Integer> {

    List<Strosek> findByKategorijaId(int kategorijaId);

    @Query("SELECT s FROM Strosek s LEFT JOIN s.kategorija k " +
           "WHERE LOWER(s.opis) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR LOWER(k.naziv) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Strosek> isci(@Param("q") String q);
}
