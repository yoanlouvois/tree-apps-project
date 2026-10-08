package App.AssociationManagement.Controller.classificationArbres;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class VotesArbresRemarquablesController {

    @FXML
    private ListView<String> listeArbresContainter;

    private static final String FILENAME = "Arbres_JSON.json";
    private static final String MEMBERS_FILENAME = "Members_JSON.json";

    @FXML
    protected void initialize() {
        loadClassementProvisoire();
        listeArbresContainter.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedItem = listeArbresContainter.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    showArbreDetails(selectedItem);
                }
            }
        });
    }

    private void loadClassementProvisoire() {
        List<JsonNode> membres = JsonManager.getNodeWithoutFilter(MEMBERS_FILENAME);
        Map<String, Integer> arbreVotes = new HashMap<>();

        for (JsonNode membre : membres) {
            if (membre.has("nominations")) {
                for (JsonNode nomination : membre.get("nominations")) {
                    if (nomination.has("idBase")) {
                        String idArbre = nomination.get("idBase").asText();
                        arbreVotes.put(idArbre, arbreVotes.getOrDefault(idArbre, 0) + 1);
                    }
                }
            }
        }

        List<JsonNode> arbres = JsonManager.getNodeWithoutFilter(FILENAME);
        List<JsonNode> arbresClassement = arbres.stream()
                .filter(arbre -> arbre.has("idBase") && arbreVotes.containsKey(arbre.get("idBase").asText()))
                .sorted((a1, a2) -> {
                    int votesDiff = arbreVotes.getOrDefault(a2.get("idBase").asText(), 0) - arbreVotes.getOrDefault(a1.get("idBase").asText(), 0);
                    if (votesDiff == 0) {
                        int circonferenceDiff = a2.has("circonference") && a1.has("circonference") ? a2.get("circonference").asInt() - a1.get("circonference").asInt() : 0;
                        if (circonferenceDiff == 0) {
                            return a2.has("hauteur") && a1.has("hauteur") ? a2.get("hauteur").asInt() - a1.get("hauteur").asInt() : 0;
                        }
                        return circonferenceDiff;
                    }
                    return votesDiff;
                })
                .limit(5)
                .collect(Collectors.toList());

        listeArbresContainter.getItems().clear();
        for (JsonNode arbre : arbresClassement) {
            String idBase = arbre.has("idBase") ? arbre.get("idBase").asText() : "Inconnu";
            String genre = arbre.has("genre") ? arbre.get("genre").asText() : "Inconnu";
            String libelleFrance = arbre.has("libelle_france") ? arbre.get("libelle_france").asText() : "Inconnu";
            String espece = arbre.has("espece") ? arbre.get("espece").asText() : "Inconnu";
            listeArbresContainter.getItems().add(idBase + " - " + genre + " - " + libelleFrance + " - " + espece);
        }
    }

    private void showArbreDetails(String selectedItem) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/classificationArbres/DetailsArbre.fxml"));
            Parent detailsView = loader.load();

            DetailsArbreController controller = loader.getController();
            controller.loadArbreDetails(selectedItem);

            Stage stage = new Stage();
            stage.setScene(new Scene(detailsView));
            stage.setTitle("Détails de l'arbre");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onButtonRetourClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/classificationArbres/ClassificationArbresRemarquables.fxml"));
            Parent arbreView = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(arbreView, 600, 400));
            stage.setTitle("Arbres remarquables");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}