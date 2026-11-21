package ma.entreprise.departement;

import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DepartementDemo {
    @Bean
    ApplicationRunner initialiserDepartement(DepartementRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.saveAll(List.of(
                        new Departement("Informatique", true),
                        new Departement("Archives", false),
                        new Departement("Finance", true)
                ));
            }
        };
    }
}
