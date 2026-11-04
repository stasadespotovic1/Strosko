package si.um.feri.backend.dao;

import org.springframework.data.repository.CrudRepository;
import si.um.feri.backend.vao.Strosek;

import java.util.List;

public interface StrosekRepository extends CrudRepository<Strosek, Integer> {

    List<Strosek> findByKategorijaId(int kategorijaId);
}
