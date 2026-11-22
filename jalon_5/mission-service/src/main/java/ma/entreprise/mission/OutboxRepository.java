package ma.entreprise.mission;


import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<OutboxMission, UUID> {
    List<OutboxMission> findFirst20ByPublieLeIsNullAndProchainEssaiLessThanEqualOrderByCreeLe(Instant maintenant);
}
