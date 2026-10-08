package App.AssociationManagement;
import java.util.*;
import Data.JsonManager;
import others.Don;
import others.Facture;
import others.Tree;

public class Association {
    private final String nom;
    private final int budget;
    private JsonManager jsonManager = JsonManager.INSTANCE;
    private List<Don> dons ;
    private List<Facture> factures ;

    private Map<Tree, Integer> votes = new HashMap<>();
    private static final int MAX_TREES_TO_SUBMIT = 5;

    // Constructeur
    public Association(String nom,int budget, List<Don> dons, List<Facture> factures) {
        this.nom = nom;
        this.budget = budget;
        this.dons = dons;
        this.factures = factures;
    }

    public Association(){
        this.nom = "Association";
        this.budget = 0;
        this.dons = new ArrayList<>();
        this.factures= new ArrayList<>();
    }

    // Getters et Setters
    public String getNom() { return nom; }
    public int getBudget() { return budget; }
    public List<Don> getDons() { return dons; }
    public List<Facture> getFactures() { return factures; }

}

