package ma.entreprise.mission;

import ma.entreprise.commun.ApiExceptionHandler;
import ma.entreprise.messages.TopologieNotifications;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@Import({ApiExceptionHandler.class, TopologieNotifications.class})
public class MissionApplication {
    public static void main(String[] args) {
        SpringApplication.run(MissionApplication.class, args);
    }
}
