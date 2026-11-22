package ma.entreprise.mission;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "departement-service")
public interface DepartementsClient {
    @GetMapping("/departements/{id}")
    MissionDto.Departement lire(@PathVariable("id") Long id);
}
