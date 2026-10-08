package App.GreenSpaceService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class TreeManagerController {

    @FXML
    void OnButtonReturnClickArbre(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/GreenServiceSpace/accueil-view.fxml"));
            Parent secondView = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(secondView, 800, 600));
            stage.getScene().getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/App/GreenServiceSpace/styles.css")).toExternalForm()
            );
            stage.setTitle("Gestion des espaces verts");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnButtonThreeListClick(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/GreenServiceSpace/three-list-view.fxml"));
            Parent secondView = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(secondView, 800, 600));
            stage.setTitle("Liste des arbres");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnButtonNewTreeClick(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/GreenServiceSpace/tree-plantation-view.fxml"));
            Parent secondView = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(secondView, 800, 600));
            /*stage.getScene().getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/App/GreenServiceSpace/styles.css")).toExternalForm()
            );*/
            stage.setTitle("Enregistrement d'un nouvel arbre");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
