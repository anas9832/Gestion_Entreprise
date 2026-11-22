package ma.entreprise.mission;


import feign.FeignException;
import ma.entreprise.commun.ApiException;
import org.springframework.http.HttpStatus;

final class ErreursDistantes {
    private ErreursDistantes() {
    }

    static boolean indisponible(FeignException erreur) {
        int statut = erreur.status();
        return statut == -1 || statut == 502 || statut == 503 || statut == 504;
    }

    static ApiException traduire(FeignException erreur, String service, Long id) {
        if (erreur.status() == 404) {
            return ApiException.notFound("Référence " + id + " introuvable dans " + service + ".");
        }
        if (indisponible(erreur)) {
            return ApiException.unavailable(service + " est momentanément indisponible.");
        }
        return new ApiException(HttpStatus.BAD_GATEWAY, "REPONSE_DISTANTE_INVALIDE",
                "Réponse inattendue de " + service + ".");
    }
}
