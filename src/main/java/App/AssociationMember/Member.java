package App.AssociationMember;
import App.AssociationManagement.Visit;
import Data.JsonManager;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import others.Personne;
import com.fasterxml.jackson.annotation.JsonFormat;
import others.Tree;

import java.time.LocalDate;
import java.util.*;

public class Member extends Personne{

    private static final int MONTANT_COTISATION = 50; // Montant fixe de la cotisation
    private static final int MAX_NOMINATIONS = 5;

    private String identifiant;
    private String password;

    private boolean cotisationPayee;



    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") // Format JSON propre
    private List<LocalDate>cotisationsPayees;


    @JsonIgnoreProperties(value = {  "circonference", "hauteur", "stade_de_developpement", "latitude", "longitude", "remarquable","dateClassification" })
    private List<Tree>nominations;


    @JsonIgnoreProperties(value = {"compteRendu" ,"cout"})
    private List<Visit>visites;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") // Format JSON propre
    private LocalDate dateInscription;



    public Member(Personne p,String identifiant, String password,LocalDate dateInscription) {
        super(p.getNom(), p.getPrenom(), p.getAge(),p.getDateNaissance(),p.getAdresse());
        this.identifiant = identifiant;
        this.password = password;
        this.cotisationPayee = false;
        this.dateInscription=dateInscription;
        this.nominations = new LinkedList<>();
        this.visites = new ArrayList<>();
        this.cotisationsPayees = new ArrayList<>();

    }

    public Member (){
        super();
        this.nominations = new LinkedList<>();
        this.visites = new ArrayList<>();
        this.cotisationsPayees = new ArrayList<>();
    }

    public Member(String nom, String prenom, int age, LocalDate dateNaissance, String adresse) {
        super(nom, prenom, age, dateNaissance, adresse);
        this.nominations = new LinkedList<>();
        this.visites = new ArrayList<>();
        this.cotisationsPayees = new ArrayList<>();
    }

    public static Optional<JsonNode> login(String Username, String password){
        Optional<JsonNode> user=JsonManager.getNodeAllMatch("Members_JSON.json",List.of(Map.entry("identifiant",Username),Map.entry("password",password)));
        if(user.isPresent()){
            return user;

        }
        System.out.println("❌ l'identifiant ou le mot de passe est incorrect");
        return Optional.empty();
    }


    // ✅ Vérifier si la cotisation est payée
    public boolean isCotisationPayee() {
        return cotisationPayee;
    }

    public List<Tree> getNominations() { return nominations; }

    public List<LocalDate> getCotisationsPayees() { return cotisationsPayees; }

    public List<Visit> getVisites() { return visites; }


    public String getIdentifiant() {
        return identifiant;
    }
    public String getPassword() {
        return password;
    }
    public LocalDate getDateInscription() {
        return dateInscription;
    }
    public static double getMontantCotisation() { return MONTANT_COTISATION; }
}
