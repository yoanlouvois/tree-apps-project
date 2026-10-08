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
import java.util.List;
import java.util.stream.Collectors;

public class ListeArbresRemarquablesController {

    @FXML
    private ListView<String> listeArbresContainter;

    private static final String FILENAME = "Arbres_JSON.json";

    @FXML
    protected void initialize() {
        loadArbresRemarquables();
        listeArbresContainter.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedItem = listeArbresContainter.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    showArbreDetails(selectedItem);
                }
            }
        });
    }

    private void loadArbresRemarquables() {
        List<JsonNode> arbres = JsonManager.getNodeWithoutFilter(FILENAME);
        listeArbresContainter.getItems().clear();
        List<JsonNode> arbresRemarquables = arbres.stream()
                .filter(arbre -> arbre.has("remarquable") && "OUI".equalsIgnoreCase(arbre.get("remarquable").asText(null)))
                .collect(Collectors.toList());
        for (JsonNode arbre : arbresRemarquables) {
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
