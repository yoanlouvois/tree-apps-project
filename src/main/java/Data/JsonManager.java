package Data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public enum JsonManager {
    INSTANCE;
    public static final String BASE_URL="src/main/resources/JSONDB";
    public static final ObjectMapper objectMapper = new ObjectMapper();

    // Initialisation de l'ObjectMapper
    static {
        objectMapper.registerModule(new JavaTimeModule()); // Support pour LocalDate
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
    }


    // ✅ Création d'un fichier JSON s'il n'existe pas
    public static synchronized void createJsonFile(String filename) {
        File file = new File(BASE_URL + "/" + filename);
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("[]"); // Initialisation avec un tableau vide
                System.out.println("✅ Fichier JSON créé : " + file.getName());
            } catch (IOException e) {
                System.out.println("❌ Erreur lors de la création du fichier : " + e.getMessage());
            }
        }
    }

    // Méthode pour supprimer un nœud d'un fichier JSON
    public boolean removeNode(String fileName, String idBase) {
        File file = new File(BASE_URL + "/" + fileName);
        if (!file.exists()) {
            System.out.println("❌ Fichier non trouvé : " + fileName);
            return false;
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            if (rootNode.isArray()) {
                ArrayNode arrayNode = (ArrayNode) rootNode;

                // Rechercher et supprimer l'objet avec l'ID donné
                for (int i = 0; i < arrayNode.size(); i++) {
                    JsonNode node = arrayNode.get(i);
                    if (node.has("idBase") && node.get("idBase").asText().equals(idBase)) {
                        arrayNode.remove(i);

                        // Écrire le JSON modifié dans le fichier
                        objectMapper.writeValue(file, arrayNode);
                        System.out.println("✅ Nœud supprimé avec succès !");
                        return true;
                    }
                }
            } else {
                System.out.println("❌ Le fichier JSON doit contenir un tableau.");
            }
        } catch (IOException e) {
            System.out.println("❌ Erreur lors de la modification du fichier JSON : " + e.getMessage());
        }
        return false;
    }

    // ✅ Ajout d'un ou plusieurs objets dans un fichier JSON
    public static synchronized void insertInJson(String fileName, List<?> newData, String keyField) {
        File file = new File(BASE_URL + "/" + fileName);
        if (!file.exists()) {
            System.out.println("❌ Fichier non trouvé : " + fileName);
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            if (!rootNode.isArray()) {
                System.out.println("❌ Erreur : Le fichier JSON doit contenir un tableau.");
                return;
            }

            ArrayNode arrayNode = (ArrayNode) rootNode;
            Set<String> existingKeys = new HashSet<>(arrayNode.findValuesAsText(keyField));

            for (Object obj : newData) {
                JsonNode newNode = objectMapper.valueToTree(obj);
                if (!newNode.has(keyField) ) {
                    System.out.println("❌ L'objet  n'a pas la clé `" + keyField + "`.");
                    continue;
                }
                else if(existingKeys.contains(newNode.get(keyField).asText())){
                    System.out.println("❌ L'objet  avec la clé `" + keyField + "` existe déjà.");
                    continue;
                }
                arrayNode.add(newNode);
                existingKeys.add(newNode.get(keyField).asText());
            }

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, arrayNode);
            System.out.println("✅ Ajout réussi dans " + fileName);
        } catch (IOException e) {
            System.out.println("❌ Erreur d'écriture JSON : " + e.getMessage());
        }
    }

    // ✅ Ajout d'un ou plusieurs objets dans un fichier JSON
    public static synchronized void insertInJson(String fileName, List<?> newData) {
        File file = new File(BASE_URL + "/" + fileName);
        if (!file.exists()) {
           System.out.println("❌ Fichier non trouvé : " + fileName);
            return;
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            if (!rootNode.isArray()) {
                System.out.println("❌ Erreur : Le fichier JSON doit contenir un tableau.");
                return;
            }

            ArrayNode arrayNode = (ArrayNode) rootNode;
            for (Object obj : newData) {
                JsonNode newNode = objectMapper.valueToTree(obj);
                System.out.println(newNode);
                arrayNode.add(newNode);
            }
            System.out.println("Contenu final du fichier JSON : " + arrayNode.toPrettyString());

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, arrayNode);
            System.out.println("✅ Ajout réussi dans " + fileName);
        }catch (IOException e) {
            System.out.println("❌ Erreur d'écriture JSON : " + e.getMessage());
        }
    }

    public static Optional<JsonNode> getRootNode(String fileName){
        File file = new File(BASE_URL + "/" + fileName);
        if (!file.exists()) {
            System.out.println("❌ Fichier non trouvé : " + fileName);
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.readTree(file));
        } catch (IOException e) {
            System.out.println("❌ Erreur de lecture JSON : " + e.getMessage());
            return Optional.empty();
        }
    }


    public static <T> Optional<T> getObjectFromJson(String fileName, Map.Entry<String,Object> entry, Class<T> clazz) {
        File file = new File(BASE_URL + "/" + fileName);
        if (!file.exists()) {
            System.out.println("❌ Fichier non trouvé : " + fileName);
            return Optional.empty();
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            if (!rootNode.isArray()) {
                System.out.println("❌ Erreur : Le fichier JSON doit contenir un tableau.");
                return Optional.empty();
            }

            for (JsonNode node : rootNode) {
                JsonNode nodeValue=objectMapper.valueToTree(entry.getValue());
                String key=entry.getKey();
                if (node.has(key) && node.get(key).equals(nodeValue)) {
                    T obj = objectMapper.treeToValue(node, clazz);
                    System.out.println("✅ Objet trouvé et converti avec succès !");
                    return Optional.of(obj);
                }
            }

            System.out.println("❌ Aucun objet trouvé avec `" + entry.getKey() + "` = `" + entry.getValue() + "`.");
        } catch (IOException e) {
            System.out.println("❌ Erreur de lecture JSON : " + e.getMessage());
        }

        return Optional.empty();
    }



    // ✅ Recherche d'objets dans un fichier JSON par cléUnique/valeur
    public static synchronized ArrayNode getNodeList(String fileName, List<Map.Entry<String,Object>>entries) {
        File file = new File(BASE_URL + "/" + fileName);
        if (!file.exists()) {
            System.out.println("❌ Fichier non trouvé : " + fileName);
            return objectMapper.createArrayNode();
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            if (!rootNode.isArray()) {
                System.out.println("❌ Erreur : Le fichier JSON doit contenir un tableau.");
                return objectMapper.createArrayNode();
            }

            ArrayNode results = objectMapper.createArrayNode();
            for (JsonNode node : rootNode) {
                for(Map.Entry<String,Object>entry:entries){
                    JsonNode nodeValue=objectMapper.valueToTree(entry.getValue());
                    String key=entry.getKey();
                    if (node.has(key) && node.get(key).equals(nodeValue)) {
                        results.add(node);
                    }
                }

            }

            return results.isEmpty() ? objectMapper.createArrayNode() : results;
        } catch (IOException e) {
            System.out.println("❌ Erreur de lecture JSON : " + e.getMessage());
            return objectMapper.createArrayNode();
        }
    }

    // ✅ Recherche d'un objet unique dans un fichier JSON par cléUnique/valeur
    public static synchronized Optional<JsonNode> getNode(String fileName, Map.Entry<String ,Object> entry) {
        File file = new File(BASE_URL + "/" + fileName);
        if (!file.exists()) {
            System.out.println("❌ Fichier non trouvé : " + fileName);
            return Optional.empty();
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            if (!rootNode.isArray()) {
                System.out.println("❌ Erreur : Le fichier JSON doit contenir un tableau.");
                return Optional.empty();
            }
            for (JsonNode node : rootNode) {

                String key = entry.getKey();
                JsonNode entryValue = objectMapper.valueToTree(entry.getValue());
                if (node.has(key) && node.get(key).equals(entryValue)) {
                    return Optional.of(node);
                }

            }

            System.out.println("❌ Aucun objet trouvé avec les critères spécifiés.");
        } catch (IOException e) {
            System.out.println("❌ Erreur de lecture JSON : " + e.getMessage());
        }

        return Optional.empty();
    }

    public static synchronized List<JsonNode> getNodeWithoutFilter(String fileName){
        File file = new File(BASE_URL + "/" + fileName);
        List<JsonNode> resultList = new ArrayList<>();

        if (!file.exists()) {
            System.out.println("❌ Fichier non trouvé : " + fileName);
            return resultList; // Retourne une liste vide
        }

        try {
            // Lire le fichier JSON
            JsonNode rootNode = objectMapper.readTree(file);

            // Vérifier si le fichier JSON contient un tableau
            if (!rootNode.isArray()) {
                System.out.println("❌ Erreur : Le fichier JSON doit contenir un tableau.");
                return resultList; // Retourne une liste vide
            }

            // Parcourir et ajouter chaque nœud à la liste
            rootNode.forEach(resultList::add);

        } catch (IOException e) {
            System.out.println("❌ Erreur de lecture JSON : " + e.getMessage());
        }

        return resultList; // Retourne la liste des nœuds
    }

    //Renvoie dans une liste le noeud sans appliquer de clé
    public static synchronized ArrayNode getArrayNode(String fileName) {
        File file = new File(BASE_URL + "/" + fileName);
        ArrayNode resultList = objectMapper.createArrayNode();

        if (!file.exists()) {
            System.out.println("❌ Fichier non trouvé : " + fileName);
            return resultList; // Retourne une liste vide
        }

        try {
            // Lire le fichier JSON
            JsonNode rootNode = objectMapper.readTree(file);

            // Vérifier si le fichier JSON contient un tableau
            if (!rootNode.isArray()) {
                System.out.println("❌ Erreur : Le fichier JSON doit contenir un tableau.");
                return resultList; // Retourne une liste vide
            }

            // Parcourir et ajouter chaque nœud à la liste
            rootNode.forEach(resultList::add);

        } catch (IOException e) {
            System.out.println("❌ Erreur de lecture JSON : " + e.getMessage());
        }

        return resultList; // Retourne la liste des nœuds
    }

    public static Optional<JsonNode> getNodeAllMatch(String fileName,List<Map.Entry<String,Object>>entries){
        File file = new File(BASE_URL + "/" + fileName);
        if (!file.exists()) {
            System.out.println("❌ Fichier non trouvé : " + fileName);
            return Optional.empty();
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            if (!rootNode.isArray()) {
                System.out.println("❌ Erreur : Le fichier JSON doit contenir un tableau.");
                return Optional.empty();
            }

            boolean allMatch;
            for (JsonNode node : rootNode) {
                allMatch=true;
                for(Map.Entry<String,Object>entry:entries){
                    String key=entry.getKey();
                    JsonNode entryValue=objectMapper.valueToTree(entry.getValue());
                    if (!node.has(key) || !node.get(key).equals(entryValue)) {
                        allMatch=false;
                        break;
                    }
                }
                if(allMatch) return Optional.of(node);

            }

            return Optional.empty();
        } catch (IOException e) {
            System.out.println("❌ Erreur de lecture JSON : " + e.getMessage());
            return Optional.empty();
        }
    }

    public static synchronized void removeFromJson(String fileName,Map.Entry<String,Object>entry){
        File file = new File(BASE_URL + "/" + fileName);
        if (!file.exists()) {
            System.out.println("❌ Fichier non trouvé : " + fileName);
            return ;
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            if (!rootNode.isArray()) {
                System.out.println("❌ Erreur : Le fichier JSON doit contenir un tableau.");
                return ;
            }
            JsonNode entryValue=objectMapper.valueToTree(entry.getValue());
            for (Iterator<JsonNode> it = rootNode.iterator(); it.hasNext();) {
                JsonNode node=it.next();
                if(node.has(entry.getKey())&&node.get(entry.getKey()).equals(entryValue)){
                    it.remove();
                }
            }

            System.out.println("❌ Aucun objet trouvé avec les critères spécifiés.");
        } catch (IOException e) {
            System.out.println("❌ Erreur de lecture JSON : " + e.getMessage());
        }
    }
    
    public static void updateNode(JsonNode node,List<Map.Entry<String,Object>>updateList){
        
        for(Map.Entry<String,Object> update : updateList){
            String key=update.getKey();
            JsonNode jsonValue=objectMapper.valueToTree(update.getValue());
            ObjectNode obj=(ObjectNode) node;
            if(node.has(key)){
                obj.set(key,jsonValue);
            }
            
        }
    }


    public static synchronized void updateJsonObject(String fileName,Map.Entry<String,Object> entry,Map.Entry<String,Object> values) {
        File file = new File(BASE_URL + "/" + fileName);
        if (!file.exists()) {
            System.out.println("❌ Fichier non trouvé : " + fileName);
            return;
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            if (!rootNode.isArray()) {
                System.out.println("❌ Erreur : Le fichier JSON doit contenir un tableau.");
                return;
            }
            JsonNode updatedValue = JsonManager.objectMapper.valueToTree(values.getValue());
            JsonNode entryValue=objectMapper.valueToTree(entry.getValue());
            for (JsonNode node : rootNode) {
                ObjectNode objectNode = (ObjectNode) node;
                if (objectNode.has(entry.getKey()) && objectNode.get(entry.getKey()).equals(entryValue)) {
                    objectNode.set(values.getKey(),updatedValue);
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);
                    System.out.println("✅ Donnée modifiée avec succès !");
                    return;
                }

            }
            System.out.println("❌ Aucune correspondance trouvée.");


        } catch (IOException e) {
            System.out.println("❌ Erreur d'écriture JSON : " + e.getMessage());
        }
    }

    // ✅ Suppression d'un fichier JSON
    public static synchronized void deleteJsonFile(String fileName) {
        File file = new File(BASE_URL + "/" + fileName);
        if (file.exists() && file.delete()) {
            System.out.println("✅ Fichier supprimé : " + fileName);
        } else {
            System.out.println("❌ Impossible de supprimer le fichier.");
        }
    }

    public boolean updateTreeRemarkableStatus(String fileName, String idBase, String newStatus) {
        File file = new File(BASE_URL + "/" + fileName);
        if (!file.exists()) {
            System.out.println("❌ Fichier non trouvé : " + fileName);
            return false;
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            if (rootNode.isArray()) {
                ArrayNode arrayNode = (ArrayNode) rootNode;

                // Rechercher l'arbre avec l'ID donné
                for (JsonNode node : arrayNode) {
                    if (node.has("idBase") && node.get("idBase").asText().equals(idBase)) {
                        ((ObjectNode) node).put("remarquable", newStatus);
                        // Écrire les modifications dans le fichier
                        objectMapper.writeValue(file, arrayNode);
                        System.out.println("✅ Arbre mis à jour dans le fichier JSON !");
                        return true;
                    }
                }
            } else {
                System.out.println("❌ Le fichier JSON doit contenir un tableau.");
            }
        } catch (IOException e) {
            System.out.println("❌ Erreur lors de la mise à jour du fichier JSON : " + e.getMessage());
        }
        return false;
    }

    public static synchronized List<JsonNode> getAllNodes(String fileName) {
        File file = new File(BASE_URL + "/" + fileName);
        if (!file.exists()) {
            System.out.println("❌ Fichier non trouvé : " + fileName);
            return Collections.emptyList();
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            if (!rootNode.isArray()) {
                System.out.println("❌ Erreur : Le fichier JSON doit contenir un tableau.");
                return Collections.emptyList();
            }

            List<JsonNode> nodes = new ArrayList<>();
            rootNode.forEach(nodes::add);
            return nodes;
        } catch (IOException e) {
            System.out.println("❌ Erreur de lecture JSON : " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public static int getLastId(String filename) {
        Optional<JsonNode> root = getRootNode(filename);
        if (root.isPresent() && root.get().isArray()) {
            JsonNode array = root.get();
            int lastId = 0;
            for (JsonNode node : array) {
                if (node.has("id")) {
                    lastId = Math.max(lastId, node.get("id").asInt());
                }
            }
            return lastId;
        }
        return 0; // Retourne 0 si le fichier est vide ou inexistant
    }

    public static void updateJsonField(String filename, String searchKey, String searchValue, String fieldToUpdate, String newValue) {
        File file = new File(filename);

        try {
            if (!file.exists()) {
                System.err.println("⚠️ Fichier JSON non trouvé : " + filename);
                return;
            }

            JsonNode root = objectMapper.readTree(file);
            if (root.isArray()) {
                ArrayNode arrayNode = (ArrayNode) root;

                for (JsonNode node : arrayNode) {
                    if (node.has(searchKey) && node.get(searchKey).asText().equals(searchValue)) {
                        ((ObjectNode) node).put(fieldToUpdate, newValue);
                        objectMapper.writeValue(file, arrayNode);
                        System.out.println("✅ Champ '" + fieldToUpdate + "' mis à jour avec succès !");
                        return;
                    }
                }
            }
            System.err.println("❌ Aucun enregistrement trouvé pour " + searchKey + " = " + searchValue);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Erreur lors de la mise à jour du fichier JSON !");
        }
    }


    public static synchronized void removeDonateurById(String fileName, int idToDelete) {
        File file = new File(BASE_URL + "/" + fileName);
        if (!file.exists()) {
            System.out.println("❌ Fichier non trouvé : " + fileName);
            return;
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            if (!rootNode.isArray()) {
                System.out.println("❌ Erreur : Le fichier JSON doit contenir un tableau.");
                return;
            }

            ArrayNode arrayNode = (ArrayNode) rootNode;
            boolean found = false;

            // Parcourir le fichier JSON pour chercher l'ID à supprimer
            for (int i = 0; i < arrayNode.size(); i++) {
                if (arrayNode.get(i).has("id") && arrayNode.get(i).get("id").asInt() == idToDelete) {
                    arrayNode.remove(i);
                    found = true;
                    break;
                }
            }

            if (found) {
                // Écriture du fichier après suppression
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, arrayNode);
                System.out.println("✅ Donateur avec ID " + idToDelete + " supprimé avec succès.");
            } else {
                System.out.println("⚠️ Aucun donateur trouvé avec l'ID " + idToDelete);
            }
        } catch (IOException e) {
            System.out.println("❌ Erreur lors de la modification du fichier JSON : " + e.getMessage());
        }
    }



}
