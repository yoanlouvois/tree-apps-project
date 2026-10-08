package App.AssociationMember;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import others.Message;
import others.ResourceHandler;

import java.io.IOException;
import java.util.Optional;

public class CotisationPageController {

    @FXML
    private JFXDrawer JFXDrawer;

    @FXML
    private JFXHamburger JFXHamburger;

    @FXML
    private ListView<String> listViewHisto;

    @FXML
    private JFXButton paiementCotisation;

    @FXML
    private HBox topHbox;

    @FXML
    private ImageView logo;

    @FXML
    private VBox vboxMenu;

    private JsonNode user;

    public void setUser(JsonNode user){
        this.user=user;
        updateMenu();
        handleButtonAction();
        updateListView();
    }

    public void updateMenu(){
        logo.setImage(new Image("file:src/main/resources/App/AssociationMember/logo.png"));

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
                    PauseTransition pause = new PauseTransition(Duration.millis(500));
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

    public void handleButtonAction(){
        paiementCotisation.setOnAction((_) -> {
            try {
                if(user.get("cotisationPayee").asBoolean()){
                    Message.showInformation("Cotisation déjà payée", "Vous avez déjà payé votre cotisation pour cette année");
                    return;
                }

                Optional<JsonNode> rootNode=JsonManager.getRootNode("Association_JSON.json");
                if(rootNode.isPresent()){
                    ArrayNode associations=(ArrayNode) rootNode.get();
                    JsonNode arreAssoc=associations.get(0);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationMember/PaymentPage.fxml"));
                    GridPane paiementCotisation = loader.load();
                    PaymentController controller = loader.getController();
                    controller.set(50.00, user, arreAssoc);
                    Stage stage = new Stage();
                    stage.setTitle("Paiement Cotisation");
                    stage.setScene(new Scene(paiementCotisation, 700, 400));
                    stage.show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateListView(){
        JsonNode cotisation=user.get("cotisationsPayees");
        System.out.println(cotisation);
        for(JsonNode c:cotisation){
            String cotisationText=c.asText();
            listViewHisto.getItems().add(cotisationText);
        }

    }


    @FXML
    public void  initialize() {
        System.out.println("HomePageController initialized");

    }

}
