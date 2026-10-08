package App.AssociationManagement.Controller.financesAssociation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FinancesAssociationController {

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

    @FXML
    protected void onButtonDonsSubventionsClick(ActionEvent event){
        try {
            // Charger la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/DonsSubventions.fxml"));
            Parent arbreView = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(new Scene(arbreView,600,400));
            //stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/App/AssociationManagement/styles.css")).toExternalForm());
            stage.setTitle("Gestion des dons et des subventions de l'association");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onButtonCotisationsClick(ActionEvent event){
        try {
            // Charger la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/Cotisations.fxml"));
            Parent arbreView = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(new Scene(arbreView,600,400));
            //stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/App/AssociationManagement/styles.css")).toExternalForm());
            stage.setTitle("Gestion des cotisations de l'association");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onButtonDepensesClick(ActionEvent event){
        try {
            // Charger la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/Depenses.fxml"));
            Parent arbreView = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(new Scene(arbreView,600,400));
            //stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/App/AssociationManagement/styles.css")).toExternalForm());
            stage.setTitle("Gestion des dépenses de l'association");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
