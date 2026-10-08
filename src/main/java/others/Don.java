package others;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Don extends Contribution {

    public Don(double montant, String natureDonateur) {
        super(montant, natureDonateur);
    }

    @Override
    public String toString() {
        return "Don" + super.toString();
    }
}
