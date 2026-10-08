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

<table>
  <tr>
    <td align="center">
      <img width="224" height="300" alt="Gestion du compte" src="https://github.com/user-attachments/assets/c26d2d2a-6c24-4011-8c5e-11137b96e48a" /><br />
      <sub>Gestion du compte</sub>
    </td>
    <td align="center">
      <img width="190" height="300" alt="Notifications" src="https://github.com/user-attachments/assets/9d7e0060-75bf-49e4-84ed-334a5edba57a" /><br />
      <sub>Notifications</sub>
    </td>
  </tr>
</table>
 
Espace personnel d'un membre de l'association :
 
- **Accueil** : notifications et prochaines visites
- **Profil** : informations du membre connecté
- **Liste des arbres** : recherche, filtres et fiche détaillée de chaque arbre
- **Votes** : voter pour classer un arbre remarquable et consulter l'historique de ses votes
- **Visites** : historique et planification des visites
- **Cotisation** : historique des paiements et règlement de la cotisation

Compte de démonstration :
 
```
identifiant : johnDoe
mot de passe : password1234
```
 
---

## Application du service des espaces verts

Interface du service municipal chargé des arbres :
 
- **Gestion des arbres** : liste filtrable, fiche détaillée, enregistrement d'un nouvel arbre, suppression et classement en arbre remarquable
- **Synchronisation** : chaque modification envoie automatiquement une notification aux autres applications (via `GreenSpaceNotif.json`)
- **Associations** : liste des associations et des membres inscrits aux informations sur les arbres
- **Notifications** : résultats des votes de l'association sur les arbres à classer remarquables
<table>
  <tr>
    <td align="center">
      <img width="280" alt="Service des espaces verts, écran 1" src="https://github.com/user-attachments/assets/772b968e-595d-4e64-9c48-bb728302aac9" />
    </td>
    <td align="center">
      <img width="280" alt="Service des espaces verts, écran 2" src="https://github.com/user-attachments/assets/e8e3f637-92ea-4889-a7f9-080cd3b24f76" />
    </td>
    <td align="center">
      <img width="280" alt="Service des espaces verts, écran 3" src="https://github.com/user-attachments/assets/f7d32845-968b-4abc-972d-01687ebd19b4" />
    </td>
  </tr>
</table>

---

## Application de gestion de l'association
 
Interface de gestion interne de l'association :
 
- **Membres** : liste des membres, inscription, désinscription et radiation
- **Présidence** : élection d'un(e) nouveau/nouvelle président(e)
- **Arbres remarquables** : liste des arbres classés et classement provisoire des arbres les plus votés
<table>
  <tr>
    <td align="center">
      <img width="280" alt="Gestion de l'association, écran 1" src="https://github.com/user-attachments/assets/d4cc0c3c-0180-44cf-80b3-899e0eebbe0d" />
    </td>
    <td align="center">
      <img width="280" alt="Gestion de l'association, écran 2" src="https://github.com/user-attachments/assets/8a3fb285-b5c3-47bd-84b5-b0d6f572d63a" />
    </td>
    <td align="center">
      <img width="280" alt="Gestion de l'association, écran 3" src="https://github.com/user-attachments/assets/aaf23628-c123-484c-899e-e95e175ef14f" />
    </td>
  </tr>
</table>
