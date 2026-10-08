package App.AssociationMember;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jfoenix.controls.JFXButton;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FiltreController {

    @FXML private ToggleButton croissantToggle, decroissantToggle;
    @FXML private ToggleButton idToggle, nomToggle;
    @FXML private ToggleButton nonToggle, ouiToggle;
    @FXML private ToggleButton taille1, taille2, taille3, taille4;
    @FXML private JFXButton afficherButton;

    private ListView<String> treeListView;
    private TextField recherche;
    private ChangeListener<String> rechercheListener;
    private ObservableList<JsonNode> treeList;
    private FilteredList<JsonNode> filteredList;
    private SortedList<JsonNode> sortedList;

    ToggleGroup groupRecherche, groupTaille, groupRemarquable, groupOrdre;

    @FXML
    public void initialize() {
        groupRecherche = new ToggleGroup();
        groupTaille = new ToggleGroup();
        groupRemarquable = new ToggleGroup();
        groupOrdre = new ToggleGroup();

        groupRecherche.getToggles().addAll(idToggle, nomToggle);
        groupTaille.getToggles().addAll(taille1, taille2, taille3, taille4);
        groupRemarquable.getToggles().addAll(ouiToggle, nonToggle);
        groupOrdre.getToggles().addAll(croissantToggle, decroissantToggle);
    }

    public void setup(ObservableList<JsonNode> tree, ListView<String> treeListView, TextField recherche, ChangeListener<String> rechercheListener) {
        this.treeList = tree;
        this.treeListView = treeListView;
        this.recherche = recherche;
        this.rechercheListener = rechercheListener;
        this.filteredList = new FilteredList<>(treeList, _ -> true);
        this.sortedList = new SortedList<>(filteredList);
        handleFilter();
    }

    public void handleFilter() {
        rechercheListener = this::applyFilter;
        recherche.textProperty().addListener(rechercheListener);

        ouiToggle.setOnAction(event -> updateFilter());
        nonToggle.setOnAction(event -> updateFilter());
        idToggle.setOnAction(event -> updateFilter());
        nomToggle.setOnAction(event -> updateFilter());
        taille1.setOnAction(event -> updateFilter());
        taille2.setOnAction(event -> updateFilter());
        taille3.setOnAction(event -> updateFilter());
        taille4.setOnAction(event -> updateFilter());
        croissantToggle.setOnAction(event -> updateFilter());
        decroissantToggle.setOnAction(event -> updateFilter());
        afficherButton.setOnAction(event -> {
                    //ferme la fenetre
                    afficherButton.getScene().getWindow().hide();
        });
    }

    // ✅ Met à jour la `FilteredList` et trie si nécessaire
    private void updateFilter() {
        filteredList.setPredicate(PredicateAccumulator(recherche.getText()));

        if (croissantToggle.isSelected()) {
            if(nomToggle.isSelected()) {
                sortedList.setComparator((tree1, tree2) -> tree1.get("libelle_france").asText().compareTo(tree2.get("libelle_france").asText()));
            } else if(idToggle.isSelected()) {
                sortedList.setComparator((tree1, tree2) -> tree1.get("idBase").asInt() - tree2.get("idBase").asInt());
            }
        } else if (decroissantToggle.isSelected()) {
            if(nomToggle.isSelected()) {
                sortedList.setComparator((tree1, tree2) -> tree2.get("libelle_france").asText().compareTo(tree1.get("libelle_france").asText()));
            } else if(idToggle.isSelected()) {
                sortedList.setComparator((tree1, tree2) -> tree2.get("idBase").asInt() - tree1.get("idBase").asInt());
            }
        }

        updateTreeListView();
    }

    private void updateTreeListView() {
        // ✅ Met à jour la `ListView` avec les arbres filtrés et triés
        List<String> affichageList = new ArrayList<>();

        for (JsonNode tree : sortedList) {
            String id = tree.get("idBase").asText();
            String nom = tree.get("libelle_france").asText();
            affichageList.add("🌳 ID " + id + " - Nom " + nom);
        }

        treeListView.setItems(FXCollections.observableArrayList(affichageList));

    }

    private void applyFilter(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        filteredList.setPredicate(PredicateAccumulator(newValue));
        updateTreeListView();
    }

    public Predicate<JsonNode> PredicateAccumulator(String newValue) {
        List<Predicate<JsonNode>> activeFilters = new ArrayList<>();
        if (idToggle.isSelected()) activeFilters.add(byId(newValue));
        if (nomToggle.isSelected()) activeFilters.add(byNom(newValue));

        if (taille1.isSelected()) activeFilters.add(circonference(0, 50));
        if (taille2.isSelected()) activeFilters.add(circonference(50, 100));
        if (taille3.isSelected()) activeFilters.add(circonference(100, 150));
        if (taille4.isSelected()) activeFilters.add(circonference(150, 200));

        if (ouiToggle.isSelected()) activeFilters.add(remarquable("OUI"));
        else if (nonToggle.isSelected()) activeFilters.add(remarquable("NON"));

        if (activeFilters.isEmpty()) activeFilters.add(defaultFilter(newValue));

        return activeFilters.stream().reduce(Predicate::and).orElse(_ -> true);
    }

    private Predicate<JsonNode> defaultFilter(String newValue) {
        return tree -> {
            if (newValue == null || newValue.isEmpty()) return true;
            String lowerCaseFilter = newValue.toLowerCase();
            return tree.has("libelle_france") && tree.get("libelle_france").asText().toLowerCase().contains(lowerCaseFilter)
                    || tree.has("genre") && tree.get("genre").asText().toLowerCase().contains(lowerCaseFilter)
                    || tree.has("espece") && tree.get("espece").asText().toLowerCase().contains(lowerCaseFilter)
                    || tree.has("lieu") && tree.get("lieu").asText().toLowerCase().contains(lowerCaseFilter)
                    || tree.has("idBase") && tree.get("idBase").asText().contains(newValue);
        };
    }

    private Predicate<JsonNode> byId(String newValue) {
        return node -> node.get("idBase").asText().contains(newValue);
    }

    private Predicate<JsonNode> byNom(String newValue) {
        return node -> node.get("libelle_france").asText().toLowerCase().contains(newValue.toLowerCase());
    }

    private Predicate<JsonNode> circonference(int min, int max) {
        return node -> {
            int circonference = node.get("circonference").asInt();
            return circonference >= min && circonference <= max;
        };
    }

    private Predicate<JsonNode> remarquable(String remarquable) {
        return node -> node.get("remarquable").asText().equals(remarquable);
    }


}
