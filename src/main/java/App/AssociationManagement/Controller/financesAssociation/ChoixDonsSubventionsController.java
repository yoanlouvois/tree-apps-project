package App.AssociationManagement.Controller.financesAssociation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChoixDonsSubventionsController {
    @FXML
    protected void onButtonAnnulerClick(ActionEvent event) {
        try {
            // Charger la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/DonsSubventions.fxml"));
            Parent financesView = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(new Scene(financesView, 600,400));
            //stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/App/AssociationManagement/styles.css")).toExternalForm());
            stage.setTitle("Gestion des dons et des subventions");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onButtonDonClick(ActionEvent event) {
        try {
            // Charger la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/DemandeDon.fxml"));
            Parent financesView = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(new Scene(financesView, 600,400));
            //stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/App/AssociationManagement/styles.css")).toExternalForm());
            stage.setTitle("Demander un don");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onButtonSubventionClick(ActionEvent event) {
        try {
            // Charger la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/DemandeSubvention.fxml"));
            Parent financesView = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(new Scene(financesView, 600,400));
            //stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/App/AssociationManagement/styles.css")).toExternalForm());
            stage.setTitle("Demander une subvention");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
