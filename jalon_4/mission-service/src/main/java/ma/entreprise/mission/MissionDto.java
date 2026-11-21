package ma.entreprise.mission;

import jakarta.validation.constraints.*;
import java.time.Instant;

public final class MissionDto {
    private MissionDto() {
    }

    public record Demande(
            @NotNull @Positive Long employeId,
            @NotNull @Positive Long departementId,
            @NotBlank @Size(max = 150) String objet,
            @NotNull @Min(1) @Max(30) Integer dureeJours) {
    }

    public record Vue(Long id, Long employeId, Long departementId, String objet,
                      int dureeJours, Instant creeLe, String statut) {
    }

    public record Employe(Long id, String nom, String email, Long departementId, Boolean actif) {
    }

    public record Departement(Long id, String nom, Boolean actif) {
    }

    public record Detail(Vue mission, Employe employe, Departement departement, String mode) {
    }
}
