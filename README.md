# TreeAppsProject

Projet universitaire en JavaFX composé de trois applications qui communiquent entre elles autour de la gestion des arbres d'une commune :

- l'**application pour un membre de l'association** ;
- l'**application du service des espaces verts** ;
- l'**application de gestion de l'association**.

## Lancement

Prérequis : JDK 23 ou plus récent, Maven.

```
mvn clean javafx:run
```

Les trois applications se lancent en même temps au démarrage de `HelloApplication`.

---

## Application pour un membre de l'association

### Connexion

Saisir son identifiant et son mot de passe, puis cliquer sur **Login**.

Exemple :

```
identifiant : johnDoe
mot de passe : password1234
```

### Accueil

La page d'accueil affiche les notifications et les visites à venir.

### Menu de navigation

Le bouton de navigation en haut à gauche donne accès aux pages suivantes :

- Profil
- Liste des arbres
- Planification et visites
- Cotisation
- Mes votes
- Home
- Logout

### Profil

Affiche les informations de l'utilisateur connecté.

### Liste des arbres

Affiche la liste des arbres présents sur la commune.

- Le bouton **More** affiche les paramètres de recherche et les options de filtrage.
- Par défaut, la recherche porte sur toutes les informations connues de l'arbre. Par exemple, taper « tetradium » dans la barre de recherche affiche tous les arbres du genre *Tetradium*.
- Un double-clic sur un arbre affiche ses détails.
- Le bouton **Voter** permet de voter pour un arbre.

### Planification et visites

Affiche les visites à venir et les visites passées. Pour planifier une nouvelle visite, double-cliquer sur la visite souhaitée.

### Cotisation

Affiche les dates des cotisations payées. Le bouton **Payer ma cotisation** permet de régler la cotisation.

### Mes votes

Affiche les votes effectués par l'utilisateur connecté.

---

## Application du service des espaces verts

1. **Lancement** : l'application se lance automatiquement avec les deux autres au démarrage de `HelloApplication`.
2. **Écran d'accueil** : trois boutons permettent de gérer les arbres, de consulter les notifications et de voir la liste des entités inscrites aux informations sur les arbres.
3. **Gestion des arbres** : permet d'accéder à la liste des arbres ou d'en enregistrer un nouveau.
   - **Liste des arbres** : affiche tous les arbres enregistrés, avec plusieurs filtres pour faciliter la recherche. Le bouton de la colonne **Info** affiche les informations détaillées d'un arbre et permet de **supprimer l'arbre** ou de le **classer comme arbre remarquable** (uniquement s'il ne l'est pas déjà). Après chaque opération, une notification est envoyée automatiquement aux autres applications via le fichier `GreenSpaceNotif.json`.
   - **Enregistrer un arbre** : il suffit de renseigner les informations de l'arbre puis de cliquer sur **Enregistrer** pour le sauvegarder, ou sur **Annuler** pour réinitialiser les champs. Une notification est là aussi envoyée automatiquement aux autres applications.
4. **Gestion des associations** : affiche la liste des associations et des membres inscrits aux informations sur les arbres de la municipalité.
5. **Notifications** : affiche les notifications envoyées par l'application de l'association. Elles concernent les résultats des votes sur les arbres à classer comme remarquables.

Chaque page dispose d'un bouton **Retour** pour revenir à la page précédente.

---

## Application de gestion de l'association

### Gestion des membres

Cette interface propose les fonctionnalités suivantes :

- **Retour** : revenir à l'écran précédent.
- **Élire un nouveau président** : désigner un(e) nouveau/nouvelle président(e) en double-cliquant sur le nom d'une personne.
- **Voir la liste des membres** : afficher la liste complète des membres.
- **Inscrire un membre** : ajouter un nouveau membre.
- **Désinscrire un membre** : retirer un membre de la liste.
- **Radier un membre** : exclure définitivement un membre pour non-respect des règles ou pour un autre motif justifié.

### Classification des arbres remarquables

Cette interface permet :

- d'afficher la liste des arbres remarquables ;
- d'afficher le classement provisoire des arbres ayant reçu le plus de votes pour être classés remarquables.
