package ma.entreprise.config;

import java.nio.file.Path;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigApplication {
    public static void main(String[] args) {
        String depotLocal = Path.of("config-repo").toAbsolutePath().normalize().toUri().toString();
        String depot = System.getenv().getOrDefault("CONFIG_REPO_URI", depotLocal);
        var application = new SpringApplication(ConfigApplication.class);
        application.setDefaultProperties(Map.of("spring.cloud.config.server.git.uri", depot));
        application.run(args);
    }
}
