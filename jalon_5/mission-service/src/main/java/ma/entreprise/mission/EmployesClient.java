package ma.entreprise.mission;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employe-service")
public interface EmployesClient {
    @GetMapping("/employes/{id}")
    MissionDto.Employe lire(@PathVariable("id") Long id);
}
