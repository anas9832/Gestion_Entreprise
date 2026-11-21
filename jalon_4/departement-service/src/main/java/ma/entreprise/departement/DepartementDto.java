package ma.entreprise.departement;

import jakarta.validation.constraints.*;

public final class DepartementDto {
    private DepartementDto() {
    }

    public record Demande(
            @NotBlank @Size(max = 100) String nom,
            @NotNull Boolean actif) {
    }

    public record Vue(Long id, String nom, Boolean actif) {
    }
}
