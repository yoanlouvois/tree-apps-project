package App.GreenSpaceService;

public class Tree {
    private String id;
    private String nom;
    private String genre;
    private String espece;
    private String lieu;
    private String remarquable;
    private String latitude;
    private String longitude;
    private String circonference;
    private String hauteur;
    private String developpementStage;

    // Constructeur
    public Tree(String id, String nom, String genre, String espece, String lieu, String remarquable
    , String lattitude, String longitude, String circonference, String hauteur, String developpementStage) {
        this.id = id;
        this.nom = nom;
        this.genre = genre;
        this.espece = espece;
        this.lieu = lieu;
        this.remarquable = remarquable;
        this.latitude = lattitude;
        this.longitude = longitude;
        this.circonference = circonference;
        this.hauteur = hauteur;
        this.developpementStage = developpementStage;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getEspece() {
        return espece;
    }

    public void setEspece(String espece) {
        this.espece = espece;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getRemarquable() {
        return remarquable;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCirconference() {
        return circonference;
    }
    public String getHauteur() {
        return hauteur;
    }
    public String getDeveloppementStage() {
        return developpementStage;
    }

    public void setRemarquable(String remarquable) {
        this.remarquable = remarquable;
    }
}