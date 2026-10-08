package App.AssociationMember;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class ArbreListeController {

    @FXML
    private JFXDrawer JFXDrawer;

    @FXML
    private JFXHamburger JFXHamburger;

    @FXML
    private ListView<String> treeListView;

    @FXML
    private ImageView logo;

    @FXML
    private TextField recherche;

    @FXML
    private VBox vboxMenu;
    @FXML
    private JFXButton moreButton;

    private JsonNode user;
    private ObservableList<JsonNode> treeList;
    private ArrayNode arbre;
    private FilteredList<JsonNode> filteredList;
    private ChangeListener<String> rechercheListener; // Permet de stocker le listener actif

    public void setUser(JsonNode user){
        this.user=user;
        updateMenu();
        handleCellClick();
        handeMoreButton();
    }

    private Predicate<JsonNode> defaultFilter(String newValue) {
        return tree -> {
            if (newValue == null || newValue.isEmpty()) {
                return true; // Aucun filtre, afficher tout
            }

            String lowerCaseFilter = newValue.toLowerCase();

            // Vérifier si les champs existent et correspondent au filtre
            boolean matchNom = tree.has("libelle_france") && tree.get("libelle_france").asText().toLowerCase().contains(lowerCaseFilter);
            boolean matchGenre = tree.has("genre") && tree.get("genre").asText().toLowerCase().contains(lowerCaseFilter);
            boolean matchEspece = tree.has("espece") && tree.get("espece").asText().toLowerCase().contains(lowerCaseFilter);
            boolean matchLieu = tree.has("lieu") && tree.get("lieu").asText().toLowerCase().contains(lowerCaseFilter);
            boolean matchDeveloppement= tree.has("stade_de_developpement") && tree.get("stade_de_developpement").asText().toLowerCase().contains(lowerCaseFilter);
            boolean matchRemarquable = tree.has("remarquable") && tree.get("remarquable").asText().toLowerCase().contains(lowerCaseFilter);
            boolean matchId = tree.has("idBase") && tree.get("idBase").asText().contains(newValue);


            // Retourne `true` si au moins un champ correspond
            return matchNom || matchGenre || matchEspece || matchLieu || matchId || matchDeveloppement || matchRemarquable;
        };
    }


    @FXML
    public void initialize() {
        logo.setImage(new Image("file:src/main/resources/App/AssociationMember/logo.png"));
        treeList = FXCollections.observableArrayList();
        arbre = JsonManager.getArrayNode("Arbres_JSON.json");
        arbre.forEach(treeList::add);

        filteredList = new FilteredList<>(treeList, _ -> true);

        rechercheListener = (ObservableValue<? extends String> _, String _, String newValue)-> {
            filteredList.setPredicate(defaultFilter(newValue));
            updateListView();
        };

        recherche.textProperty().addListener(rechercheListener);


        updateListView();

    }

    public void updateListView(){
        treeListView.getItems().clear();
        filteredList.forEach(tree -> {
            String id=tree.get("idBase").asText();
            String nom=tree.get("libelle_france").asText();
            String affichage = "🌳 ID " + id + " - Nom " + nom;
            treeListView.getItems().add(affichage);
        });
    }

    public void reset(){
        recherche.textProperty().removeListener(rechercheListener);
        filteredList.setPredicate(_ -> true);
        updateListView();

    }

    public void updateMenu(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationMember/Menu.fxml"));
        try {
            VBox box = loader.load();
            MenuController controller = loader.getController();
            controller.setUser(user);
            JFXDrawer.setSidePane(box);

            HamburgerBackArrowBasicTransition burgerTask2 = new HamburgerBackArrowBasicTransition(JFXHamburger);
            burgerTask2.setRate(-1);
            JFXHamburger.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, (_) -> {
                System.out.println("Hamburger clicked");
                burgerTask2.setRate(burgerTask2.getRate() * -1);
                burgerTask2.play();
                if (JFXDrawer.isOpened()) {
                    JFXDrawer.close();
                    // ✅ Attendre 300ms avant de masquer vboxMenu
                    PauseTransition pause = new PauseTransition(Duration.millis(300));
                    pause.setOnFinished(event -> vboxMenu.setVisible(false));
                    pause.play();


                } else {
                    JFXDrawer.open();
                    vboxMenu.setVisible(true);
                }
            });
        } catch (Exception e) {
            System.out.println("Error de l'initialisation: " + e.getMessage());
        }


    }

    public void handleCellClick(){
        treeListView.setOnMouseClicked((event) -> {
            if(event.getClickCount() == 2 && !treeListView.getSelectionModel().isEmpty()){
                String selected = treeListView.getSelectionModel().getSelectedItem();
                String id = selected.split("ID ")[1].split("- Nom ")[0];
                int idInt = Integer.parseInt(id.trim());
                Optional<JsonNode> arbreOption=JsonManager.getNode("Arbres_JSON.json", Map.entry("idBase", idInt));
                if(arbreOption.isPresent()){
                    JsonNode arbre = arbreOption.get();
                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationMember/DetailsPage.fxml"));
                    try {
                        VBox detailsPage = loader.load();
                        DetailsController controller = loader.getController();
                        controller.setUser(user, arbre);
                        stage.setTitle("Details");
                        stage.setScene(new Scene(detailsPage));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    public void handeMoreButton(){
        moreButton.setOnAction(event -> {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationMember/FiltrePage.fxml"));
            try {
                updateListView();
                VBox ajoutPage = loader.load();
                FiltreController controller = loader.getController();

                reset();

                controller.setup(treeList,treeListView,recherche,rechercheListener);
                Scene scene = new Scene(ajoutPage);
                scene.getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/App/AssociationMember/FiltrePage.css")).toExternalForm()
                );
                stage.setTitle("Paramètre de recherche et filtre");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
