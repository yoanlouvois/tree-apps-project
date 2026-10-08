package others;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Contribution {
    private static final AtomicInteger compteur = new AtomicInteger(1);

    @JsonProperty("id")
    protected final int id;

    @JsonProperty("montant")
    protected double montant;

    @JsonProperty("natureDonateur")
    protected String natureDonateur;

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    protected LocalDate date;

    public Contribution(double montant, String natureDonateur) {
        this.id = compteur.getAndIncrement();
        this.montant = montant;
        this.natureDonateur = natureDonateur;
        this.date = LocalDate.now();
    }

    public int getId() { return id; }
    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public String getNatureDonateur() { return natureDonateur; }
    public void setNatureDonateur(String natureDonateur) { this.natureDonateur = natureDonateur; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    @Override
    public String toString() {
        return "Contribution{id=" + id + ", montant=" + montant + ", natureDonateur='" + natureDonateur + '\'' +
                ", date=" + date + '}';
    }
}
