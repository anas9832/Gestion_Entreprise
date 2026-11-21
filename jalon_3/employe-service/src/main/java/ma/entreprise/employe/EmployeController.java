package ma.entreprise.employe;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employes")
public class EmployeController {
    private final EmployeService service;

    public EmployeController(EmployeService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<EmployeDto.Vue> creer(@Valid @RequestBody EmployeDto.Demande body) {
        var resultat = service.creer(body);
        return ResponseEntity.created(URI.create("employes/" + resultat.id())).body(resultat);
    }

    @GetMapping
    List<EmployeDto.Vue> lister() {
        return service.lister();
    }

    @GetMapping("/{id}")
    EmployeDto.Vue lire(@PathVariable @Positive Long id) {
        return service.lire(id);
    }

    @PutMapping("/{id}")
    EmployeDto.Vue modifier(@PathVariable @Positive Long id, @Valid @RequestBody EmployeDto.Demande body) {
        return service.modifier(id, body);
    }
}
