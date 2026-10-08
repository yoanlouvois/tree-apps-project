package App.AssociationManagement.Controller.finExerciceBudgetaire;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConstitutionTransmissionListeArbresRemarquablesController {

    @FXML
    private ListView<String> listeArbresContainter;

    @FXML
    private Button GreenServiceSubmit;

    private static final String FILENAME = "Arbres_JSON.json";
    private static final String MEMBERS_FILENAME = "Members_JSON.json";
    private static final String REMARKABLE_JSON = "ArbreRemarquableChoisi.json";

    @FXML
    protected void initialize() {
        // Charger le classement des arbres et configurer l'action du bouton
        loadClassementProvisoire();
        GreenServiceSubmit.setOnAction(this::onGreenServiceSubmit);
    }

    @FXML
    protected void onButtonRetourClick(ActionEvent event) {
        // Retourner à la vue précédente
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/finExerciceBudgetaire/AccueilFinExerciceBudgetaire.fxml"));
            Parent membreView = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(membreView, 600, 400));
            stage.setTitle("Exercice Budgétaire");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadClassementProvisoire() {
        // Charger les votes des membres et classer les arbres en fonction des votes
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

        // Charger les arbres et les classer par votes
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

        // Mettre à jour la ListView
        listeArbresContainter.getItems().clear();
        for (JsonNode arbre : arbresClassement) {
            String idBase = arbre.has("idBase") ? arbre.get("idBase").asText() : "Inconnu";
            String genre = arbre.has("genre") ? arbre.get("genre").asText() : "Inconnu";
            String libelleFrance = arbre.has("libelle_france") ? arbre.get("libelle_france").asText() : "Inconnu";
            String espece = arbre.has("espece") ? arbre.get("espece").asText() : "Inconnu";
            listeArbresContainter.getItems().add(idBase + " - " + genre + " - " + libelleFrance + " - " + espece);
        }
    }

    private void onGreenServiceSubmit(ActionEvent event) {
        // Soumettre tous les arbres affichés dans la ListView
        List<String> items = listeArbresContainter.getItems();

        if (items.isEmpty()) {
            // Alerte si la liste est vide
            Alert alert = new Alert(Alert.AlertType.WARNING, "Aucun arbre à soumettre.", javafx.scene.control.ButtonType.OK);
            alert.show();
            return;
        }

        List<JsonNode> arbres = JsonManager.getNodeWithoutFilter(FILENAME);

        // Créer une liste pour les changements à soumettre
        List<Map<String, Object>> changements = items.stream()
                .map(item -> {
                    String[] parts = item.split(" - ");
                    String idBase = parts[0];

                    // Trouver l'arbre correspondant dans le fichier JSON
                    JsonNode selectedTree = arbres.stream()
                            .filter(arbre -> arbre.has("idBase") && arbre.get("idBase").asText().equals(idBase))
                            .findFirst()
                            .orElse(null);

                    if (selectedTree != null) {
                        Map<String, Object> changement = new HashMap<>();
                        changement.put("TypeChangement", "Proposition arbre remarquable");
                        changement.put("Arbre", selectedTree);
                        changement.put("idBase", selectedTree.get("idBase").asText());
                        return changement;
                    }
                    return null;
                })
                .filter(changement -> changement != null)
                .collect(Collectors.toList());

        if (!changements.isEmpty()) {
            // Insérer les changements dans le fichier JSON
            JsonManager.insertInJson(REMARKABLE_JSON, new ArrayList<>(changements), "idBase");

            // Alerte de confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Tous les arbres ont été soumis au service des espaces verts !", javafx.scene.control.ButtonType.OK);
            alert.show();
        }
    }
}