package ma.entreprise.mission;


import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_missions")
public class OutboxMission {
    @Id
    private UUID id;

    @Column(nullable = false, updatable = false)
    private Instant creeLe;

    @Column(nullable = false)
    private Instant prochainEssai;

    private Instant publieLe;

    @Lob
    @Column(nullable = false, updatable = false)
    private String contenu;

    protected OutboxMission() {
    }

    OutboxMission(UUID id, String contenu, Instant maintenant) {
        this.id = id;
        this.contenu = contenu;
        this.creeLe = maintenant;
        this.prochainEssai = maintenant;
    }

    UUID id() {
        return id;
    }

    String contenu() {
        return contenu;
    }

    void marquerPublie() {
        publieLe = Instant.now();
    }

    void reporter() {
        prochainEssai = Instant.now().plusSeconds(5);
    }
}
