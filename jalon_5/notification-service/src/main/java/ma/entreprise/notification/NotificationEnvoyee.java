package ma.entreprise.notification;


import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications_envoyees")
public class NotificationEnvoyee {
    @Id
    private UUID id;

    @Column(nullable = false)
    private Instant envoyeeLe;

    protected NotificationEnvoyee() {
    }

    NotificationEnvoyee(UUID id) {
        this.id = id;
        this.envoyeeLe = Instant.now();
    }
}
