package App.AssociationMember;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.Objects;
import java.util.Optional;


public class LoginScreenController {


    @FXML
    private TextField identifiantLabel;

    @FXML
    private ImageView logo;

    @FXML
    private PasswordField passwordLabel;


    @FXML
    void handleLoginAction() {
        Optional<JsonNode> user=Member.login("johnDoe","password123");

        if(user.isPresent()){
            SessionManager s=SessionManager.INSTANCE;
            s.setUserData(user.get());
            System.out.println(s.getUserData().toString());
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationMember/HomePage.fxml"));
                StackPane pane = loader.load();
                HomePageController controller = loader.getController();
                controller.setUser(user.get());
                Scene scene=identifiantLabel.getScene();

                scene.getStylesheets().clear();
                scene.getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/App/AssociationMember/HomePage.css")).toExternalForm()
                );
                scene.setRoot(pane);

            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
            }

        }

    }

    @FXML
    public void initialize(){
        logo.setImage(new Image("file:src/main/resources/App/AssociationMember/logo.png"));

    }



}
