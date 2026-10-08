package App.AssociationManagement.Controller.financesAssociation;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DonsSubventionsController {

    @FXML
    private ListView<String> donsSubventionsListView;

    @FXML
    private ComboBox<String> donsSubventionsFilter;

    @FXML
    private Button demandeDonsSubventionsButton;

    private static final String JSON_FILE = "Contributions_JSON.json";

    // Initialisation du contrôleur après le chargement du FXML
    @FXML
    public void initialize() {
        // Charger toutes les contributions au démarrage
        loadContributions();

        // Ajouter les options de filtrage
        donsSubventionsFilter.getItems().addAll("Tous", "Don", "Subvention");
        donsSubventionsFilter.setValue("Tous");
    }

    // Méthode pour charger les contributions dans la ListView
    private void loadContributions() {
        List<JsonNode> contributions = JsonManager.getNodeWithoutFilter(JSON_FILE);

        if (!contributions.isEmpty()) {
            donsSubventionsListView.getItems().clear();
            for (JsonNode contribution : contributions) {
                String type = contribution.get("type").asText();
                int id = contribution.get("id").asInt();
                double montant = contribution.get("montant").asDouble();
                String natureDonateur = contribution.get("natureDonateur").asText();
                String date = contribution.get("date").asText();

                // Vérifie si c'est une subvention pour afficher le rapport d'activité
                String rapportActivite = contribution.has("rapportActivite") ? contribution.get("rapportActivite").asText() : "N/A";

                // Ajout de l'entrée formatée à la ListView
                donsSubventionsListView.getItems().add(String.format(
                        "[%s] | Donateur: %s | Montant: %.2f € | Date: %s | Rapport: %s",
                        type, natureDonateur, montant, date, rapportActivite
                ));
            }
        }
    }

    // Gestion du filtrage lors du clic sur la ComboBox
    @FXML
    private void onFilterDonsSubventionsClick(ActionEvent event) {
        String selectedFilter = donsSubventionsFilter.getValue();
        List<JsonNode> contributions = JsonManager.getNodeWithoutFilter(JSON_FILE);

        if (!contributions.isEmpty()) {
            List<String> filteredItems = contributions.stream()
                    .filter(contribution ->
                            selectedFilter.equals("Tous") ||
                                    contribution.get("type").asText().equalsIgnoreCase(selectedFilter))
                    .map(contribution -> {
                        String type = contribution.get("type").asText();
                        double montant = contribution.get("montant").asDouble();
                        String natureDonateur = contribution.get("natureDonateur").asText();
                        String date = contribution.get("date").asText();
                        String rapportActivite = contribution.has("rapportActivite") ? contribution.get("rapportActivite").asText() : "N/A";

                        return String.format(
                                "[%s] | Donateur: %s | Montant: %.2f € | Date: %s | Rapport: %s",
                                type, natureDonateur, montant, date, rapportActivite
                        );
                    })
                    .collect(Collectors.toList());

            donsSubventionsListView.getItems().setAll(filteredItems);
        }
    }

    // Ouverture de la fenêtre de demande de don ou de subvention
    @FXML
    private void onButtonDonsSubventionsClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/ChoixDonsSubventions.fxml"));
            Parent choixDonsSubventionsView = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(choixDonsSubventionsView));
            stage.setTitle("Faire une demande");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Retour à l'écran précédent
    @FXML
    protected void onButtonRetourClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/FinancesAssociation.fxml"));
            Parent financesView = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(financesView, 600, 400));
            stage.setTitle("Finances de l'association");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
