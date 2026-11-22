package ma.entreprise.notification;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.internet.InternetAddress;
import java.io.IOException;
import ma.entreprise.messages.MissionValidee;
import ma.entreprise.messages.TopologieNotifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationConsommateur {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationConsommateur.class);
    private final NotificationRepository repository;
    private final ObjectMapper json;
    private final JavaMailSender smtp;
    private final String expediteur;

    public NotificationConsommateur(NotificationRepository repository, ObjectMapper json,
            JavaMailSender smtp, @Value("${entreprise.mail.expediteur}") String expediteur) {
        this.repository = repository;
        this.json = json;
        this.smtp = smtp;
        this.expediteur = expediteur;
    }

    @RabbitListener(queues = TopologieNotifications.FILE)
    @Transactional
    public void notifier(Message message) {
        var evenement = lire(message);
        if (repository.existsById(evenement.evenementId())) {
            return;
        }

        var courriel = new SimpleMailMessage();
        courriel.setFrom(expediteur);
        courriel.setTo(evenement.destinataire());
        courriel.setSubject("Mission n°" + evenement.missionId() + " validée");
        courriel.setText("""
                Bonjour %s,

                Votre mission « %s » a été validée.
                Département : %s
                Durée : %d jour(s).
                """.formatted(evenement.nomEmploye(), evenement.objet(), evenement.departement(), evenement.dureeJours()));
        smtp.send(courriel);
        repository.saveAndFlush(new NotificationEnvoyee(evenement.evenementId()));
        LOG.info("Courriel de mission envoyé : {}", evenement.evenementId());
    }

    private MissionValidee lire(Message message) {
        try {
            if (!MessageProperties.CONTENT_TYPE_JSON.equals(message.getMessageProperties().getContentType())) {
                throw new IllegalArgumentException("Content-Type application/json attendu.");
            }
            var evenement = json.readValue(message.getBody(), MissionValidee.class);
            if (evenement == null || evenement.evenementId() == null || evenement.missionId() < 1
                    || evenement.destinataire() == null || evenement.nomEmploye() == null
                    || evenement.departement() == null || evenement.objet() == null
                    || evenement.dureeJours() < 1 || evenement.dureeJours() > 30 || evenement.creeLe() == null) {
                throw new IllegalArgumentException("Événement de mission incomplet.");
            }
            new InternetAddress(evenement.destinataire(), true).validate();
            return evenement;
        } catch (IOException | jakarta.mail.internet.AddressException | IllegalArgumentException erreur) {
            throw new AmqpRejectAndDontRequeueException("Événement de mission invalide.", erreur);
        }
    }
}
