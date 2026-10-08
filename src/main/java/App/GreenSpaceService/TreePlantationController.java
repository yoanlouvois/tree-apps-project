package App.GreenSpaceService;

import Data.JsonManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TreePlantationController {

    @FXML
    private TextField idField;
    @FXML
    private TextField libelleField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField especeField;
    @FXML
    private TextField lieuField;
    @FXML
    private TextField remarquableField;
    @FXML
    private TextField latitudeField;
    @FXML
    private TextField longitudeField;
    @FXML
    private TextField hauteurField;
    @FXML
    private TextField circonferenceField;
    @FXML
    private TextField developpementStageField;

    // Bouton Annuler
    public void onCancelButtonClick(ActionEvent actionEvent) {
        resetFields();
    }
    @FXML
    public void onReturnButtonClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource
                    ("/App/GreenServiceSpace/three-manager-view.fxml"));
            Parent secondView = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene()
                    .getWindow();
            stage.setScene(new Scene(secondView, 800, 600));
            stage.getScene().getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource
                            ("/App/GreenServiceSpace/styles.css")).toExternalForm());
            stage.setTitle("Gestion des espaces verts");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSaveButtonClick(ActionEvent actionEvent) {
        // Vérifier si tous les champs sont remplis
        if (idField.getText().isEmpty() || libelleField.getText().isEmpty() || genreField.getText().isEmpty() ||
                especeField.getText().isEmpty() || lieuField.getText().isEmpty() || remarquableField.getText().isEmpty() ||
                latitudeField.getText().isEmpty() || longitudeField.getText().isEmpty() ||
                hauteurField.getText().isEmpty() || circonferenceField.getText().isEmpty() ||
                developpementStageField.getText().isEmpty()) {

            // Alerte si un champ est vide
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs avant d'enregistrer.", ButtonType.OK);
            alert.show();
            return;
        }

        try {
            // Convertir les valeurs saisies dans les types appropriés
            int idBase = Integer.parseInt(idField.getText());
            int circonference = Integer.parseInt(circonferenceField.getText().replaceAll("[^0-9]", ""));
            int hauteur = Integer.parseInt(hauteurField.getText().replaceAll("[^0-9]", ""));
            double latitude = Double.parseDouble(latitudeField.getText());
            double longitude = Double.parseDouble(longitudeField.getText());

            // Créer un nouvel objet représentant l'arbre
            Map<String, Object> newTree = new HashMap<>();
            newTree.put("idBase", idBase);
            newTree.put("libelle_france", libelleField.getText());
            newTree.put("genre", genreField.getText());
            newTree.put("espece", especeField.getText());
            newTree.put("lieu", lieuField.getText());
            newTree.put("remarquable", remarquableField.getText());
            newTree.put("latitude", latitude);
            newTree.put("longitude", longitude);
            newTree.put("circonference", circonference);
            newTree.put("hauteur", hauteur);
            newTree.put("stade_de_developpement", developpementStageField.getText());


            // Ajouter l'arbre au fichier JSON
            JsonManager.insertInJson("Arbres_JSON.json", List.of(newTree));

            logPlantationToNotifFile(newTree);
            // Confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Arbre enregistré avec succès !", ButtonType.OK);
            alert.show();

            // Réinitialiser les champs
            resetFields();

        } catch (NumberFormatException e) {
            // Gérer les erreurs de conversion
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez saisir des valeurs valides pour les champs numériques.", ButtonType.OK);
            alert.show();
        } catch (Exception e) {
            // Gérer d'autres erreurs
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Une erreur est survenue lors de l'enregistrement.", ButtonType.OK);
            alert.show();
        }
    }

    // Réinitialiser tous les champs de texte
    private void resetFields() {
        idField.clear();
        libelleField.clear();
        genreField.clear();
        especeField.clear();
        lieuField.clear();
        remarquableField.clear();
        latitudeField.clear();
        longitudeField.clear();
        hauteurField.clear();
        circonferenceField.clear();
        developpementStageField.clear();
    }

    // Méthode pour journaliser une plantation dans GreenSpaceNotif.json
    private void logPlantationToNotifFile(Map<String, Object> newTree) {
        try {
            // Préparer l'entrée pour le fichier de notifications
            Map<String, Object> notifEntry = new HashMap<>();
            notifEntry.put("TypeChangement", "Plantation");

            // Mettre les détails de l'arbre dans une clé "Arbre"
            Map<String, Object> arbreDetails = new HashMap<>(newTree);
            arbreDetails.remove("idBase"); // Supprimer "idBase" car il n'existe pas dans les autres entrées
            arbreDetails.put("id", newTree.get("idBase")); // Renommer "idBase" en "id"

            notifEntry.put("Arbre", arbreDetails);

            // Insérer dans le fichier de notifications
            JsonManager.insertInJson("GreenSpaceNotif.json", List.of(notifEntry));
            System.out.println("✅ Plantation journalisée dans GreenSpaceNotif.json");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors de la journalisation dans GreenSpaceNotif.json");
        }
    }

}

