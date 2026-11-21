package ma.entreprise.mission;

import ma.entreprise.commun.ApiExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ApiExceptionHandler.class)
@EnableFeignClients
public class MissionApplication {
    public static void main(String[] args) {
        SpringApplication.run(MissionApplication.class, args);
    }
}
