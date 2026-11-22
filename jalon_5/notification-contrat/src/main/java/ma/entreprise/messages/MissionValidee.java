package ma.entreprise.messages;

import java.time.Instant;
import java.util.UUID;

public record MissionValidee(UUID evenementId, long missionId, String destinataire, String nomEmploye,
                            String departement, String objet, int dureeJours, Instant creeLe) {
}
