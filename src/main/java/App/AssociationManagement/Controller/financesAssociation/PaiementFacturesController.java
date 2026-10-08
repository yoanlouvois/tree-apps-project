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

public class PaiementFacturesController {

    @FXML
    private ListView<String> facturesListView;

    @FXML
    private VBox detailsContainer;

    @FXML
    private Button paiementFactureButton;

    private static final String FILE_NAME = "Factures_JSON.json";
    private JsonNode selectedFacture;
    private List<JsonNode> facturesList = new ArrayList<>();

    @FXML
    public void initialize() {
        loadFacturesData();
        setupListViewListener();
    }

    private void loadFacturesData() {
        facturesListView.getItems().clear();
        facturesList.clear();

        List<JsonNode> factures = JsonManager.getAllNodes(FILE_NAME);

        if (factures.isEmpty()) {
            Message.showAlert("Erreur", "Aucune facture trouvée dans le fichier.");
            return;
        }

        for (JsonNode facture : factures) {
            boolean estPayee = facture.has("estPayee") && facture.get("estPayee").asBoolean();
            String statut = estPayee ? "[Payée] " : "[Non payée] ";

            String item = statut + "Émetteur: " + facture.get("emetteur").asText() +
                    " | Montant: " + facture.get("montant").asDouble() + "€";

            facturesListView.getItems().add(item);
            facturesList.add(facture);
        }
    }

    private void setupListViewListener() {
        facturesListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int selectedIndex = facturesListView.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {
                    selectedFacture = facturesList.get(selectedIndex);
                    displayFactureDetails(selectedFacture);
                }
            }
        });
    }

    private void displayFactureDetails(JsonNode facture) {
        detailsContainer.getChildren().clear();

        detailsContainer.getChildren().add(new Label("Détails de la facture :"));
        detailsContainer.getChildren().add(new Label("ID: " + facture.get("id").asInt()));
        detailsContainer.getChildren().add(new Label("Montant: " + facture.get("montant").asDouble() + "€"));
        detailsContainer.getChildren().add(new Label("Émetteur: " + facture.get("emetteur").asText()));
        detailsContainer.getChildren().add(new Label("Bénéficiaire: " + facture.get("beneficiaire").asText()));
        detailsContainer.getChildren().add(new Label("Date d'émission: " + facture.get("dateEmission").asText()));
        detailsContainer.getChildren().add(new Label("Payée: " + (facture.get("estPayee").asBoolean() ? "Oui" : "Non")));
    }

    @FXML
    protected void onButtonPaiementFactureClick(ActionEvent event) {
        if (selectedFacture == null) {
            Message.showAlert("Erreur", "Veuillez sélectionner une facture d'abord.");
            return;
        }

        boolean facturePayee = selectedFacture.get("estPayee").asBoolean();
        if (facturePayee) {
            Message.showInformation("Information", "Cette facture a déjà été payée.");
        } else {
            Alert confirmation = Message.showConfirmation("Confirmer le paiement",
                    "Voulez-vous vraiment payer cette facture ?");
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    payerFacture(selectedFacture.get("id").asInt());
                }
            });
        }
    }

    private void payerFacture(int factureId) {
        JsonManager.updateJsonObject(FILE_NAME,
                Map.entry("id", factureId),
                Map.entry("estPayee", true));

        Message.showInformation("Succès", "La facture a été payée avec succès.");
        loadFacturesData(); // Recharger les données après mise à jour
    }

    @FXML
    protected void onButtonRetourClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/Depenses.fxml"));
            Parent financesView = loader.load();
            Stage stage = (Stage) paiementFactureButton.getScene().getWindow();
            stage.setScene(new Scene(financesView, 600, 400));
            stage.setTitle("Dépenses de l'association");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
