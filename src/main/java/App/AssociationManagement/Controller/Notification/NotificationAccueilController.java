package App.AssociationManagement.Controller.Notification;

import com.fasterxml.jackson.databind.JsonNode;
import Data.JsonManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.util.Collections;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NotificationAccueilController {

    @FXML
    private ListView<String> notificationListView; // ListView pour les notifications liées aux arbres

    @FXML
    private ListView<String> memberNotificationListView; // ListView pour les notifications liées aux membres

    private static final String NOTIFICATIONS_FILE = "GreenSpaceNotif.json"; // Notifications des arbres
    private static final String MEMBER_NOTIFICATIONS_FILE = "MemberAppNotif.json"; // Notifications des membres

    @FXML
    public void initialize() {
        loadTreeNotifications();
        loadMemberNotifications();
    }

    // Charger les notifications liées aux arbres
    private void loadTreeNotifications() {
        List<JsonNode> notifications = JsonManager.getNodeWithoutFilter(NOTIFICATIONS_FILE);

        if (notifications.isEmpty()) {
            System.out.println("❌ Aucune notification liée aux arbres trouvée !");
            return;
        }

        List<String> formattedNotifications = new ArrayList<>();

        for (JsonNode notification : notifications) {
            String typeChangement = notification.has("TypeChangement") ? notification.get("TypeChangement").asText() : "Type inconnu";
            JsonNode arbre = notification.get("Arbre");

            if (arbre != null) {
                String nom = arbre.has("nom") ? arbre.get("nom").asText() : "Nom inconnu";
                String lieu = arbre.has("lieu") ? arbre.get("lieu").asText() : "Lieu inconnu";
                formattedNotifications.add(typeChangement + " - " + nom + " (" + lieu + ")");
            } else {
                formattedNotifications.add(typeChangement + " - Aucune information sur l'arbre");
            }
        }

        Collections.reverse(formattedNotifications); // Afficher les notifications les plus récentes en premier
        notificationListView.getItems().clear();
        notificationListView.getItems().addAll(formattedNotifications);
    }

    // Charger les notifications liées aux membres
    private void loadMemberNotifications() {
        List<JsonNode> memberNotifications = JsonManager.getNodeWithoutFilter(MEMBER_NOTIFICATIONS_FILE);

        if (memberNotifications.isEmpty()) {
            System.out.println("❌ Aucune notification liée aux membres trouvée !");
            return;
        }

        List<String> formattedNotifications = new ArrayList<>();

        for (JsonNode notification : memberNotifications) {
            String message = notification.has("message") ? notification.get("message").asText() : "Message inconnu";
            String date = notification.has("date") ? notification.get("date").asText() : "Date inconnue";
            String time = notification.has("time") ? notification.get("time").asText() : "Heure inconnue";

            formattedNotifications.add(date + " " + time + " - " + message);
        }

        Collections.reverse(formattedNotifications); // Afficher les notifications les plus récentes en premier
        memberNotificationListView.getItems().clear();
        memberNotificationListView.getItems().addAll(formattedNotifications);
    }

    @FXML
    private Button retourButton; // Bouton lié à FXML avec fx:id="retourButton"

    @FXML
    protected void onRetourButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/PageAccueil/PageAccueil.fxml"));
            Parent accueilView = loader.load();

            Stage stage = (Stage) retourButton.getScene().getWindow();
            stage.setScene(new Scene(accueilView, 600, 400));
            stage.setTitle("Page d'Accueil");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}