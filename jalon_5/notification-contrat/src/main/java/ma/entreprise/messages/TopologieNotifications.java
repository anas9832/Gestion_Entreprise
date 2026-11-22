package ma.entreprise.messages;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class TopologieNotifications {
    public static final String EXCHANGE = "entreprise.missions";
    public static final String ROUTE = "mission.validee";
    public static final String FILE = "entreprise.notifications.mission";
    public static final String FILE_ERREURS = "entreprise.notifications.mission.dlq";

    @Bean
    Declarables topologieMissions() {
        String exchangeErreurs = EXCHANGE + ".dlx";
        var principal = new DirectExchange(EXCHANGE, true, false);
        var erreurs = new DirectExchange(exchangeErreurs, true, false);
        var file = QueueBuilder.durable(FILE).deadLetterExchange(exchangeErreurs)
                .deadLetterRoutingKey(ROUTE).build();
        var dlq = QueueBuilder.durable(FILE_ERREURS).build();
        return new Declarables(principal, erreurs, file, dlq,
                BindingBuilder.bind(file).to(principal).with(ROUTE),
                BindingBuilder.bind(dlq).to(erreurs).with(ROUTE));
    }
}
