package ma.entreprise.mission;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import ma.entreprise.commun.Idempotence;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/missions")
public class MissionController {
    private final MissionService service;

    public MissionController(MissionService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<MissionDto.Vue> creer(@Valid @RequestBody MissionDto.Demande body,
            @RequestHeader("Idempotency-Key") @Pattern(regexp = Idempotence.FORMAT) String cle) {
        var resultat = service.creer(body, cle);
        return Idempotence.reponse(resultat, "missions/" + resultat.valeur().id());
    }

    @GetMapping
    List<MissionDto.Vue> lister() {
        return service.lister();
    }

    @GetMapping("/{id}")
    MissionDto.Vue lire(@PathVariable @Positive Long id) {
        return service.lire(id);
    }

    @GetMapping("/{id}/detail")
    MissionDto.Detail detail(@PathVariable @Positive Long id) {
        return service.detail(id);
    }
}
