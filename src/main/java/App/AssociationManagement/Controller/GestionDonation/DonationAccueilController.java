package App.AssociationManagement.Controller.GestionDonation;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class DonationAccueilController {
    @FXML
    private TextField idSuppressionTextField;

    @FXML
    private TextField rechercheTextField;

    @FXML
    private ListView<String> donateursListView;

    private ObservableList<String> donateursList;

    @FXML
    protected void onButtonRetourClick(ActionEvent event) {
        try {
            // Charger la page d'accueil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/PageAccueil/PageAccueil.fxml"));
            Parent accueilView = loader.load();

            // Obtenir la scène actuelle
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(new Scene(accueilView, 600, 400));
            stage.setTitle("Menu Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshDonateurList() {
        donateursList.clear();

        Optional<JsonNode> donationsRoot = JsonManager.getRootNode("Donations.json");
        if (donationsRoot.isPresent()) {
            ArrayNode donationsArray = (ArrayNode) donationsRoot.get();

            donationsArray.forEach((JsonNode node) -> {
                int id = node.get("id").asInt();
                String nom = node.has("nom") ? node.get("nom").asText() : "Donateur inconnu";
                String nature = node.get("nature").asText();
                String date = node.get("date_don").asText();

                donateursList.add("ID: " + id + " | Nom: " + nom + " | Nature: " + nature + " | Date: " + date);
            });
        }

        donateursListView.setItems(donateursList);
    }

    @FXML
    protected void onButtonSupprimerClick(ActionEvent event) {
        String idText = idSuppressionTextField.getText().trim();

        if (idText.isEmpty()) {
            System.out.println("❌ Veuillez entrer un ID pour supprimer un donateur.");
            return;
        }

        try {
            int idToDelete = Integer.parseInt(idText);

            // Appel de la méthode dans JsonManager pour supprimer l'entrée du JSON
            JsonManager.removeDonateurById("Donations.json", idToDelete);

            // Rafraîchir la liste des donateurs après suppression
            refreshDonateurList();
        } catch (NumberFormatException e) {
            System.out.println("❌ L'ID doit être un nombre valide.");
        }
    }

    @FXML
    public void initialize() {
        donateursList = FXCollections.observableArrayList();

        Optional<JsonNode> donationsRoot = JsonManager.getRootNode("Donations.json");
        if (donationsRoot.isPresent()) {
            System.out.println("✅ Fichier JSON chargé avec succès !");
            ArrayNode donationsArray = (ArrayNode) donationsRoot.get();

            donationsArray.forEach((JsonNode node) -> {
                int id = node.get("id").asInt();
                String nom = node.has("nom") ? node.get("nom").asText() : "Donateur inconnu";
                String nature = node.get("nature").asText();
                String date = node.get("date_don").asText();

                donateursList.add("ID: " + id + " | Nom: " + nom + " | Nature: " + nature + " | Date: " + date);
            });
        } else {
            System.out.println("⚠️ Fichier Donations.json non trouvé !");
        }

        FilteredList<String> filteredList = new FilteredList<>(donateursList, _ -> true);
        rechercheTextField.textProperty().addListener((_, _, newValue) -> {
            filteredList.setPredicate(donateur -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return donateur.toLowerCase().contains(newValue.toLowerCase());
            });
        });

        donateursListView.setItems(filteredList);
    }

    @FXML
    protected void onButtonAjouterClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/Donation/AjouterDonateur.fxml"));
            Parent ajouterDonateurView = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(ajouterDonateurView, 600, 400));
            stage.setTitle("Ajouter un Donateur");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
