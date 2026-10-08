package App.AssociationManagement.Controller.financesAssociation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DepensesController {

    @FXML
    protected void onButtonRetourClick(ActionEvent event) {
        try {
            // Charger la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/FinancesAssociation.fxml"));
            Parent financesView = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(new Scene(financesView, 600,400));
            //stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/App/AssociationManagement/styles.css")).toExternalForm());
            stage.setTitle("Finances de l'association");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onButtonDefraiementVisitesClick(ActionEvent event){
        try {
            // Charger la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/DefraiementVisites.fxml"));
            Parent financesView = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(new Scene(financesView, 600,400));
            //stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/App/AssociationManagement/styles.css")).toExternalForm());
            stage.setTitle("Défraiement des visites des clients");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onButtonPaiementFacturesClick(ActionEvent event){
        try {
            // Charger la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/AssociationManagement/financesAssociation/PaiementFactures.fxml"));
            Parent financesView = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(new Scene(financesView, 600,400));
            //stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/App/AssociationManagement/styles.css")).toExternalForm());
            stage.setTitle("Paiement des factures");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

