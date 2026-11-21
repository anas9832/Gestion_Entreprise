package ma.entreprise.departement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/departements")
public class DepartementController {
    private final DepartementService service;

    public DepartementController(DepartementService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<DepartementDto.Vue> creer(@Valid @RequestBody DepartementDto.Demande body) {
        var resultat = service.creer(body);
        return ResponseEntity.created(URI.create("departements/" + resultat.id())).body(resultat);
    }

    @GetMapping
    List<DepartementDto.Vue> lister() {
        return service.lister();
    }

    @GetMapping("/{id}")
    DepartementDto.Vue lire(@PathVariable @Positive Long id) {
        return service.lire(id);
    }

    @PutMapping("/{id}")
    DepartementDto.Vue modifier(@PathVariable @Positive Long id, @Valid @RequestBody DepartementDto.Demande body) {
        return service.modifier(id, body);
    }
}
