package si.um.feri.backend.dao;

import org.springframework.data.repository.CrudRepository;
import si.um.feri.backend.vao.Kategorija;

public interface KategorijaRepository extends CrudRepository<Kategorija, Integer> {
}