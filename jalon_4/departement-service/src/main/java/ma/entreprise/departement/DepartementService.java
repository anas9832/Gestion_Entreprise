package ma.entreprise.departement;

import java.util.List;
import ma.entreprise.commun.ApiException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartementService {
    private final DepartementRepository repository;

    public DepartementService(DepartementRepository repository) {
        this.repository = repository;
    }

    public DepartementDto.Vue creer(DepartementDto.Demande body) {
        return repository.save(new Departement(body.nom(), body.actif())).vue();
    }

    public List<DepartementDto.Vue> lister() {
        return repository.findAll(Sort.by("id")).stream().map(Departement::vue).toList();
    }

    public DepartementDto.Vue lire(Long id) {
        return trouver(id).vue();
    }

    @Transactional
    public DepartementDto.Vue modifier(Long id, DepartementDto.Demande body) {
        Departement element = trouver(id);
        element.modifier(body.nom(), body.actif());
        return element.vue();
    }

    private Departement trouver(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Departement " + id + " introuvable."));
    }
}
