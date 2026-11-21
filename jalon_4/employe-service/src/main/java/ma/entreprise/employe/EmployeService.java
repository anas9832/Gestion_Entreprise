package ma.entreprise.employe;

import java.util.List;
import ma.entreprise.commun.ApiException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeService {
    private final EmployeRepository repository;

    public EmployeService(EmployeRepository repository) {
        this.repository = repository;
    }

    public EmployeDto.Vue creer(EmployeDto.Demande body) {
        return repository.save(new Employe(body.nom(), body.email(), body.departementId(), body.actif())).vue();
    }

    public List<EmployeDto.Vue> lister() {
        return repository.findAll(Sort.by("id")).stream().map(Employe::vue).toList();
    }

    public EmployeDto.Vue lire(Long id) {
        return trouver(id).vue();
    }

    @Transactional
    public EmployeDto.Vue modifier(Long id, EmployeDto.Demande body) {
        Employe element = trouver(id);
        element.modifier(body.nom(), body.email(), body.departementId(), body.actif());
        return element.vue();
    }

    private Employe trouver(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Employe " + id + " introuvable."));
    }
}
