package App.AssociationManagement.Controller.gestionMembres;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;

public class VoirListeMembresController {

    @FXML
    private TextField nomTextField;
    @FXML
    private ListView<String> nomListView;
    @FXML
    private VBox detailsContainer;
    @FXML
    private Button cotisationsButton;

    private ObservableList<String> memberList;
    private JsonNode selectedMember;
    private static final String JSON_FILE_NAME = "Members_JSON.json";

    @FXML
    protected void onNotificationsClick(ActionEvent event) {
        try {
            System.out.println("Tentative de chargement de NotificationAccueil.fxml");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/NotificationManagement/NotificationAccueil.fxml"));
            Parent notificationView = loader.load();

            // Obtenir le stage à partir de n'importe quel Node connu
            MenuItem menuItem = (MenuItem) event.getSource();
            Scene scene = menuItem.getParentPopup().getOwnerWindow().getScene();
            Stage stage = (Stage) scene.getWindow();

            // Charger la nouvelle scène
            stage.setScene(new Scene(notificationView, 600, 400));
            stage.setTitle("Notifications");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Impossible de charger la page des notifications.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    public void initialize() {
        memberList = FXCollections.observableArrayList();
        cotisationsButton.setVisible(false);

        Optional<JsonNode> memberRoot = JsonManager.getRootNode(JSON_FILE_NAME);
        memberRoot.ifPresent(members -> members.forEach(node -> {
            String identifiant = node.get("identifiant").asText();
            String nom = node.get("nom").asText();
            String prenom = node.get("prenom").asText();
            memberList.add("Identifiant: " + identifiant + " Nom: " + prenom + " " + nom);
        }));

        FilteredList<String> filteredList = new FilteredList<>(memberList, _ -> true);
        nomTextField.textProperty().addListener((_, _, newValue) -> {
            filteredList.setPredicate(member -> newValue == null || newValue.isEmpty() || member.toLowerCase().contains(newValue.toLowerCase()));
        });

        nomListView.setItems(filteredList);
        nomListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                afficherDetailsMembre();
            }
        });
    }

    private void afficherDetailsMembre() {
        detailsContainer.getChildren().clear();
        String selected = nomListView.getSelectionModel().getSelectedItem();

        cotisationsButton.setVisible(false);

        if (selected == null) {
            return;
        }

        String identifiant = selected.split(" ")[1];
        Optional<JsonNode> membreOption = JsonManager.getNode(JSON_FILE_NAME, Map.entry("identifiant", identifiant));

        membreOption.ifPresent(membre -> {
            selectedMember = membre;

            detailsContainer.getChildren().add(new Label("Nom: " + membre.get("nom").asText()));
            detailsContainer.getChildren().add(new Label("Prénom: " + membre.get("prenom").asText()));
            detailsContainer.getChildren().add(new Label("Âge: " + membre.get("age").asText()));
            detailsContainer.getChildren().add(new Label("Adresse: " + membre.get("adresse").asText()));
            detailsContainer.getChildren().add(new Label("Date de naissance: " + membre.get("dateNaissance").asText()));
            detailsContainer.getChildren().add(new Label("Identifiant: " + membre.get("identifiant").asText()));
            detailsContainer.getChildren().add(new Label("Cotisation payée: " + (membre.get("cotisationPayee").asBoolean() ? "Oui" : "Non")));

            cotisationsButton.setVisible(true);

        });
    }

    @FXML
    protected void onButtonRetourClick(ActionEvent event) {
        retournerPagePrecedente(event);
    }

    @FXML
    protected void onButtonCotisationsClick() {
        if (selectedMember != null) {
            boolean cotisationPayee = selectedMember.get("cotisationPayee").asBoolean();

            // Vérifier si la liste cotisationsPayees existe et n'est pas vide
            if (selectedMember.has("cotisationsPayees") && selectedMember.get("cotisationsPayees").isArray()) {
                ArrayNode cotisationsArray = (ArrayNode) selectedMember.get("cotisationsPayees");

                double totalCotisation = 0.0;
                for (JsonNode montantNode : cotisationsArray) {
                    totalCotisation += montantNode.asDouble(0.0);  // Ajouter la cotisation en vérifiant si la valeur est correcte
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Recette des cotisations");
                alert.setHeaderText("Détails de la cotisation");
                alert.setContentText("Nombre de cotisations: " + cotisationsArray.size() + "\n" +
                        "Montant total des cotisations: " + totalCotisation + " €\n" +
                        "Statut: " + (cotisationPayee ? "À jour" : "Non à jour"));
                alert.showAndWait();
            } else {
                // Si la liste est vide ou absente
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Recette des cotisations");
                alert.setHeaderText("Détails de la cotisation");
                alert.setContentText("Aucune cotisation enregistrée pour ce membre.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Aucun membre sélectionné.", ButtonType.OK);
            alert.showAndWait();
        }
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
