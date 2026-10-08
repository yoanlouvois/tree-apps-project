package App.AssociationManagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class VisiteArbreController {

    @FXML
    protected void onButtonRetourClick(ActionEvent event){
        try {
            // Charger la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/PageAccueil/PageAccueil.fxml"));
            Parent arbreView = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(new Scene(arbreView,600,400));
            //stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/App/AssociationManagement/styles.css")).toExternalForm());
            stage.setTitle("Gestion de l'association");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

