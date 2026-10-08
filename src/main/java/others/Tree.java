package others;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;

import java.util.Optional;



public class Tree {
    private final int idBase;
    private final String genre;
    private final String espece;
    private final String libelle_france;
    private final int circonference; // en cm
    private final double hauteur; // en mètres
    private final String stade_de_developpement;
    private final String lieu;
    private final double latitude;
    private final double longitude;
    private  String remarquable;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private final LocalDate dateClassification; // Peut être vide si inconnu

    // ✅ Constructeur
    public Tree(int idBase,String genre, String espece, String nomCommun, int circonference, double hauteur,
                String stadeDeveloppement, String adresse, double latitude, double longitude,
                String remarquable) {
        this.genre = genre;
        this.espece = espece;
        this.libelle_france = nomCommun;
        this.circonference = circonference;
        this.hauteur = hauteur;
        this.stade_de_developpement = stadeDeveloppement;
        this.lieu= adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.remarquable = remarquable;
        this.dateClassification = null;
        this.idBase = idBase;
    }

    public Tree(Tree copie){
        this.genre = copie.genre;
        this.espece = copie.espece;
        this.libelle_france = copie.libelle_france;
        this.circonference = copie.circonference;
        this.hauteur = copie.hauteur;
        this.stade_de_developpement = copie.stade_de_developpement;
        this.lieu = copie.lieu;
        this.latitude = copie.latitude;
        this.longitude = copie.longitude;
        this.remarquable = copie.remarquable;
        this.dateClassification = copie.dateClassification;
        this.idBase = copie.idBase;
    }

    public Tree(){
        this.genre = "";
        this.espece = "";
        this.libelle_france = "";
        this.circonference = 0;
        this.hauteur = 0;
        this.stade_de_developpement = "";
        this.lieu = "";
        this.latitude = 0;
        this.longitude = 0;
        this.remarquable = "NON";
        this.dateClassification = null;
        this.idBase = 0;
    }


    public static String arbreNodeToString(JsonNode arbre) {
        if (arbre == null || arbre.isEmpty()) {
            return "Aucune information disponible sur l'arbre.";
        }

        StringBuilder description = new StringBuilder();

        description.append("🌳 **Informations sur l'Arbre** 🌳\n");
        description.append("📌 **ID:** ").append(arbre.has("idBase") ? arbre.get("idBase").asInt() : "Non spécifié").append("\n");
        description.append("🌱 **Genre:** ").append(arbre.has("genre") ? arbre.get("genre").asText() : "Inconnu").append("\n");
        description.append("🌿 **Espèce:** ").append(arbre.has("espece") ? arbre.get("espece").asText() : "Inconnue").append("\n");
        description.append("📛 **Nom commun:** ").append(arbre.has("libelle_france") ? arbre.get("libelle_france").asText() : "Non défini").append("\n");
        description.append("📏 **Circonférence:** ").append(arbre.has("circonference") ? arbre.get("circonference").asInt() + " cm" : "Non mesurée").append("\n");
        description.append("📏 **Hauteur:** ").append(arbre.has("hauteur") ? arbre.get("hauteur").asDouble() + " m" : "Non mesurée").append("\n");
        description.append("🌲 **Stade de développement:** ").append(arbre.has("stade_de_developpement") ? arbre.get("stade_de_developpement").asText() : "Non renseigné").append("\n");
        description.append("📍 **Lieu:** ").append(arbre.has("lieu") ? arbre.get("lieu").asText() : "Non précisé").append("\n");
        description.append("🌎 **Coordonnées:** ");

        if (arbre.has("latitude") && arbre.has("longitude")) {
            description.append(arbre.get("latitude").asDouble())
                    .append(", ")
                    .append(arbre.get("longitude").asDouble())
                    .append("\n");
        } else {
            description.append("Non disponibles\n");
        }

        description.append("⭐ **Remarquable:** ").append(arbre.has("remarquable") ? arbre.get("remarquable").asText() : "Non défini").append("\n");

        if (arbre.has("dateClassification") && !arbre.get("dateClassification").isNull()) {
            description.append("📅 **Date de classification:** ").append(arbre.get("dateClassification").asText()).append("\n");
        } else {
            description.append("📅 **Date de classification:** Non disponible\n");
        }

        return description.toString();
    }


    // ✅ Getters
    public String getGenre() { return genre; }
    public String getEspece() { return espece; }
    public String getLibelle_france() { return libelle_france; }
    public int getCirconference() { return circonference; }
    public double getHauteur() { return hauteur; }
    public String getStade_de_developpement() { return stade_de_developpement; }
    public String getLieu() { return lieu; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getRemarquable() { return remarquable; }
    public LocalDate getDateClassification() { return dateClassification; }
    public int getIdBase() { return idBase; }


    public void setRemarquable(String remarquable) {
        this.remarquable = remarquable;
    }
}
