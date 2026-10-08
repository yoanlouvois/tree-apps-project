package App.AssociationMember;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import others.Message;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class PlanificationController {

    @FXML
    private JFXDrawer JFXDrawer;

    @FXML
    private JFXHamburger JFXHamburger;

    @FXML
    private ListView<String> listViewHoldVisites;

    @FXML
    private ListView<String> listViewNewVistes;

    @FXML
    private HBox topHbox;

    @FXML
    private ImageView logo;

    @FXML
    private VBox vboxMenu;

    private JsonNode user;
    private JsonNode visites;

    public void setUser(JsonNode user,JsonNode rootVisites){
        this.user=user;
        this.visites=rootVisites;
        updateMenu();
        updateListviewHoldVisites();
        updateListviewNewVisites();
        handleReservation();
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

    public void updateListviewHoldVisites(){
        JsonNode reservations = user.get("visites");

        for(JsonNode reservation:reservations){
            String date=reservation.get("date").asText();
            JsonNode arbre=reservation.get("tree");
            String nom=arbre.get("libelle_france").asText();
            String lieu=arbre.get("lieu").asText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            LocalDate localDate = LocalDate.parse(date, formatter);

            if(localDate.isBefore(LocalDate.now())){
                listViewHoldVisites.getItems().add("🌳 "+nom+" - "+lieu+" - "+date);
            }

        }

    }

    public void updateListviewNewVisites(){
        for(JsonNode visite:visites){
            String date=visite.get("date").asText();
            String id=visite.get("id").asText();
            JsonNode arbre=visite.get("tree");
            String nom=arbre.get("libelle_france").asText();
            String lieu=arbre.get("lieu").asText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            LocalDate localDate = LocalDate.parse(date, formatter);
            JsonNode nullNode=JsonManager.objectMapper.nullNode();
            if(localDate.isAfter(LocalDate.now())&& visite.get("member")==nullNode){
                listViewNewVistes.getItems().add("🌳 "+nom+" - "+lieu+" - "+date+" - "+id);
            }

        }

    }

    public void handleReservation(){

        listViewNewVistes.setOnMouseClicked((MouseEvent event)->{
            if(event.getClickCount()==2 && !listViewNewVistes.getSelectionModel().isEmpty()){
                String selectedItem = listViewNewVistes.getSelectionModel().getSelectedItem();
                String[] parts = selectedItem.split(" - ");
                int id=Integer.parseInt(parts[3]);
                Optional<JsonNode> visiteNode=JsonManager.getNode("Visites_JSON.json",Map.entry("id",id));
                ArrayNode reservations = (ArrayNode) user.get("visites");
                boolean canReserve=true;
                for(JsonNode reservation:reservations){
                    if(reservation.get("id").asInt()==id){
                        Message.showInformation("Erreur", "Vous avez déjà réservé cette visite");
                        canReserve=false;
                        break;
                    }
                }
                if(canReserve) {

                    if (visiteNode.isPresent()) {
                        Optional<ButtonType> result = Message.showConfirmation("Réservation visite", "Voulez-vous vraiment réserver cette visite?").showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            ArrayNode array = ((ArrayNode) user.get("visites")).add(visiteNode.get());
                            String key = user.get("identifiant").asText();
                            JsonManager.updateJsonObject("Members_JSON.json", Map.entry("identifiant", key), Map.entry("visites", array));
                            JsonManager.updateJsonObject("Visites_JSON.json", Map.entry("id", id), Map.entry("member", user));
                        }

                    }
                }

            }
        });
    }



    @FXML
    public void  initialize() {
        System.out.println("HomePageController initialized");
    }

}
