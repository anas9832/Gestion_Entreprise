package ma.entreprise.mission;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    Optional<Mission> findByCleIdempotence(String cle);
}
