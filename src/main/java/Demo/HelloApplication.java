package Demo;

import App.AssociationManagement.Association;
import App.AssociationManagement.Visit;
import App.AssociationMember.Member;
import others.*;
import Data.JsonManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        //affichage samy
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationMember/LoginScreen.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/App/AssociationMember/LoginScreen.css")).toExternalForm()
        );
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();



        //affichage yoan
        Stage secondStage = new Stage(); // Nouveau Stage
        FXMLLoader accueilLoader = new FXMLLoader(getClass().getResource("/App/GreenServiceSpace/accueil-view.fxml"));

        Scene scene1 = new Scene(accueilLoader.load(), 800, 600);
        scene1.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/App/GreenServiceSpace/styles.css")).toExternalForm()
        );

        secondStage.setTitle("Gestion des espaces verts");
        secondStage.setScene(scene1);
        secondStage.show();


        //affichage ny
        Stage accueilStage = new Stage();
        FXMLLoader pageAccueilLoader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/PageAccueil/PageAccueil.fxml"));
        Scene accueilScene = new Scene(pageAccueilLoader.load(), 600, 400);

        accueilStage.setTitle("Gestion de l'association");
        accueilStage.setScene(accueilScene);
        accueilStage.show();

    }
    public static void MakeJsonFiles() {
        
        JsonManager.deleteJsonFile("Members_JSON.json");
        JsonManager.deleteJsonFile("Visites_JSON.json");
        JsonManager.deleteJsonFile("Association_JSON.json");
        JsonManager.deleteJsonFile("Visits_JSON.json");


        JsonManager.createJsonFile("Members_JSON.json");
        JsonManager.createJsonFile("Visites_JSON.json");
        JsonManager.createJsonFile("Association_JSON.json");

        String filename = "Arbres_JSON.json";
        File file = new File(JsonManager.BASE_URL+"/"+filename);

        Personne p1= new Personne("Doe", "John", 25, LocalDate.of(1996, 5, 15),"balec");
        Personne p2= new Personne("Doe", "Jane", 22, LocalDate.of(1999, 8, 25),"balec");
        Personne p3= new Personne("Doe", "Alice", 30, LocalDate.of(1991, 2, 10),"balec");
        Personne p4= new Personne("Doe", "Bob", 28, LocalDate.of(1993, 12, 5),"balec");
        Personne p5= new Personne("Doe", "Eve", 35, LocalDate.of(1986, 10, 30),"balec");
        Member m1= new Member(p1, "johnDoe", "password123",LocalDate.of(2021, 5, 15));
        Member m2= new Member(p2, "JaneDoe", "password456",LocalDate.of(2021, 5, 15));
        Member m3= new Member(p3, "AliceDoe", "password789",LocalDate.of(2021, 5, 15));
        Member m4= new Member(p4, "BobDoe", "password101112",LocalDate.of(2021, 5, 15));
        Member m5= new Member(p5, "DoeEve", "password131415",LocalDate.of(2021, 5, 15));

        System.out.println(m1);
        JsonManager.insertInJson("Members_JSON.json", List.of(m1, m2, m3, m4, m5), "identifiant");

        Don d= new Don(100, "kaka");
        Facture f= new Facture(100, "kaka","pipi", true);


        Association a= new Association("Arre Assoc",500,List.of(d),List.of(f));

        JsonManager.insertInJson("Association_JSON.json", List.of(a), "nom");

        Optional<Tree> tree1=JsonManager.getObjectFromJson(filename, Map.entry("genre","Tetradium"), Tree.class);
        Optional<Tree> tree2=JsonManager.getObjectFromJson(filename,Map.entry("genre","Platanus"), Tree.class);
        Optional<Tree> tree3=JsonManager.getObjectFromJson(filename,Map.entry("genre","Styphnolobium"), Tree.class);


        Visit v1= new Visit(50,LocalDate.of(2025, 3, 16), tree1.get(),null, true);
        Visit v2= new Visit(150,LocalDate.of(2025, 5, 16), tree2.get(),m2, false);
        Visit v3= new Visit(100,LocalDate.of(2025, 5, 17), tree3.get(),m1, false);

        Don don = new Don(500, "Association locale");
        Subvention subvention = new Subvention(2000, "Ministère des Sports", "rapport_2024.pdf");

        JsonManager.insertInJson("Visites_JSON.json",List.of(v1, v2, v3), "date");

        Nomination n1= new Nomination(tree1.get(), 3);
        Nomination n2= new Nomination(tree2.get(), 2);

        JsonManager.createJsonFile("Nominations_JSON.json");
        JsonManager.insertInJson("Nominations_JSON.json", List.of(n1, n2), "idBase");


    }

    public static void notifTest(){
        List<Notification> notifications = new ArrayList<>();

        notifications.add(new Notification("🌳 Une nouvelle visite d'arbre est planifiée.",
                LocalDate.of(2025, 2, 10), LocalTime.of(14, 30)));

        notifications.add(new Notification("📅 Votre cotisation annuelle est due bientôt.",
                LocalDate.of(2025, 3, 5), LocalTime.of(9, 45)));

        notifications.add(new Notification("🔔 Un nouvel arbre remarquable a été ajouté !",
                LocalDate.of(2025, 4, 20), LocalTime.of(16, 15)));

        notifications.add(new Notification("✅ Votre proposition de classification a été validée.",
                LocalDate.of(2025, 5, 8), LocalTime.of(11, 0)));


        JsonManager.insertInJson("AssociationNotif.json",notifications);
    }


    public static void main(String[] args) throws IOException {

        launch();

    }
}