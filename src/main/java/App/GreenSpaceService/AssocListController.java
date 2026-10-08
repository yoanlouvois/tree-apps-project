package App.GreenSpaceService;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AssocListController {

    @FXML
    private ListView<String> listEntite;

    private final ObservableList<String> entiteList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Charger les données des fichiers JSON et remplir la ListView
        loadEntities();
        listEntite.setItems(entiteList);
    }

    @FXML
    public void OnButtonReturnClick2(ActionEvent event) {
        try {
            // Chemin fixe pour le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/GreenServiceSpace/accueil-view.fxml"));
            Parent secondView = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(secondView, 800, 600));
            stage.getScene().getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/App/GreenServiceSpace/styles.css")).toExternalForm()
            );
            stage.setTitle("Gestion des espaces verts");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Log de l'exception pour diagnostiquer les erreurs
        }
    }

    private void loadEntities() {
        // Charger les associations
        List<JsonNode> associations = JsonManager.getNodeWithoutFilter("Association_JSON.json");
        for (JsonNode association : associations) {
            String formattedAssoc = formatAssociation(association);
            entiteList.add(formattedAssoc);
        }
        // Charger les membres
        List<JsonNode> members = JsonManager.getNodeWithoutFilter("Members_JSON.json");
        for (JsonNode member : members) {
            String formattedMember = formatMember(member);
            entiteList.add(formattedMember);
        }
    }

    private String formatMember(JsonNode member) {
        // Construire une chaîne lisible pour un membre
        String nom = member.get("nom").asText("");
        String prenom = member.get("prenom").asText("");
        String age = member.get("age").asText("");
        String identifiant = member.get("identifiant").asText("");

        return String.format("Membre: %s %s (Age: %s, ID: %s)", prenom, nom, age, identifiant);
    }

    private String formatAssociation(JsonNode association) {
        // Construire une chaîne lisible pour une association
        String nom = association.get("nom").asText("");
        String budget = association.get("budget").asText("");

        return String.format("Association: %s (Budget: %s)", nom, budget);
    }




}
