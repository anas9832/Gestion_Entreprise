# Démonstration · Jalon 5.0

Démarrez les applications selon le [README](../README.md) et lancez le dossier **01** de la collection.
Les statuts, les codes d’erreur et les identifiants sont vérifiés automatiquement par les scripts Postman.

| Cas | Résultat |
|---|---|
| Création valide | `201`, identifiant et `Location` |
| Champs ou références invalides selon le jalon | `400` et code d’erreur |
| Ressource inexistante | `404` |

Ouvrez [Eureka](http://localhost:8761), puis appelez `GET http://localhost:8080/employe-service/employes/1`.
Le même employé est accessible directement sur `http://localhost:8082/employes/1`.

Appelez `GET http://localhost:8888/mission-service/default/main`. Vérifiez la présence des propriétés
`server.port` et `info.projet` dans les deux sources et comparez le champ `version` à
`git -C config-repo rev-parse HEAD`.

## Enrichissement et rejeu

Une création de mission valide est suivie par un détail enrichi. Renvoyez exactement le même POST avec
la même clé : le statut passe à `200`, l’identifiant reste identique et `Idempotency-Replayed` vaut
`true`. Modifiez ensuite l’objet et conservez la clé pour obtenir `409 CLE_DEJA_UTILISEE`.
`GET /missions` permet de contrôler que cet identifiant apparaît une seule fois.

## Courriel et traitement différé

1. Dans le [dossier 02 de la collection](Gestion-Entreprise-J5.postman_collection.json), utilisez
   **Boîte Mailpit après envoi**. L’interface [Mailpit](http://localhost:8025) doit afficher
   « Mission n°… validée » à destination de `sara@example.test`. Identifiez le courriel par l’objet
   de mission ou l’identifiant retourné.
2. Arrêtez la JVM de `notification-service`. Exécutez **Créer avec le consommateur arrêté**.
   Le POST donne `201`. Dans [RabbitMQ → Queues](http://localhost:15672/#/queues), attendez un message
   **Ready** dans `entreprise.notifications.mission`. Le courriel de cette mission est encore absent.
3. Redémarrez `notification-service`. La file se vide et le courriel de la mission différée apparaît dans Mailpit.
4. Pour tester une panne SMTP avec le consommateur actif, exécutez `docker compose stop mailpit`, créez
   une nouvelle mission avec une clé neuve et laissez les trois tentatives se terminer. La mission retourne
   `201` et le message arrive dans `entreprise.notifications.mission.dlq`.
   Rétablissez SMTP avec `docker compose start mailpit`.
5. Pour tester une panne du broker, exécutez `docker compose stop rabbitmq`, créez une mission
   puis relancez le broker avec `docker compose start rabbitmq`. La mission reste créée et l’outbox
   publie l’événement une fois le broker disponible.
