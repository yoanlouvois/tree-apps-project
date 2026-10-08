module TreeApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.opencsv;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires net.datafaker;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.jfoenix;
    requires java.desktop;

    exports Demo;
    exports others;
    exports App.AssociationManagement;
    exports App.AssociationMember ;
    exports App.GreenSpaceService;
    exports App.AssociationManagement.Controller;




    opens Demo to javafx.fxml;
    opens others to com.fasterxml.jackson.databind; // Autorise Jackson à accéder aux champs privés
    opens App.AssociationMember to javafx.fxml,com.fasterxml.jackson.databind,com.jfoenix;
    opens App.AssociationManagement to com.fasterxml.jackson.databind,com.jfoenix;
    opens App.GreenSpaceService to javafx.fxml,com.fasterxml.jackson.databind,com.jfoenix;
    opens App.AssociationManagement.Controller to javafx.fxml, com.fasterxml.jackson.databind, com.jfoenix;
    exports App.AssociationManagement.Controller.classificationArbres;
    opens App.AssociationManagement.Controller.classificationArbres to javafx.fxml, com.fasterxml.jackson.databind, com.jfoenix;
    exports App.AssociationManagement.Controller.financesAssociation;
    opens App.AssociationManagement.Controller.financesAssociation to javafx.fxml, com.fasterxml.jackson.databind, com.jfoenix;
    exports App.AssociationManagement.Controller.finExerciceBudgetaire;
    opens App.AssociationManagement.Controller.finExerciceBudgetaire to com.fasterxml.jackson.databind, com.jfoenix, javafx.fxml;
    exports App.AssociationManagement.Controller.gestionMembres;
    opens App.AssociationManagement.Controller.gestionMembres to com.fasterxml.jackson.databind, com.jfoenix, javafx.fxml;
    exports App.AssociationManagement.Controller.gestionVisiteArbre;
    opens App.AssociationManagement.Controller.gestionVisiteArbre to javafx.fxml, com.fasterxml.jackson.databind, com.jfoenix;
    opens App.AssociationManagement.Controller.GestionDonation to javafx.fxml;
    exports App.AssociationManagement.Controller.Notification;
    opens App.AssociationManagement.Controller.Notification to com.fasterxml.jackson.databind, com.jfoenix, javafx.fxml;


}