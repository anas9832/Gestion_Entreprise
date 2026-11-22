package ma.entreprise.mission;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.UUID;
import ma.entreprise.messages.MissionValidee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MissionPersistence {
    private final MissionRepository missions;
    private final OutboxRepository outbox;
    private final ObjectMapper json;

    public MissionPersistence(MissionRepository missions, OutboxRepository outbox, ObjectMapper json) {
        this.missions = missions;
        this.outbox = outbox;
        this.json = json;
    }

    @Transactional
    public MissionDto.Vue valider(Mission mission, MissionDto.Employe employe, MissionDto.Departement departement) {
        var vue = missions.saveAndFlush(mission).vue();
        var evenement = new MissionValidee(UUID.randomUUID(), vue.id(), employe.email(), employe.nom(),
                departement.nom(), vue.objet(), vue.dureeJours(), vue.creeLe());
        try {
            outbox.save(new OutboxMission(evenement.evenementId(), json.writeValueAsString(evenement), Instant.now()));
        } catch (JsonProcessingException erreur) {
            throw new IllegalStateException("Impossible de sérialiser la notification de mission.", erreur);
        }
        return vue;
    }
}
