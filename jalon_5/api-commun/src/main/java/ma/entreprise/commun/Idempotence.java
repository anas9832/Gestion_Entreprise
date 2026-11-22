package ma.entreprise.commun;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class Idempotence {
    public static final String FORMAT = "[A-Za-z0-9._-]{1,100}";
    private static final JsonMapper CANONIQUE = JsonMapper.builder()
            .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY).build();

    private Idempotence() {
    }

    public static String empreinte(Object demandeValidee) {
        try {
            byte[] json = CANONIQUE.writeValueAsBytes(demandeValidee);
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(json));
        } catch (JsonProcessingException | NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Impossible de calculer l’empreinte de la demande.", ex);
        }
    }

    public static void verifier(String empreinteInitiale, String nouvelleEmpreinte) {
        if (!nouvelleEmpreinte.equals(empreinteInitiale)) {
            throw ApiException.conflict("CLE_DEJA_UTILISEE", "Cette clé correspond à une autre demande.");
        }
    }

    public static <T> ResponseEntity<T> reponse(Resultat<T> resultat, String chemin) {
        return ResponseEntity.status(resultat.rejeu() ? HttpStatus.OK : HttpStatus.CREATED)
                .location(URI.create(chemin))
                .header("Idempotency-Replayed", Boolean.toString(resultat.rejeu()))
                .body(resultat.valeur());
    }

    public record Resultat<T>(T valeur, boolean rejeu) {
    }
}
