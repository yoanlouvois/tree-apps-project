package App.AssociationMember;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import others.Message;
import others.Nomination;
import others.Tree;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DetailsController {

    @FXML
    private TextFlow description;

    @FXML
    private JFXButton votesButton;

    private JsonNode user;
    private JsonNode arbre;

    public void setUser(JsonNode user, JsonNode arbre){
        this.user=user;
        this.arbre=arbre;
        update();
    }


    public void update(){
        Text text = new Text(Tree.arbreNodeToString(arbre));
        description.getChildren().add(text);

        votesButton.setOnAction((event) -> {
            JsonNode choix = arbre.deepCopy();
            String id = arbre.get("idBase").asText();  // ✅ Convertir en String
            int votes = user.get("nominations").size();

            ((ObjectNode) choix).retain("idBase", "libelle_france");
            ArrayNode votesArray = (ArrayNode) user.get("nominations");

            boolean canVote = true;
            for (JsonNode vote : votesArray) {
                System.out.println(vote.get("idBase").asText());
                if (vote.get("idBase").asText().equals(id)) {  // ✅ Correction de la comparaison
                    Message.showInformation("Erreur", "Vous avez déjà voté pour cet arbre");
                    canVote = false;
                    break;
                }
            }

            if (canVote) {
                Optional<ButtonType> result = Message.showConfirmation("Confirmation", "Voulez-vous vraiment voter pour cet arbre?").showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (votes >= 5) {
                        votesArray.remove(0);
                    }
                    votesArray.add(choix);

                    // ✅ Met à jour le JSON des membres
                    JsonManager.updateJsonObject("Members_JSON.json",
                            Map.entry("identifiant", user.get("identifiant").asText()),
                            Map.entry("nominations", votesArray));

                    ArrayNode nominationListe = JsonManager.getArrayNode("Nominations_JSON.json");

                    JsonNode existingNomination = null;

                    // ✅ Vérifier si l'arbre est déjà nominé
                    for (JsonNode node : nominationListe) {
                        if (node.has("idBase") && node.get("idBase").asText().equals(id)) {  // ✅ Correction de la comparaison
                            existingNomination = node;
                            break;
                        }
                    }

                    if (existingNomination == null) {
                        // ✅ L'arbre n'a pas encore été nominé, création d'une nouvelle nomination
                        try {
                            Tree tree = JsonManager.objectMapper.treeToValue(arbre, Tree.class);
                            Nomination nomination = new Nomination(tree, 1); // Ajout avec un vote initial
                            JsonManager.insertInJson("Nominations_JSON.json", List.of(nomination), "idBase");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // ✅ L'arbre a déjà été nominé, on incrémente le nombre de votes
                        int currentVotes = existingNomination.has("votes") ? existingNomination.get("votes").asInt() : 0;  // ✅ Vérification
                        int updatedVotes = currentVotes + 1;

                        // ✅ Mise à jour du fichier JSON avec le nouveau nombre de votes
                        JsonManager.updateJsonObject("Nominations_JSON.json",
                                Map.entry("idBase", id),
                                Map.entry("votes", updatedVotes)
                        );
                    }
                }
            }
        });
    }



}
