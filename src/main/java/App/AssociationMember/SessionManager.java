package App.AssociationMember;

import com.fasterxml.jackson.databind.JsonNode;

public enum SessionManager {
    INSTANCE;
    private JsonNode userData=null; // Stocke les données de l'utilisateur connecté

    // ✅ Stocker les données de l'utilisateur après connexion
    public void setUserData(JsonNode userData) {
        this.userData = userData;
    }

    // ✅ Récupérer les données de l'utilisateur
    public JsonNode getUserData() {
        return userData;
    }
}

