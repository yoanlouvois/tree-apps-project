package App.AssociationManagement.Controller.gestionMembres;

import Data.JsonManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InscriptionMembreController {

    @FXML
    private TextField nomTextField, prenomTextField, adresseTextField, identifiantTextField, compAdresseTextField;
    @FXML
    private PasswordField passwordTextField, confirmationPasswordTextField;
    @FXML
    private DatePicker dateNaissanceDatePicker;
    @FXML
    private Button validerButton;

    private static final String JSON_FILE_NAME = "Members_JSON.json";

    @FXML
    protected void onButtonRetourClick(ActionEvent event) {
        retournerPagePrecedente(event);
    }

    @FXML
    protected void onButtonAnnulerClick(ActionEvent event) {
        effacerChamps();
        retournerPagePrecedente(event);
    }

    @FXML
    protected void onButtonValiderClick(ActionEvent event) {
        if (champsObligatoiresRemplis() && passwordsMatch()) {
            sauvegarderMembre();
            afficherPopup("Succès", "Le membre a bien été inscrit.");
            retournerPagePrecedente(event);
        } else {
            afficherChampsObligatoiresNonRemplis();
        }
    }

    private boolean champsObligatoiresRemplis() {
        boolean isValid = true;
        if (nomTextField.getText().isEmpty()) {
            nomTextField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            nomTextField.setStyle(null);
        }
        if (prenomTextField.getText().isEmpty()) {
            prenomTextField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            prenomTextField.setStyle(null);
        }
        if (adresseTextField.getText().isEmpty()) {
            adresseTextField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            adresseTextField.setStyle(null);
        }
        if (identifiantTextField.getText().isEmpty()) {
            identifiantTextField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            identifiantTextField.setStyle(null);
        }
        if (passwordTextField.getText().isEmpty()) {
            passwordTextField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            passwordTextField.setStyle(null);
        }
        if (confirmationPasswordTextField.getText().isEmpty()) {
            confirmationPasswordTextField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            confirmationPasswordTextField.setStyle(null);
        }
        if (dateNaissanceDatePicker.getValue() == null) {
            dateNaissanceDatePicker.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            dateNaissanceDatePicker.setStyle(null);
        }
        return isValid;
    }

    private boolean passwordsMatch() {
        if (!passwordTextField.getText().equals(confirmationPasswordTextField.getText())) {
            afficherPopup("Erreur", "Les mots de passe ne correspondent pas.");
            return false;
        }
        return true;
    }

    private void sauvegarderMembre() {
        LocalDate dateNaissance = dateNaissanceDatePicker.getValue();
        int age = Period.between(dateNaissance, LocalDate.now()).getYears();
        String dateInscription = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        Map<String, Object> membre = new HashMap<>();
        membre.put("nom", nomTextField.getText());
        membre.put("prenom", prenomTextField.getText());
        membre.put("age", age);
        membre.put("adresse", adresseTextField.getText());
        membre.put("dateNaissance", dateNaissance.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        membre.put("identifiant", identifiantTextField.getText());
        membre.put("password", passwordTextField.getText());
        membre.put("cotisationPayee", false);
        membre.put("cotisationsPayees", new ArrayList<>());
        membre.put("nominations", new ArrayList<>());
        membre.put("visites", new ArrayList<>());
        membre.put("dateInscription", dateInscription);

        JsonManager.insertInJson(JSON_FILE_NAME, Collections.singletonList(membre), "identifiant");
    }


    private void effacerChamps() {
        nomTextField.clear();
        prenomTextField.clear();
        adresseTextField.clear();
        identifiantTextField.clear();
        passwordTextField.clear();
        confirmationPasswordTextField.clear();
        compAdresseTextField.clear();
        dateNaissanceDatePicker.setValue(null);
    }

    private void afficherPopup(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherChampsObligatoiresNonRemplis() {
        afficherPopup("Erreur", "Veuillez remplir tous les champs obligatoires.");
    }

    private void retournerPagePrecedente(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/gestionMembres/GestionMembres.fxml"));
            Parent membreView = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(membreView, 600, 400));
            stage.setTitle("Gestion des membres de l'association");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
