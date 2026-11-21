package ma.entreprise.employe;

import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EmployeDemo {
    @Bean
    ApplicationRunner initialiserEmploye(EmployeRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.saveAll(List.of(
                        new Employe("Sara Amrani", "sara@example.test", 1L, true),
                        new Employe("Youssef Naji", "youssef@example.test", 1L, false),
                        new Employe("Lina El Idrissi", "lina@example.test", 2L, true),
                        new Employe("Adam Karim", "adam@example.test", 3L, true)
                ));
            }
        };
    }
}
