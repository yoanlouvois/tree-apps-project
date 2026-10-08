package App.AssociationManagement.Controller.GestionDonation;

import Data.JsonManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;

public class AjouterDonateurController {

    @FXML
    private ComboBox<String> natureComboBox;

    @FXML
    private DatePicker dateDonPicker;

    @FXML
    private TextField nomDonateurTextField;

    private static final String JSON_FILE = "Donations.json";

    @FXML
    public void initialize() {
        natureComboBox.getItems().addAll("Services Municipaux", "Entreprise", "Association", "Individu");
    }

    @FXML
    protected void onButtonAjouterClick(ActionEvent event) {
        String nature = natureComboBox.getValue();
        LocalDate date = dateDonPicker.getValue();
        String nomDonateur = nomDonateurTextField.getText().trim(); // Supprime les espaces inutiles

        if (nature == null || date == null || nomDonateur.isEmpty()) {
            System.out.println("❌ Tous les champs doivent être remplis !");
            return;
        }

        try {
            // Génération d'un nouvel ID basé sur le dernier ID existant
            int newId = JsonManager.getLastId(JSON_FILE) + 1;

            // Création du donateur avec le nom
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode donateurNode = objectMapper.createObjectNode();
            donateurNode.put("id", newId);
            donateurNode.put("nature", nature);
            donateurNode.put("nom", nomDonateur);
            donateurNode.put("date_don", date.toString());

            // Insérer dans le fichier JSON
            JsonManager.insertInJson(JSON_FILE, Collections.singletonList(donateurNode));

            System.out.println("✅ Donateur ajouté avec succès : " + nomDonateur);

            // Retourner à la page d'accueil des dons
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/Donation/DonationAccueil.fxml"));
            Parent accueilView = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(accueilView, 600, 400));
            stage.setTitle("Gestion des Dons");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onButtonRetourClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/Donation/DonationAccueil.fxml"));
            Parent accueilView = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(accueilView, 600, 400));
            stage.setTitle("Gestion des Dons");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
