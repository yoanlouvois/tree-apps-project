package App.GreenSpaceService;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TreeListController {

    @FXML
    private TextField textFiltrerCirconference;

    @FXML
    private TableView<Tree> treeTableView;

    @FXML
    private TableColumn<Tree, String> colId;

    @FXML
    private TableColumn<Tree, Void> colActions;

    @FXML
    private TableColumn<Tree, String> colNom;

    @FXML
    private TableColumn<Tree, String> colGenre;

    @FXML
    private TableColumn<Tree, String> colEspece;

    @FXML
    private TableColumn<Tree, String> colLieu;

    @FXML
    private ComboBox<String> filterComboBox;

    private final ObservableList<Tree> treeList = FXCollections.observableArrayList();
    private final ObservableList<Tree> filteredList = FXCollections.observableArrayList();

    @FXML
    public void OnActionButtonClicked5(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource
                    ("/App/GreenServiceSpace/three-manager-view.fxml"));
            Parent secondView = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene()
                    .getWindow();
            stage.setScene(new Scene(secondView, 800, 600));
            stage.getScene().getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource
                            ("/App/GreenServiceSpace/styles.css")).toExternalForm());
            stage.setTitle("Gestion des espaces verts");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize() {
        // Configurer les colonnes pour mapper les propriétés de Tree
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colEspece.setCellValueFactory(new PropertyValueFactory<>("espece"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));

        addButtonToTable();
        loadTreeData();
        treeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        treeTableView.setSelectionModel(null);

        ObservableList<String> filters = FXCollections.observableArrayList(
                "Aucun filtre",
                "Arbres remarquables",
                "Arbres non remarquables",
                "circonférences supérieur à :"
        );
        // Associer les filtres au ComboBox
        filterComboBox.setItems(filters);
        // Optionnel : Sélectionner un filtre par défaut
        filterComboBox.getSelectionModel().select("Aucun filtre");
        // Ajouter un ChangeListener pour détecter les changements de filtre
        filterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> applyFilter(newValue));

        // Ajouter un écouteur sur le champ `TextField` pour appliquer le filtre de circonférence
        textFiltrerCirconference.setOnKeyReleased(this::filterByCirconference);
        // Initialiser la liste affichée par défaut (sans filtre)
        filteredList.addAll(treeList);
        treeTableView.setItems(filteredList);
        textFiltrerCirconference.setVisible(false);
    }

    public void loadTreeData() {
        JsonManager jsonManager = JsonManager.INSTANCE;
        List<JsonNode> arbreList = jsonManager.getNodeWithoutFilter("Arbres_JSON.json");

        // Convertir les données JSON en objets Tree et les ajouter à la liste
        arbreList.forEach((JsonNode node) -> {
            String id = node.get("idBase").asText();
            String nom = node.get("libelle_france").asText();
            String genre = node.get("genre").asText();
            String espece = node.get("espece").asText();
            String lieu = node.get("lieu").asText();
            String remarquable = node.get("remarquable").asText();
            String latitude = node.get("latitude").asText();
            String longitude = node.get("longitude").asText();
            String hauteur = node.get("hauteur").asText();
            String circonference = node.get("circonference").asText();
            String developpementStage = node.get("stade_de_developpement").asText();

            treeList.add(new Tree(id, nom, genre, espece, lieu, remarquable,latitude,
                    longitude,hauteur,circonference,developpementStage));
        });

        // Ajouter les données au TableView
        treeTableView.setItems(treeList);
    }

    public void removeTree(Tree tree) {
        treeList.remove(tree); // Supprime de la liste observable
        treeTableView.setItems(treeList); // Met à jour la TableView
        logChangeToNotifFile("Suppression", tree);
    }

    private void addButtonToTable() {
        // Ajout de la colonne pour les boutons
        Callback<TableColumn<Tree, Void>, TableCell<Tree, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Tree, Void> call(final TableColumn<Tree, Void> param) {
                return new TableCell<>() {

                    private final Button btn = new Button("infos");

                    {
                        btn.setOnAction(event -> {
                            // Obtenir l'arbre correspondant à cette ligne
                            Tree tree = getTableView().getItems().get(getIndex());

                            // Créer une nouvelle fenêtre pour afficher les infos de l'arbre
                            try {
                                Stage secondStage = new Stage(); // Nouveau Stage
                                FXMLLoader TreeLoader = new FXMLLoader(getClass().getResource("/App/GreenServiceSpace/three-option-view.fxml"));

                                // Charger la vue
                                Parent root = TreeLoader.load();

                                // Récupérer le contrôleur
                                TreeOptionController controller = TreeLoader.getController();

                                // Passer les informations de l'arbre au contrôleur
                                controller.setTreeInfo(tree);
                                controller.setParentController(TreeListController.this); // Passer la référence au contrôleur parent

                                // Configurer et afficher la scène
                                Scene scene1 = new Scene(root);
                                secondStage.setTitle("Infos arbre");
                                secondStage.setScene(scene1);
                                secondStage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                // Ajouter un message d'erreur utilisateur ou un log si nécessaire
                            }
                        });

                        // Ajout de style CSS au bouton
                        btn.setStyle("-fx-background-color: rgba(46, 139, 87,0.5); -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        colActions.setCellFactory(cellFactory);
    }

    public void updateTreeRemarkableStatus(Tree tree, String newStatus) {
        // Mettre à jour l'arbre dans le fichier JSON
        boolean isUpdated = JsonManager.INSTANCE.updateTreeRemarkableStatus("Arbres_JSON.json", tree.getId(), newStatus);

        if (isUpdated) {
            // Mettre à jour l'objet Tree dans la liste observable
            tree.setRemarquable(newStatus);

            // Rafraîchir la TableView
            treeTableView.refresh();
            logChangeToNotifFile("Changement en arbre remarquable", tree);
            //System.out.println("✅ Arbre mis à jour : " + tree.getId() + " est maintenant " + newStatus);

        } else {
            //System.out.println("❌ Impossible de mettre à jour l'arbre dans le fichier JSON.");
        }
    }

    private void applyFilter(String filter) {
        filteredList.clear();

        switch (filter) {
            case "Aucun filtre":
                // Ajouter tous les arbres sans filtrer
                filteredList.addAll(treeList);
                textFiltrerCirconference.setVisible(false);
                break;

            case "Arbres remarquables":
                // Filtrer uniquement les arbres remarquables
                filteredList.addAll(treeList.stream()
                        .filter(tree -> "OUI".equalsIgnoreCase(tree.getRemarquable()))
                        .toList());
                textFiltrerCirconference.setVisible(false);
                break;

            case "Arbres non remarquables":
                // Filtrer uniquement les arbres non remarquables
                filteredList.addAll(treeList.stream()
                        .filter(tree -> "NON".equalsIgnoreCase(tree.getRemarquable()))
                        .toList());
                textFiltrerCirconference.setVisible(false);
                break;

            case "circonférences supérieur à :":
                textFiltrerCirconference.setVisible(true);
                filteredList.addAll(treeList);

            default:
                break;
        }

        // Mettre à jour la TableView
        treeTableView.setItems(filteredList);
    }

    private void filterByCirconference(KeyEvent event) {
        try {
            String input = textFiltrerCirconference.getText();
            if (input.isEmpty()) {
                filteredList.clear();
                filteredList.addAll(treeList); // Aucun filtre si la saisie est vide
            } else {
                double minCirconference = Double.parseDouble(input);

                // Filtrer en fonction de la circonférence
                filteredList.clear();
                filteredList.addAll(treeList.stream()
                        .filter(tree -> {
                            try {
                                return Double.parseDouble(tree.getCirconference()) >= minCirconference;
                            } catch (NumberFormatException e) {
                                return false; // Ignore les valeurs non valides
                            }
                        })
                        .toList());
            }

            // Mettre à jour la TableView
            treeTableView.setItems(filteredList);

        } catch (NumberFormatException e) {
            System.out.println("❌ Valeur de circonférence invalide : " + textFiltrerCirconference.getText());
        }
    }

    private void logChangeToNotifFile(String typeChangement, Tree tree) {
        try {
            // Créer une map pour représenter le changement
            Map<String, Object> changeLog = new HashMap<>();
            changeLog.put("TypeChangement", typeChangement);

            Map<String, Object> treeData = new HashMap<>();
            treeData.put("id", tree.getId());
            treeData.put("nom", tree.getNom());
            treeData.put("genre", tree.getGenre());
            treeData.put("espece", tree.getEspece());
            treeData.put("lieu", tree.getLieu());
            treeData.put("remarquable", tree.getRemarquable());
            treeData.put("latitude", tree.getLatitude());
            treeData.put("longitude", tree.getLongitude());
            treeData.put("hauteur", tree.getHauteur());
            treeData.put("circonference", tree.getCirconference());
            treeData.put("stade_de_developpement", tree.getDeveloppementStage());

            changeLog.put("Arbre", treeData);

            // Appeler JsonManager pour insérer dans le fichier JSON
            JsonManager.insertInJson("GreenSpaceNotif.json", List.of(changeLog));
            System.out.println("✅ Changement enregistré dans GreenSpaceNotif.json : " + typeChangement);
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de l'enregistrement du changement : " + e.getMessage());
        }
    }
}
