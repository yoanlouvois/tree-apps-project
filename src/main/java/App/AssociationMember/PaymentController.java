package App.AssociationMember;

import Data.JsonManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import others.Message;
import others.Notification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class PaymentController {

    @FXML
    private Label totalAmountLabel;

    @FXML
    private TextField nameField, cardNumberField, expiryDateField, cvvField;

    @FXML
    private Button payer;

    private JsonNode user;
    private JsonNode association;

    public void initialize() {

        cardNumberField.setPromptText("XXXX XXXX XXXX XXXX");
        cvvField.setPromptText("CVV");
        expiryDateField.setPromptText("MM/YY");
        nameField.setPromptText("Nom sur la carte");

        // Application des TextFormatters
        cardNumberField.setTextFormatter(new TextFormatter<>(formatCardNumber()));
        cvvField.setTextFormatter(new TextFormatter<>(formatCVV()));
        expiryDateField.setTextFormatter(new TextFormatter<>(formatExpiryDate()));
        nameField.setTextFormatter(new TextFormatter<>(formatName()));
    }

    public void set(double amount, JsonNode user,JsonNode association) {
        this.user = user;
        this.association = association;
        totalAmountLabel.setText("€" + amount);

        payer.setOnAction(event -> handlePayment());
    }

    private boolean validateFields() {
        if (nameField.getText().isEmpty() ||
            cardNumberField.getText().isEmpty() ||
            expiryDateField.getText().isEmpty() ||
            cvvField.getText().isEmpty()) {
            return false;
        }

        // Validate card number (assuming it should be 16 digits)
        if (!cardNumberField.getText().matches("\\d{16}")) {
            return false;
        }

        // Validate expiry date (assuming format MM/YY)
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            formatter.parse(expiryDateField.getText());
        } catch (DateTimeParseException e) {
            return false;
        }

        // Validate CVV (assuming it should be 3 digits)
        if (!cvvField.getText().matches("\\d{3}")) {
            return false;
        }

        return true;
    }



    private void handlePayment() {
        if (validateFields()) {
            Message.showInformation("Paiement", "Paiement accepté ✅");

            ArrayNode cotisations = (ArrayNode) user.get("cotisationsPayees");
            System.out.println(cotisations);
            double budget = association.get("budget").asDouble();
            cotisations.add(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            payer.setDisable(true);

            Notification notification= new Notification(user.get("nom").asText()+" "+user.get("prenom").asText()+" "+"à payé sa cotisation");


            //UpdateDataBase
            JsonManager.updateJsonObject("Members_JSON.json",Map.entry("identifiant",user.get("identifiant")),Map.entry("cotisationsPayees",cotisations));
            JsonManager.updateJsonObject("Members_JSON.json",Map.entry("identifiant",user.get("identifiant")),Map.entry("cotisationPayee",true));
            JsonManager.updateJsonObject("Association_JSON.json",Map.entry("nom",association.get("nom")),Map.entry("budget",budget+50.0));
            JsonManager.insertInJson("MemberAppNotif.json", List.of(notification));



        } else {
            Message.showAlert("Validation Error", "Please fill in all fields correctly.");
        }
    }

    // 🔹 Formatage du numéro de carte (16 chiffres, espaces tous les 4 chiffres)
    private static UnaryOperator<TextFormatter.Change> formatCardNumber() {
        return change -> {

            if (!change.getControlNewText().matches("\\d{0,16}")) {
                return null; // Bloque la saisie si non numérique ou trop long
            }
            return change;
        };
    }

    // 🔹 Formatage du CVV (3 chiffres max)
    private static UnaryOperator<TextFormatter.Change> formatCVV() {
        return change -> {
            if (!change.getControlNewText().matches("\\d{0,3}")) {
                return null; // Bloque la saisie au-delà de 3 chiffres
            }
            return change;
        };
    }

    // 🔹 Formatage de la date d'expiration (MM/YY)
    private static UnaryOperator<TextFormatter.Change> formatExpiryDate() {
        return change -> {

            // Vérification du format correct
            if (!change.getControlNewText().matches("\\d{0,2}(/\\d{0,2})?")) {
                return null; // Bloque les saisies incorrectes
            }

            return change;
        };
    }

    // 🔹 Formatage du nom (lettres et espaces uniquement)
    private static UnaryOperator<TextFormatter.Change> formatName() {
        return change -> {
            if (!change.getControlNewText().matches("[a-zA-Z ]*")) {
                return null; // Bloque la saisie de caractères invalides
            }
            return change;
        };
    }


}
