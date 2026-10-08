package App.AssociationManagement.Controller.gestionVisiteArbre;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Collections;
import Data.JsonManager;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

public class AjouterVisiteController {

    @FXML
    private TextField nomArbreTextField, lieuTextField, coutTextField;

    @FXML
    private DatePicker dateVisitePicker;

    @FXML
    protected void onButtonAjouterClick(ActionEvent event) {
        String nomArbre = nomArbreTextField.getText().trim();
        String lieu = lieuTextField.getText().trim();
        LocalDate dateVisite = dateVisitePicker.getValue();
        String cout = coutTextField.getText().trim();

        if (nomArbre.isEmpty() || lieu.isEmpty() || dateVisite == null || cout.isEmpty()) {
            System.out.println("Tous les champs doivent être remplis !");
            return;
        }

        // Charger le fichier JSON pour récupérer le dernier ID
        Optional<JsonNode> visiteRoot = JsonManager.getRootNode("Visites_JSON.json");
        int newId = 1; // Valeur par défaut si le fichier est vide

        if (visiteRoot.isPresent()) {
            ArrayNode visiteArray = (ArrayNode) visiteRoot.get();
            if (visiteArray.size() > 0) {
                JsonNode lastNode = visiteArray.get(visiteArray.size() - 1);
                if (lastNode.has("id")) {
                    newId = lastNode.get("id").asInt() + 1;
                }
            }
        }

        // Création d'un ObjectNode avec ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode visiteNode = objectMapper.createObjectNode();

        visiteNode.put("id", newId);
        visiteNode.put("date", dateVisite.toString());
        visiteNode.put("cout", cout);

        // Créer l'objet "tree" pour stocker nomArbre et lieu
        ObjectNode treeNode = objectMapper.createObjectNode();
        treeNode.put("libelle_france", nomArbre);
        treeNode.put("lieu", lieu);

        // Ajouter "tree" à la visite
        visiteNode.set("tree", treeNode);

        // Ajouter la visite au fichier JSON
        JsonManager.insertInJson("Visites_JSON.json", Collections.singletonList(visiteNode));
        System.out.println("Visite ajoutée avec succès : " + visiteNode);

        // Revenir à la liste des visites
        onButtonRetourClick(event);
    }

    @FXML
    protected void onButtonRetourClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/gestionVisiteArbre/VisitesArbres.fxml"));
            Parent visiteView = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(visiteView, 600, 400));
            stage.setTitle("Liste des visites d'arbres");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
