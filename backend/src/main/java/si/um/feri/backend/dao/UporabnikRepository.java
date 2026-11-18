package si.um.feri.backend.dao;

import org.springframework.data.repository.CrudRepository;
import si.um.feri.backend.vao.Uporabnik;

import java.util.Optional;

public interface UporabnikRepository extends CrudRepository<Uporabnik, Integer> {

    Optional<Uporabnik> findByEmail(String email);

    boolean existsByEmail(String email);
}
