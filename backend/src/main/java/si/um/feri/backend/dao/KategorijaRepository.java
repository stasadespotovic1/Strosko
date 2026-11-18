package si.um.feri.backend.dao;

import org.springframework.data.repository.CrudRepository;
import si.um.feri.backend.vao.Kategorija;

import java.util.List;
import java.util.Optional;

public interface KategorijaRepository extends CrudRepository<Kategorija, Integer> {

    List<Kategorija> findByUporabnikId(int uporabnikId);

    Optional<Kategorija> findByIdAndUporabnikId(int id, int uporabnikId);
}
