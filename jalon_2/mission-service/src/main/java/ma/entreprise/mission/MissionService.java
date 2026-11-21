package ma.entreprise.mission;

import java.util.List;
import ma.entreprise.commun.ApiException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MissionService {
    private final MissionRepository repository;

    public MissionService(MissionRepository repository) {
        this.repository = repository;
    }

    public MissionDto.Vue creer(MissionDto.Demande demande) {
        return repository.save(new Mission(demande)).vue();
    }

    public List<MissionDto.Vue> lister() {
        return repository.findAll(Sort.by("id")).stream().map(Mission::vue).toList();
    }

    public MissionDto.Vue lire(Long id) {
        return repository.findById(id).map(Mission::vue)
                .orElseThrow(() -> ApiException.notFound("Mission " + id + " introuvable."));
    }
}
