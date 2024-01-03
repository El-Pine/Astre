package fr.elpine.astre.ihm.stage;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.metier.objet.Intervenant;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class StageAffichageCSV implements Initializable {

    private static String nomAnnee;
    public Button btnAnnuler;
    public TextField txtFieldRecherche;

    public TableView<ObservableList<String>> tabAffInter;
    public TableColumn<ObservableList<String>,String> tcCategorie;
    public TableColumn<ObservableList<String>,String> tcNom;
    public TableColumn<ObservableList<String>,String> tcPrenom;
    public TableColumn<ObservableList<String>,String> tcHServ;
    public TableColumn<ObservableList<String>,String> tcHMax;
    public TableColumn<ObservableList<String>,String> tcRatioTP;
    public TableColumn<ObservableList<String>,String> tcTheoTotImpair;
    public TableColumn<ObservableList<String>,String> tcReelTotImpair;
    public TableColumn<ObservableList<String>,String> tcTheoTotPair;
    public TableColumn<ObservableList<String>,String> tcReelTotPair;
    public TableColumn<ObservableList<String>,String> tcTheoTot;
    public TableColumn<ObservableList<String>,String> tcReelTot;
    private Stage stage;

    private ArrayList<String[]> alDonnees;
    private static ObservableList<String[]> ensDonnes;

    public static Stage creer( String nomAnnee ) throws IOException{

        Stage stage = new Stage();

        AstreApplication.refreshIcon(stage);
        StageAffichageCSV.nomAnnee = nomAnnee;

        FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("affichageCSV.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 786, 400);

        StageAffichageCSV stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) { stageCtrl.setStage(stage); }

        stage.setTitle("Affichage CSV");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            try {
                StageEtats.creer().show();
            } catch (IOException ignored) { }
        });

        return stage;
    }
    
    private void setStage(Stage stage) {
        this.stage = stage;
    }

    public ArrayList<String[]> LireCSV(String annee ) {
        // Chemin du fichier CSV
        String cheminFichierCSV = "CSV/résultat-" + annee + ".csv";

        try (CSVReader reader = new CSVReader(new FileReader(cheminFichierCSV))) {
            ArrayList<String[]> alLigne = new ArrayList<String[]>();
            String[] ligne;
            while ((ligne = reader.readNext()) != null) {
                alLigne.add(ligne);
            }
            return alLigne;
        } catch (IOException | CsvValidationException e) { return new ArrayList<String[]>(); }

    }

    public void onBtnClickAnnuler(ActionEvent actionEvent) {
    }

    public void onBtnRechercher(ActionEvent actionEvent) {
        String recherche = txtFieldRecherche.getText();

        ArrayList<String[]> ensTemp = new ArrayList<>();

        for (String[] ligne : this.alDonnees) {
            for (String s : ligne) {
                if (s.toLowerCase().contains(recherche.toLowerCase())) {
                    ensTemp.add(ligne);
                    break; // Pour éviter d'ajouter plusieurs fois la même ligne
                }
            }
        }

        tabAffInter.setItems(listeToObservable(ensTemp));
    }

    public void initialize(URL location, ResourceBundle resources) {

        this.alDonnees = this.LireCSV(StageAffichageCSV.nomAnnee);

        tcCategorie.setCellValueFactory     (cellData -> new SimpleStringProperty( cellData.getValue().get(0))); // Colonne de codeCategorie
        tcNom.setCellValueFactory           (cellData -> new SimpleStringProperty( cellData.getValue().get(1))); // Colonne de nom
        tcPrenom.setCellValueFactory        (cellData -> new SimpleStringProperty( cellData.getValue().get(2))); // Colonne de prenom
        tcHServ.setCellValueFactory         (cellData -> new SimpleStringProperty( cellData.getValue().get(3))); // Colonne de heureService
        tcHMax.setCellValueFactory          (cellData -> new SimpleStringProperty( cellData.getValue().get(4))); // Colonne de heureMax
        tcRatioTP.setCellValueFactory       (cellData -> new SimpleStringProperty( cellData.getValue().get(5))); // Colonne de ratioTP
        tcTheoTotImpair.setCellValueFactory (cellData -> new SimpleStringProperty( cellData.getValue().get(6)));
        tcReelTotImpair.setCellValueFactory (cellData -> new SimpleStringProperty( cellData.getValue().get(7)));
        tcTheoTotPair.setCellValueFactory   (cellData -> new SimpleStringProperty( cellData.getValue().get(8)));
        tcReelTotPair.setCellValueFactory   (cellData -> new SimpleStringProperty( cellData.getValue().get(9)));
        tcTheoTot.setCellValueFactory       (cellData -> new SimpleStringProperty( cellData.getValue().get(10)));
        tcReelTot.setCellValueFactory       (cellData -> new SimpleStringProperty( cellData.getValue().get(11)));

        tabAffInter.setItems(listeToObservable(this.alDonnees));
    }

    public ObservableList<ObservableList<String>> listeToObservable(ArrayList<String[]> liste) {
        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();

        // Convertir chaque ligne (tableau de chaînes) en ObservableList de chaînes pour l'affichage dans la TableView
        for (int i = 1; i < liste.size(); i++) {
            String[] ligne = liste.get(i);
            ObservableList<String> row = FXCollections.observableArrayList();
            row.addAll(Arrays.asList(ligne));
            rows.add(row);
        }

        return rows;
    }
}
