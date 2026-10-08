package App.AssociationManagement.Controller.classificationArbres;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.Iterator;
import java.util.Map.Entry;

public class DetailsArbreController {

    @FXML
    private TextFlow description;

    private static final String FILENAME = "Arbres_JSON.json";

    public void loadArbreDetails(String selectedItem) {
        System.out.println("Recherche de l'arbre pour: " + selectedItem);
        List<JsonNode> arbres = JsonManager.getNodeWithoutFilter(FILENAME);

        for (JsonNode arbre : arbres) {
            String idBase = arbre.has("idBase") ? arbre.get("idBase").asText() : "";
            System.out.println("Comparaison avec ID: " + idBase);
            if (selectedItem.contains(idBase)) {
                System.out.println("Arbre trouvé !");
                displayArbreDetails(arbre);
                break;
            }
        }
        System.out.println("Aucun arbre correspondant trouvé.");
    }


    private void displayArbreDetails(JsonNode arbre) {

        StringBuilder details = new StringBuilder();

        if (arbre != null) {
            details.append("ID: ").append(arbre.has("idBase") ? arbre.get("idBase").asText() : "Inconnu").append("\n");
            details.append("Genre: ").append(arbre.has("genre") ? arbre.get("genre").asText() : "Inconnu").append("\n");
            details.append("Espèce: ").append(arbre.has("espece") ? arbre.get("espece").asText() : "Inconnu").append("\n");
            details.append("Circonférence: ").append(arbre.has("circonference") ? arbre.get("circonference").asInt() + " cm" : "Non spécifiée").append("\n");
            details.append("Hauteur: ").append(arbre.has("hauteur") ? arbre.get("hauteur").asInt() + " m" : "Non spécifiée").append("\n");
            details.append("Lieu: ").append(arbre.has("lieu") ? arbre.get("lieu").asText() : "Non spécifié").append("\n");
            details.append("Latitude: ").append(arbre.has("latitude") ? arbre.get("latitude").asDouble() : "Non spécifiée").append("\n");
            details.append("Longitude: ").append(arbre.has("longitude") ? arbre.get("longitude").asDouble() : "Non spécifiée").append("\n");
            details.append("Stade de développement: ").append(arbre.has("stade_de_developpement") ? arbre.get("stade_de_developpement").asText() : "Non spécifié").append("\n");
            details.append("Remarquable: ").append(arbre.has("remarquable") ? arbre.get("remarquable").asText() : "Non spécifié").append("\n");

        } else {
            details.append("Aucune information disponible");
        }

        description.getChildren().clear();
        description.getChildren().add(new Text(details.toString()));
    }

}
