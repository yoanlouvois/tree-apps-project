package App.AssociationManagement.Controller.financesAssociation;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import others.Message;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CotisationsController {

    @FXML
    private ListView<String> payedCotisationsListView;

    @FXML
    private ListView<String> unpayedCotisationsListView;

    @FXML
    private Button cotisationsTotalesButton;

    private static final String FILE_NAME = "Members_JSON.json";

    @FXML
    public void initialize() {
        loadCotisationsData();
    }

    private void loadCotisationsData() {
        List<JsonNode> members = JsonManager.getAllNodes(FILE_NAME);

        if (members.isEmpty()) {
            Message.showAlert("Erreur", "Aucun membre trouvé dans le fichier.");
            return;
        }

        // Séparer les membres ayant payé et ceux n'ayant pas payé leur cotisation
        List<String> payedMembers = members.stream()
                .filter(node -> node.has("cotisationsPayees") && node.get("cotisationsPayees").size() > 0)
                .map(node -> node.get("nom").asText() + " " + node.get("prenom").asText())
                .collect(Collectors.toList());

        List<String> unpayedMembers = members.stream()
                .filter(node -> node.has("cotisationsPayees") && node.get("cotisationsPayees").isEmpty())
                .map(node -> node.get("nom").asText() + " " + node.get("prenom").asText())
                .collect(Collectors.toList());

        // Mettre à jour les ListView
        payedCotisationsListView.getItems().setAll(payedMembers);
        unpayedCotisationsListView.getItems().setAll(unpayedMembers);
    }

    @FXML
    protected void onButtonRetourClick(ActionEvent event) {
        try {
            // Charger la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/FinancesAssociation.fxml"));
            Parent financesView = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(new Scene(financesView, 600,400));
            //stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/App/AssociationManagement/styles.css")).toExternalForm());
            stage.setTitle("Finances de l'association");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onButtonCotisationsTotalesClick(ActionEvent event) {
        List<JsonNode> members = JsonManager.getAllNodes(FILE_NAME);

        if (members.isEmpty()) {
            Message.showAlert("Erreur", "Impossible de calculer la recette totale.");
            return;
        }

        int totalCotisations = members.stream()
                .filter(node -> node.has("cotisationsPayees") && node.get("cotisationsPayees").isArray())
                .flatMapToInt(node -> {
                    // Convertir les éléments de la liste cotisationsPayees en flux d'entiers
                    return convertJsonArrayToIntStream(node.get("cotisationsPayees"));
                })
                .sum();

        Message.showInformation("Recette totale", "Le montant total des cotisations est de : " + totalCotisations + " €");
    }

    // Méthode utilitaire pour convertir une liste JSON en flux d'entiers
    private static java.util.stream.IntStream convertJsonArrayToIntStream(JsonNode jsonArray) {
        return java.util.stream.StreamSupport.stream(jsonArray.spliterator(), false)
                .mapToInt(JsonNode::asInt);
    }


}
