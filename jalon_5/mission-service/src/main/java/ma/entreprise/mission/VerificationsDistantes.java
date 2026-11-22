package ma.entreprise.mission;


import feign.FeignException;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

@Service
public class VerificationsDistantes {
    private final EmployesClient employes;
    private final DepartementsClient departements;

    public VerificationsDistantes(EmployesClient employes, DepartementsClient departements) {
        this.employes = employes;
        this.departements = departements;
    }

    public MissionDto.Employe employeStrict(Long id) {
        return lire(() -> employes.lire(id), "employe-service", id);
    }

    public MissionDto.Departement departementStrict(Long id) {
        return lire(() -> departements.lire(id), "departement-service", id);
    }

    private <T> T lire(Supplier<T> appel, String service, Long id) {
        try {
            return appel.get();
        } catch (FeignException erreur) {
            throw ErreursDistantes.traduire(erreur, service, id);
        }
    }
}
