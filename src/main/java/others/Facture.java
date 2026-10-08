package others;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class Facture {
    private static final AtomicInteger compteur = new AtomicInteger(1);

    @JsonProperty("id") // Clé JSON personnalisée
    private final int id;

    @JsonProperty("montant")
    private double montant;

    @JsonProperty("emetteur")
    private String emetteur;

    @JsonProperty("beneficiaire")
    private String beneficiaire;

    @JsonProperty("estPayee")
    private boolean estPayee;

    @JsonProperty("dateEmission")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateEmission;

    // ✅ Constructeur
    public Facture(double montant, String emetteur, String beneficiaire, boolean estPayee) {
        this.id = compteur.getAndIncrement();
        this.montant = montant;
        this.emetteur = emetteur;
        this.beneficiaire = beneficiaire;
        this.estPayee = estPayee;
        this.dateEmission = LocalDate.now();
    }

    // ✅ Getter et Setter
    public int getId() { return id; }
    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public String getEmetteur() { return emetteur; }
    public void setEmetteur(String emetteur) { this.emetteur = emetteur; }

    public String getBeneficiaire() { return beneficiaire; }
    public void setBeneficiaire(String beneficiaire) { this.beneficiaire = beneficiaire; }

    public boolean isEstPayee() { return estPayee; }
    public void setEstPayee(boolean estPayee) { this.estPayee = estPayee; }

    public LocalDate getDateEmission() { return dateEmission; }
    public void setDateEmission(LocalDate dateEmission) { this.dateEmission = dateEmission; }

    @Override
    public String toString() {
        return "Facture{" +
                "id=" + id +
                ", montant=" + montant +
                ", emetteur='" + emetteur + '\'' +
                ", beneficiaire='" + beneficiaire + '\'' +
                ", estPayee=" + estPayee +
                ", dateEmission=" + dateEmission +
                '}';
    }
}

