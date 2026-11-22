package ma.entreprise.departement;

import ma.entreprise.commun.ApiExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ApiExceptionHandler.class)
public class DepartementApplication {
    public static void main(String[] args) {
        SpringApplication.run(DepartementApplication.class, args);
    }
}
