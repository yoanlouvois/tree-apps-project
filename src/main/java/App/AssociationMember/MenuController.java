package App.AssociationMember;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import others.ResourceHandler;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;

public class MenuController {

    @FXML
    private JFXButton arbreListeButton;

    @FXML
    private JFXButton cotisationButton;

    @FXML
    private JFXButton votesButton;

    @FXML
    private JFXButton home;

    @FXML
    private Label userLabel;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private JFXButton planificationButton;

    @FXML
    private JFXButton profilButton;

    private JsonNode user;

    public void setUser(JsonNode user){
        this.user=user;
        handleButtonClick();
    }
@FXML
public void initialize() {


}
    void handleButtonClick() {
        userLabel.setText(user.get("identifiant").asText());
        profilButton.setOnAction((ActionEvent _) -> {
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/App/AssociationMember/ProfilPage.fxml"));
            try {
                StackPane pane = loader.load();
                ProfilController controller = loader.getController();
                controller.setUser(user);
                Scene scene = profilButton.getScene();
                scene.getStylesheets().clear();
                scene.getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/App/AssociationMember/ProfilPage.css")).toExternalForm()
                );
                profilButton.getScene().setRoot(pane);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        
        });
        arbreListeButton.setOnAction((ActionEvent event) -> {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationMember/ArbreListePage.fxml"));
            URL fxmlPath = getClass().getResource("/App/AssociationMember/ArbreListePage.fxml");
            if(fxmlPath==null){
                System.out.println("fxmlPath est null");
            }
            else {
                System.out.println("fxmlPath est non null");
            }
            
            try {
                StackPane pane = loader.load();
                ArbreListeController controller = loader.getController();
                controller.setUser(user);
                Scene scene = arbreListeButton.getScene();
                scene.getStylesheets().clear();
                scene.getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/App/AssociationMember/ArbreListePage.css")).toExternalForm()
                );
                arbreListeButton.getScene().setRoot(pane);
            } catch (Exception e) {
                System.out.println("impossible de charger la page arbreListePage");
                e.printStackTrace(); // ✅ Affiche l'erreur complète pour identifier le problème
            }
            
        });
        cotisationButton.setOnAction((ActionEvent event) -> {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationMember/CotisationPage.fxml"));

            
            try {

                StackPane pane = loader.load();
                CotisationPageController controller = loader.getController();
                controller.setUser(user);
                Scene scene = cotisationButton.getScene();
                scene.getStylesheets().clear();
                scene.getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/App/AssociationMember/CotisationPage.css")).toExternalForm()
                );
                cotisationButton.getScene().setRoot(pane);
            } catch (Exception e) {
                System.out.println(e.getMessage());                }
            
        });
        planificationButton.setOnAction((ActionEvent event) -> {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationMember/PlanificationPage.fxml"));
         
            try {
                Optional<JsonNode> rootNodeOption = JsonManager.getRootNode("Visites_JSON.json");
                StackPane pane = loader.load();
                PlanificationController controller = loader.getController();
                controller.setUser(user,rootNodeOption.get());
                Scene scene = planificationButton.getScene();
                scene.getStylesheets().clear();
                scene.getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/App/AssociationMember/PlanificationPage.css")).toExternalForm()
                );
                planificationButton.getScene().setRoot(pane);
            } catch (Exception e) {
                System.out.println(e.getMessage());                }
            
        });

        votesButton.setOnAction((ActionEvent event) -> {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationMember/VotesPage.fxml"));

            try {
                StackPane pane = loader.load();
                VotesController controller = loader.getController();
                controller.setUser(user);
                Scene scene = votesButton.getScene();
                scene.getStylesheets().clear();
                scene.getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/App/AssociationMember/VotesPage.css")).toExternalForm()
                );
                votesButton.getScene().setRoot(pane);
            } catch (Exception e) {
                System.out.println(e.getMessage());                }

        });
        logoutButton.setOnAction((ActionEvent event) -> {
             System.out.println("Logout Button clicked");
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationMember/LoginScreen.fxml"));
        
            try {
                Scene scene = logoutButton.getScene();
                scene.getStylesheets().clear();
                scene.getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/App/AssociationMember/LoginScreen.css")).toExternalForm()
                );
                VBox pane = loader.load();
                this.user=null;
                logoutButton.getScene().setRoot(pane);
            } catch (Exception e) {
                System.out.println(e.getMessage());                }
           

        });
        home.setOnAction((ActionEvent event) -> {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationMember/HomePage.fxml"));
           
            try {
                StackPane pane = loader.load();
                HomePageController controller = loader.getController();
                controller.setUser(user);
                Scene scene = home.getScene();
                scene.getStylesheets().clear();
                scene.getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/App/AssociationMember/HomePage.css")).toExternalForm()
                );
                home.getScene().setRoot(pane);
            } catch (Exception e) {
                System.out.println(e.getMessage());                }
            
        });

    }



}
