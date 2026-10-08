package App.AssociationManagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class GenericController {

    public void onMenu2Click(ActionEvent event) {
        try {
            // Charger la page NotificationAccueil.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/NotificationManagement/NotificationAccueil.fxml"));
            Parent notificationView = loader.load();

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();

            // Définir la scène
            stage.setScene(new Scene(notificationView, 697, 471));
            stage.setTitle("Notifications");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}