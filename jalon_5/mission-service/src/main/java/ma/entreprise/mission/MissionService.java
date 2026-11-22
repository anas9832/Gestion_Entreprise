package ma.entreprise.mission;

import java.util.List;
import ma.entreprise.commun.ApiException;
import ma.entreprise.commun.Idempotence;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MissionService {
    private final MissionRepository repository;
    private final VerificationsDistantes distant;
    private final MissionPersistence persistence;

    public MissionService(MissionRepository repository, VerificationsDistantes distant, MissionPersistence persistence) {
        this.repository = repository;
        this.distant = distant;
        this.persistence = persistence;
    }

    public Idempotence.Resultat<MissionDto.Vue> creer(MissionDto.Demande demande, String cle) {
        String empreinte = Idempotence.empreinte(demande);
        var existante = repository.findByCleIdempotence(cle);
        if (existante.isPresent()) {
            return rejouer(existante.get(), empreinte);
        }

        var employe = distant.employeStrict(demande.employeId());
        var departement = distant.departementStrict(demande.departementId());
        if (!employe.actif()) {
            throw ApiException.invalid("EMPLOYE_INACTIF", "L’employé doit être actif.");
        }
        if (!departement.actif()) {
            throw ApiException.invalid("DEPARTEMENT_INACTIF", "Le département doit être actif.");
        }
        if (!departement.id().equals(employe.departementId())) {
            throw ApiException.invalid("AFFECTATION_INCOMPATIBLE", "L’employé doit appartenir au département.");
        }

        var mission = new Mission(demande);
        mission.memoriserRequete(cle, empreinte);
        try {
            return new Idempotence.Resultat<>(persistence.valider(mission, employe, departement), false);
        } catch (DataIntegrityViolationException erreur) {
            // La transaction d’écriture est terminée. Une requête concurrente peut avoir gagné.
            return repository.findByCleIdempotence(cle)
                    .map(gagnante -> rejouer(gagnante, empreinte))
                    .orElseThrow(() -> erreur);
        }
    }

    private Idempotence.Resultat<MissionDto.Vue> rejouer(Mission mission, String empreinte) {
        Idempotence.verifier(mission.empreinte(), empreinte);
        return new Idempotence.Resultat<>(mission.vue(), true);
    }

    public List<MissionDto.Vue> lister() {
        return repository.findAll(Sort.by("id")).stream().map(Mission::vue).toList();
    }

    public MissionDto.Vue lire(Long id) {
        return repository.findById(id).map(Mission::vue)
                .orElseThrow(() -> ApiException.notFound("Mission " + id + " introuvable."));
    }

    public MissionDto.Detail detail(Long id) {
        var vue = lire(id);
        return new MissionDto.Detail(vue, distant.employeStrict(vue.employeId()),
                distant.departementStrict(vue.departementId()), "ENRICHI");
    }
}
