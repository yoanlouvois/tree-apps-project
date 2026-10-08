package App.AssociationManagement.Controller.financesAssociation;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import others.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefraiementVisitesController {

    @FXML
    private ListView<String> visitesListView;

    @FXML
    private VBox detailsContainer;

    @FXML
    private Button defraiementButton;

    private static final String FILE_NAME = "Visites_JSON.json";
    private JsonNode selectedVisit;
    private List<JsonNode> visitsList = new ArrayList<>();

    @FXML
    public void initialize() {
        loadVisitesData();
        setupListViewListener();
    }

    private void loadVisitesData() {
        visitesListView.getItems().clear();
        visitsList.clear();

        List<JsonNode> visits = JsonManager.getAllNodes(FILE_NAME);

        if (visits.isEmpty()) {
            Message.showAlert("Erreur", "Aucune visite trouvée dans le fichier.");
            return;
        }

        for (JsonNode visit : visits) {
            if (visit.has("member") && !visit.get("member").isNull()) {
                boolean visiteDefrayee = visit.has("visiteDefrayee") && visit.get("visiteDefrayee").asBoolean();
                String statut = visiteDefrayee ? "[Défrayée] " : "[Non défrayée] ";
                String item = statut + "ID: " + visit.get("id").asInt() +
                        " | Prix: " + visit.get("cout").asDouble() + "€" +
                        " | Membre: " + visit.get("member").get("nom").asText() +
                        " " + visit.get("member").get("prenom").asText();
                visitesListView.getItems().add(item);
                visitsList.add(visit);
            }
        }
    }

    private void setupListViewListener() {
        visitesListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int selectedIndex = visitesListView.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {
                    selectedVisit = visitsList.get(selectedIndex);
                    displayVisitDetails(selectedVisit);
                }
            }
        });
    }

    private void displayVisitDetails(JsonNode visit) {
        detailsContainer.getChildren().clear();

        detailsContainer.getChildren().add(new Label("Détails de la visite :"));
        detailsContainer.getChildren().add(new Label("ID: " + visit.get("id").asInt()));
        detailsContainer.getChildren().add(new Label("Prix: " + visit.get("cout").asDouble() + "€"));
        detailsContainer.getChildren().add(new Label("Lieu: " + visit.get("tree").get("lieu").asText()));
        detailsContainer.getChildren().add(new Label("Arbre: " + visit.get("tree").get("libelle_france").asText()));
        detailsContainer.getChildren().add(new Label("Date: " + visit.get("date").asText()));
        detailsContainer.getChildren().add(new Label("Défrayée: " + (visit.get("visiteDefrayee").asBoolean() ? "Oui" : "Non")));
    }

    @FXML
    protected void onButtonDefraiementClick(ActionEvent event) {
        if (selectedVisit == null) {
            Message.showAlert("Erreur", "Veuillez sélectionner une visite d'abord.");
            return;
        }

        boolean visiteDefrayee = selectedVisit.get("visiteDefrayee").asBoolean();
        if (visiteDefrayee) {
            Message.showInformation("Information", "Cette visite a déjà été défrayée.");
        } else {
            Alert confirmation = Message.showConfirmation("Confirmer le défraiement",
                    "Voulez-vous vraiment défrayer cette visite ?");
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    defrayerVisite(selectedVisit.get("id").asInt());
                }
            });
        }
    }

    private void defrayerVisite(int visitId) {
        JsonManager.updateJsonObject(FILE_NAME,
                Map.entry("id", visitId),
                Map.entry("visiteDefrayee", true));

        Message.showInformation("Succès", "La visite a été défrayée avec succès.");
        loadVisitesData(); // Recharger les données après mise à jour
    }

    @FXML
    protected void onButtonRetourClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/Depenses.fxml"));
            Parent financesView = loader.load();
            Stage stage = (Stage) defraiementButton.getScene().getWindow();
            stage.setScene(new Scene(financesView, 600, 400));
            stage.setTitle("Dépenses de l'association");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
