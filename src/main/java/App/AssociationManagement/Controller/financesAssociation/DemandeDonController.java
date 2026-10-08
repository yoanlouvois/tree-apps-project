package App.AssociationManagement.Controller.financesAssociation;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class DemandeDonController {

    @FXML
    private ListView<String> donateursListView;

    @FXML
    private TextField montantTextField;

    @FXML
    private Button demandeDonsButton;

    @FXML
    private Button annulerButton;

    private final ObservableList<String> donateursList = FXCollections.observableArrayList();
    private String selectedDonateur = null;

    private static final String JSON_FILE = "Donateurs_JSON.json";

    @FXML
    public void initialize() {
        loadDonateurs();
        demandeDonsButton.setDisable(true);

        // Gestion de la sélection de donateur
        donateursListView.setOnMouseClicked(this::onDonateurSelected);
        montantTextField.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
    }

    private void loadDonateurs() {
        List<JsonNode> donateurs = JsonManager.getNodeWithoutFilter(JSON_FILE);
        for (JsonNode donateur : donateurs) {
            String nom = donateur.get("nom").asText();
            String type = donateur.get("type").asText();
            donateursList.add(nom + " (" + type + ")");
        }
        donateursListView.setItems(donateursList);
    }

    private void onDonateurSelected(MouseEvent event) {
        selectedDonateur = donateursListView.getSelectionModel().getSelectedItem();
        if (selectedDonateur != null) {
            donateursListView.setStyle("-fx-background-color: lightgreen;"); // Indication visuelle
        }
        validateForm();
    }

    private void validateForm() {
        String montant = montantTextField.getText();
        boolean isNumeric = montant.matches("\\d+(\\.\\d{1,2})?");

        if (selectedDonateur != null && isNumeric) {
            demandeDonsButton.setDisable(false);
        } else {
            demandeDonsButton.setDisable(true);
        }
    }

    @FXML
    private void onButtonValiderClick(ActionEvent event) {
        String montant = montantTextField.getText();
        if (!montant.matches("\\d+(\\.\\d{1,2})?")) {
            showAlert("Erreur de saisie", "Veuillez entrer un montant valide (chiffres uniquement).", Alert.AlertType.ERROR);
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de la demande");
        confirmation.setHeaderText("Confirmez-vous la demande ?");
        confirmation.setContentText("Voulez-vous demander " + montant + " € à " + selectedDonateur + " ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            showAlert("Succès", "Votre demande de don a bien été envoyée.", Alert.AlertType.INFORMATION);
            montantTextField.clear();
            donateursListView.getSelectionModel().clearSelection();
            demandeDonsButton.setDisable(true);
        }
    }

    @FXML
    private void onButtonAnnulerClick(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Annulation");
        confirmation.setHeaderText("Êtes-vous sûr de vouloir annuler ?");
        confirmation.setContentText("Cette action vous ramènera à l'écran précédent.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            retourPagePrecedente(event);
        }
    }

    private void retourPagePrecedente(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/DonsSubventions.fxml"));
            Parent donsSubventionsView = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(donsSubventionsView));
            stage.setTitle("Dons et Subventions");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de revenir à la page précédente.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void onButtonRetourClick(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/ChoixDonsSubventions.fxml"));
            Parent choixDonsSubventionsView = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(choixDonsSubventionsView));
            stage.setTitle("Faire une demande");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
