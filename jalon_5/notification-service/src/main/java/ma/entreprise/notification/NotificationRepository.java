package ma.entreprise.notification;


import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEnvoyee, UUID> {
}
