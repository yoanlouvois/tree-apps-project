package others;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class Personne {


    private String nom;
    private String prenom;
    private int age;
    private String adresse;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate dateNaissance;



    public Personne(String nom, String prenom, int age, LocalDate dateNaissance,String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.dateNaissance = dateNaissance;
        this.adresse=adresse;

    }

    public Personne(){}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getAdresse() {
        return adresse;
    }

    public int getAge() {
        return age;
    }
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

}
