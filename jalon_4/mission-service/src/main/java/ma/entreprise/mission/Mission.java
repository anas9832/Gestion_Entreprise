package ma.entreprise.mission;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "missions")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long employeId;

    @Column(nullable = false)
    private Long departementId;

    @Column(nullable = false, length = 150)
    private String objet;

    @Column(nullable = false)
    private int dureeJours;

    @Column(nullable = false)
    private Instant creeLe;

    @Column(unique = true, length = 100)
    private String cleIdempotence;

    @Column(length = 64)
    private String empreinte;

    String empreinte() {
        return empreinte;
    }

    void memoriserRequete(String cle, String empreinte) {
        this.cleIdempotence = cle;
        this.empreinte = empreinte;
    }

    protected Mission() {
    }

    Mission(MissionDto.Demande demande) {
        this.employeId = demande.employeId();
        this.departementId = demande.departementId();
        this.objet = demande.objet();
        this.dureeJours = demande.dureeJours();
        this.creeLe = Instant.now().truncatedTo(java.time.temporal.ChronoUnit.MICROS);
    }

    MissionDto.Vue vue() {
        return new MissionDto.Vue(id, employeId, departementId, objet, dureeJours, creeLe, "VALIDEE");
    }
}
