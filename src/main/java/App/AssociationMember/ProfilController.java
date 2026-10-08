package App.AssociationMember;

import com.fasterxml.jackson.databind.JsonNode;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import others.ResourceHandler;

import java.util.Optional;

public class ProfilController {

    @FXML
    private JFXDrawer JFXDrawer;

    @FXML
    private JFXHamburger JFXHamburger;

    @FXML
    private Label dateDeNaissance;

    @FXML
    private Label dateInscription;

    @FXML
    private Label adresse;

    @FXML
    private Label nom;

    @FXML
    private Label prenom;

    @FXML
    private VBox vboxMenu;

    @FXML
    private HBox topHbox;

    @FXML
    private ImageView logo;

    JsonNode user;


    public void setUser(JsonNode user){
        this.user=user;
        System.out.println(user.get("nom").asText());
        updateProfil();
        updateMenu();
    }

    public void updateProfil(){
        String name=user.get("nom").asText();
        String firstname=user.get("prenom").asText();
        String bithday=user.get("dateNaissance").asText();
        String inscription=user.get("dateInscription").asText();
        String address=user.get("adresse").asText();

        prenom.setText(firstname);
        nom.setText(name);
        dateDeNaissance.setText(bithday);
        dateInscription.setText(inscription);
        adresse.setText(address);

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


    @FXML
    public void  initialize() {
        System.out.println("HomePageController initialized");

    }

}
