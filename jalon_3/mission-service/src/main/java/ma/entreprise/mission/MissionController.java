package ma.entreprise.mission;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
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
    ResponseEntity<MissionDto.Vue> creer(@Valid @RequestBody MissionDto.Demande body) {
        var resultat = service.creer(body);
        return ResponseEntity.created(URI.create("missions/" + resultat.id())).body(resultat);
    }

    @GetMapping
    List<MissionDto.Vue> lister() {
        return service.lister();
    }

    @GetMapping("/{id}")
    MissionDto.Vue lire(@PathVariable @Positive Long id) {
        return service.lire(id);
    }
}
