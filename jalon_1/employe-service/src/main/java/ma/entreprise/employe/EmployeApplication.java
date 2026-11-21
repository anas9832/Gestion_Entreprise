package ma.entreprise.employe;

import ma.entreprise.commun.ApiExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ApiExceptionHandler.class)
public class EmployeApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmployeApplication.class, args);
    }
}
