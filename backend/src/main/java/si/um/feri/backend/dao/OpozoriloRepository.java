package si.um.feri.backend.dao;

import org.springframework.data.repository.CrudRepository;
import si.um.feri.backend.vao.Opozorilo;

import java.util.List;
import java.util.Optional;

public interface OpozoriloRepository extends CrudRepository<Opozorilo, Integer> {

    List<Opozorilo> findByUporabnikIdOrderByUstvarjenoDesc(int uporabnikId);

    List<Opozorilo> findByUporabnikIdAndPrebranoFalseOrderByUstvarjenoDesc(int uporabnikId);

    List<Opozorilo> findByUporabnikIdAndLetoAndMesec(int uporabnikId, int leto, int mesec);

    Optional<Opozorilo> findByIdAndUporabnikId(int id, int uporabnikId);
}
