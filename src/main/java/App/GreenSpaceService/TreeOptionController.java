package App.GreenSpaceService;

import Data.JsonManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Objects;

public class TreeOptionController {

    @FXML
    private Label idLabel;
    @FXML
    private Label nomLabel;
    @FXML
    private Label genreLabel;
    @FXML
    private Label especeLabel;
    @FXML
    private Label lieuLabel;
    @FXML
    private Label remarquableLabel;
    @FXML
    private Label latitudeLabel;
    @FXML
    private Label longitudeLabel;
    @FXML
    private Label hauteurLabel;
    @FXML
    private Label circonferenceLabel;
    @FXML
    private Label developpementStageLabel;
    @FXML
    private Button deleteButton;
    @FXML
    private Button saveButton;

    private Tree tree; // L'arbre courant
    private TreeListController parentController; // Référence au contrôleur principal

    // Méthode pour définir les informations d'un arbre
    public void setTreeInfo(Tree tree) {
        this.tree = tree;
        idLabel.setText("ID : " + tree.getId());
        nomLabel.setText("Nom : " + tree.getNom());
        genreLabel.setText("Genre : " + tree.getGenre());
        especeLabel.setText("Espèce : " + tree.getEspece());
        lieuLabel.setText("Lieu : " + tree.getLieu());
        remarquableLabel.setText("Remarquable : " + tree.getRemarquable());
        latitudeLabel.setText("Latitude : " + tree.getLatitude());
        longitudeLabel.setText("Longitude : " + tree.getLongitude());
        hauteurLabel.setText("Hauteur : " + tree.getHauteur());
        circonferenceLabel.setText("Circonférence : " + tree.getCirconference());
        developpementStageLabel.setText("Stade de développement : " + tree.getDeveloppementStage());
        if(Objects.equals(tree.getRemarquable(), "OUI")){
            deleteButton.setDisable(true);
            saveButton.setDisable(true);
        }
    }

    // Méthode pour recevoir la référence au contrôleur parent
    public void setParentController(TreeListController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void onDeleteButtonClick() {
        if (tree != null && parentController != null) {
            // Supprimer l'arbre du fichier JSON
            boolean isRemoved = JsonManager.INSTANCE.removeNode("Arbres_JSON.json", tree.getId());

            if (isRemoved) {
                // Supprimer l'arbre de la table et fermer la fenêtre
                parentController.removeTree(tree);
                idLabel.getScene().getWindow().hide();
            } else {
                System.out.println("❌ Impossible de supprimer l'arbre du fichier JSON.");
            }
        }
    }

    @FXML
    public void onTransformToRemarkableButtonClick() {
        if (tree != null && parentController != null) {
            // Mettre à jour l'arbre comme remarquable
            parentController.updateTreeRemarkableStatus(tree, "OUI");

            // Fermer la fenêtre après la mise à jour
            idLabel.getScene().getWindow().hide();
        }
    }
}
