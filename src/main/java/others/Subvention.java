package others;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Subvention extends Contribution {

    @JsonProperty("rapportActivite")
    private String rapportActivite;

    public Subvention(double montant, String natureDonateur, String rapportActivite) {
        super(montant, natureDonateur);
        this.rapportActivite = rapportActivite;
    }

    public String getRapportActivite() { return rapportActivite; }
    public void setRapportActivite(String rapportActivite) { this.rapportActivite = rapportActivite; }

    @Override
    public String toString() {
        return "Subvention" + super.toString() + ", rapportActivite='" + rapportActivite + "'}";
    }
}
