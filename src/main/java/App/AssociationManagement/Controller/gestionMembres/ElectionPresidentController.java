package App.AssociationManagement.Controller.gestionMembres;

import App.AssociationMember.Member;
import App.AssociationMember.President;
import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import others.Message;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class ElectionPresidentController {

    @FXML
    private TextField nomTextField;
    @FXML
    private ListView<String> nomListView;

    private Label nomPrenomPresidentLabel = new Label();

    @FXML
    private VBox detailsContainer;

    private ObservableList<String> memberList;
    private JsonNode selectedMember;
    private static final String JSON_FILE_NAME = "Members_JSON.json";
    private static final String PRESIDENT_JSON_FILE = "President.json";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @FXML
    public void initialize() {
        memberList = FXCollections.observableArrayList();
        loadPresidentFromFile();

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
        if (selected == null) return;
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
        });
    }

    @FXML
    protected void onButtonValiderClick(ActionEvent event) {
        if (selectedMember != null) {
            Member newPresident = new Member(
                    new Member(
                            selectedMember.get("nom").asText(),
                            selectedMember.get("prenom").asText(),
                            selectedMember.get("age").asInt(),
                            LocalDate.parse(selectedMember.get("dateNaissance").asText(), DATE_FORMATTER),
                            selectedMember.get("adresse").asText()
                    ),
                    selectedMember.get("identifiant").asText(),
                    selectedMember.get("password").asText(),
                    LocalDate.parse(selectedMember.get("dateInscription").asText(), DATE_FORMATTER)
            );
            President.INSTANCE.setPresident(newPresident);
            savePresidentToFile(newPresident);
            nomPrenomPresidentLabel.setText(newPresident.getPrenom() + " " + newPresident.getNom());

            Message.showInformation("Président élu", "Le nouveau président est " + newPresident.getPrenom() + " " + newPresident.getNom());

            retournerPageGestionMembres(event, newPresident);
        } else {
            Message.showAlert("Erreur", "Veuillez sélectionner un membre avant de valider.");
        }
    }

    private void loadPresidentFromFile() {
        Optional<JsonNode> presidentNode = JsonManager.getNodeWithoutFilter(PRESIDENT_JSON_FILE).stream().findFirst();
        presidentNode.ifPresent(node -> nomPrenomPresidentLabel.setText(node.get("prenom").asText() + " " + node.get("nom").asText()));
    }

    private void savePresidentToFile(Member president) {
        JsonManager.insertInJson(PRESIDENT_JSON_FILE, Collections.singletonList(president));
    }

    @FXML
    protected void onButtonRetourClick(ActionEvent event) {
        retournerPagePrecedente(event);
    }

    @FXML
    protected void onButtonAnnulerClick(ActionEvent event) {
        nomTextField.clear();
        nomListView.getSelectionModel().clearSelection();
        Message.showInformation("Action annulée", "La sélection a été réinitialisée.");
        retournerPagePrecedente(event);
    }

    private void retournerPageGestionMembres(ActionEvent event, Member president) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/gestionMembres/GestionMembres.fxml"));
            Parent membreView = loader.load();
            GestionMembresController controller = loader.getController();
            controller.initialize();
            //controller.setPresidentName(president.getPrenom() + " " + president.getNom());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(membreView, 600, 400));
            stage.setTitle("Gestion des membres de l'association");
            stage.show();
        } catch (IOException e) {
            Message.showAlert("Erreur", "Impossible de charger la page de gestion des membres.");
        }
    }

    private void retournerPagePrecedente(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/gestionMembres/GestionMembres.fxml"));
            Parent membreView = loader.load();
            GestionMembresController controller = loader.getController();
            controller.initialize();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(membreView, 600, 400));
            stage.setTitle("Gestion des membres de l'association");
            stage.show();
        } catch (IOException e) {
            Message.showAlert("Erreur", "Impossible de retourner à la page précédente.");
        }
    }

}
