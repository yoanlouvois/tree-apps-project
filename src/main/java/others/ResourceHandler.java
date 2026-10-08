package others;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceHandler {
    private final Path directoryPath;

    // ✅ Constructeur qui initialise le chemin vers le répertoire
    public ResourceHandler(String baseUrl) {
        this.directoryPath = Path.of(baseUrl);

        // Vérifie si le répertoire existe, sinon affiche un avertissement
        if (!Files.exists(directoryPath) || !Files.isDirectory(directoryPath)) {
            System.out.println("⚠️ Le répertoire spécifié n'existe pas : " + baseUrl);
        }
    }
    // ✅ Méthode pour récupérer un fichier spécifique en URL
    public URL getFileUrl(String fileName) {
        Path filePath = directoryPath.resolve(fileName);
        if (Files.exists(filePath)) {
            try {
                return filePath.toUri().toURL();
            } catch (MalformedURLException e) {
                System.out.println("❌ Erreur lors de la récupération de l'URL du fichier : " + e.getMessage());
            }
        } else {
            System.out.println("❌ Fichier introuvable : " + fileName);
        }
        return null;
    }

    public Optional<FXMLLoader> getFXMLLoader(String fileName){
        URL fileUrl = getFileUrl(fileName);
        if (fileUrl != null) {
            FXMLLoader loader = new FXMLLoader(fileUrl);
            return Optional.of(loader);
        }
        return Optional.empty();
    }

    // ✅ Test de la classe

}
