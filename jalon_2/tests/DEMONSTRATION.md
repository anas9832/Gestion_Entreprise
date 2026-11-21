# Démonstration · Jalon 2.0

Démarrez les applications selon le [README](../README.md) et lancez le dossier **01** de la collection.
Les statuts, les codes d’erreur et les identifiants sont vérifiés automatiquement par les scripts Postman.

| Cas | Résultat |
|---|---|
| Création valide | `201`, identifiant et `Location` |
| Champs ou références invalides selon le jalon | `400` et code d’erreur |
| Ressource inexistante | `404` |

Ouvrez [Eureka](http://localhost:8761), puis appelez `GET http://localhost:8080/employe-service/employes/1`.
Le même employé est accessible directement sur `http://localhost:8082/employes/1`.
