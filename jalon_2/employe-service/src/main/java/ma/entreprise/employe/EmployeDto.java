package ma.entreprise.employe;

import jakarta.validation.constraints.*;

public final class EmployeDto {
    private EmployeDto() {
    }

    public record Demande(
            @NotBlank @Size(max = 100) String nom,
            @NotBlank @Email @Size(max = 150) String email,
            @NotNull @Positive Long departementId,
            @NotNull Boolean actif) {
    }

    public record Vue(Long id, String nom, String email, Long departementId, Boolean actif) {
    }
}
