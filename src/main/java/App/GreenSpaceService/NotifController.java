package App.GreenSpaceService;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NotifController {

    @FXML
    private ListView<String> listNotif;

    private static final String NOTIF_FILENAME = "ArbreRemarquableChoisi.json";

    @FXML
    public void initialize() {
        loadNotifications();
    }

    private void loadNotifications() {
        // Lire les données depuis le fichier JSON
        List<JsonNode> notifications = JsonManager.getNodeWithoutFilter(NOTIF_FILENAME);

        // Inverser l'ordre des notifications pour afficher les plus récentes en premier
        Collections.reverse(notifications);

        for (JsonNode notif : notifications) {
            // Récupérer le type de changement
            String typeChangement = notif.has("TypeChangement") ? notif.get("TypeChangement").asText() : "Type inconnu";

            // Récupérer les informations de l'arbre
            JsonNode arbre = notif.get("Arbre");
            if (arbre != null) {
                String idBase = arbre.has("idBase") ? arbre.get("idBase").asText() : "ID inconnu";
                String genre = arbre.has("genre") ? arbre.get("genre").asText() : "Genre inconnu";
                String espece = arbre.has("espece") ? arbre.get("espece").asText() : "Espèce inconnue";
                String lieu = arbre.has("lieu") ? arbre.get("lieu").asText() : "Lieu inconnu";

                // Construire une chaîne descriptive avec l'ID
                String notifText = String.format("%s - ID: %s - %s %s (%s)", typeChangement, idBase, genre, espece, lieu);

                // Ajouter la notification dans la ListView
                listNotif.getItems().add(notifText);
            }
        }
    }

    public void OnButtonReturnClick3(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/GreenServiceSpace/accueil-view.fxml"));
            Parent secondView = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(secondView, 800, 600));
            stage.getScene().getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/App/GreenServiceSpace/styles.css")).toExternalForm()
            );
            stage.setTitle("Gestion des espaces verts");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}