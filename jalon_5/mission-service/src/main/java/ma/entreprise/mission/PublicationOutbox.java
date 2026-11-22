package ma.entreprise.mission;


import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import ma.entreprise.messages.TopologieNotifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PublicationOutbox {
    private static final Logger LOG = LoggerFactory.getLogger(PublicationOutbox.class);
    private final OutboxRepository outbox;
    private final RabbitTemplate rabbit;

    public PublicationOutbox(OutboxRepository outbox, RabbitTemplate rabbit) {
        this.outbox = outbox;
        this.rabbit = rabbit;
    }

    @Scheduled(fixedDelayString = "${entreprise.outbox.delai-ms:1000}")
    public void publierEvenementsValides() {
        // Cette lecture voit seulement les événements dont la transaction a été validée.
        for (var evenement : outbox.findFirst20ByPublieLeIsNullAndProchainEssaiLessThanEqualOrderByCreeLe(Instant.now())) {
            if (!publier(evenement)) {
                return;
            }
        }
    }

    private boolean publier(OutboxMission evenement) {
        String id = evenement.id().toString();
        var message = MessageBuilder.withBody(evenement.contenu().getBytes(StandardCharsets.UTF_8))
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding(StandardCharsets.UTF_8.name())
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .setMessageId(id).build();
        var correlation = new CorrelationData(id);
        try {
            rabbit.send(TopologieNotifications.EXCHANGE, TopologieNotifications.ROUTE, message, correlation);
            var confirmation = correlation.getFuture().get(3, TimeUnit.SECONDS);
            if (!confirmation.isAck() || correlation.getReturned() != null) {
                throw new IllegalStateException("Publication non confirmée ou sans file de destination.");
            }
        } catch (InterruptedException erreur) {
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception erreur) {
            evenement.reporter();
            outbox.save(evenement);
            LOG.warn("Publication reportée pour {} : {}", id, erreur.getMessage());
            return false;
        }
        evenement.marquerPublie();
        outbox.save(evenement);
        LOG.info("Notification de mission publiée : {}", id);
        return true;
    }
}
