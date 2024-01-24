package fr.elpine.astre.ihm.stage;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class StageAffichageCSV extends Stage
{
    @FXML private TextField txtFieldRecherche;

    @FXML private TableView<ObservableList<String>> tabAffCsv;
    @FXML private TableColumn<ObservableList<String>,String> tcCategorie;
    @FXML private TableColumn<ObservableList<String>,String> tcNom;
    @FXML private TableColumn<ObservableList<String>,String> tcPrenom;
    @FXML private TableColumn<ObservableList<String>,String> tcHServ;
    @FXML private TableColumn<ObservableList<String>,String> tcHMax;
    @FXML private TableColumn<ObservableList<String>,String> tcRatioTP;
    @FXML private TableColumn<ObservableList<String>,String> tcTheoTotImpair;
    @FXML private TableColumn<ObservableList<String>,String> tcReelTotImpair;
    @FXML private TableColumn<ObservableList<String>,String> tcTheoTotPair;
    @FXML private TableColumn<ObservableList<String>,String> tcReelTotPair;
    @FXML private TableColumn<ObservableList<String>,String> tcTheoTot;
    @FXML private TableColumn<ObservableList<String>,String> tcReelTot;
    @FXML private TableColumn<ObservableList<String>,String> tcTheoS1;
    @FXML private TableColumn<ObservableList<String>,String> tcReelS1;
    @FXML private TableColumn<ObservableList<String>,String> tcTheoS3;
    @FXML private TableColumn<ObservableList<String>,String> tcReelS3;
    @FXML private TableColumn<ObservableList<String>,String> tcTheoS5;
    @FXML private TableColumn<ObservableList<String>,String> tcReelS5;
    @FXML private TableColumn<ObservableList<String>,String> tcTheoS2;
    @FXML private TableColumn<ObservableList<String>,String> tcReelS2;
    @FXML private TableColumn<ObservableList<String>,String> tcTheoS4;
    @FXML private TableColumn<ObservableList<String>,String> tcReelS4;
    @FXML private TableColumn<ObservableList<String>,String> tcTheoS6;
    @FXML private TableColumn<ObservableList<String>,String> tcReelS6;
    @FXML private ChoiceBox<String> cbNomAnnee;

    private ArrayList<String[]> alDonnees;

    public StageAffichageCSV() // fxml -> "affichageCSV"
    {
        this.setTitle("Affichage CSV");
        this.setMinWidth(1400);
        this.setMinHeight(600);
    }

    public static ArrayList<String> listerFichiers() {
        ArrayList<String> fichiers = new ArrayList<>();
        File repertoire = new File("Export/CSV");

        if (repertoire.isDirectory()) {
            File[] files = repertoire.listFiles((dir, name) -> name.endsWith(".csv"));

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fichiers.add(file.getName());
                    }
                }
            }
        }

        return fichiers;
    }

    public ArrayList<String[]> lireCSV(String cheminFichierCSV ) {
        //File f = /*cheminFichierCSV.contains("/") ? new File(cheminFichierCSV) :*/ new File("Export/CSV/" + cheminFichierCSV);

        try (CSVReader reader = new CSVReader(new FileReader("Export/CSV/" + cheminFichierCSV))) {
            ArrayList<String[]> alLigne = new ArrayList<>();
            String[] ligne;
            reader.readNext();
            while ((ligne = reader.readNext()) != null) {
                alLigne.add(ligne);
            }
            return alLigne;
        } catch (IOException | CsvValidationException e) { return new ArrayList<>(); }

    }

    @FXML private void onBtnClickAnnuler()
    {
        this.close();
    }

    @FXML private void onBtnRechercher() {
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

        this.tabAffCsv.setItems(listeToObservable(ensTemp));
    }

    public ObservableList<ObservableList<String>> listeToObservable(ArrayList<String[]> liste) {
        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();

        // Convertir chaque ligne (tableau de chaînes) en ObservableList de chaînes pour l'affichage dans la TableView
        for (String[] ligne : liste) {
            ObservableList<String> row = FXCollections.observableArrayList();
            row.addAll(Arrays.asList(ligne));
            rows.add(row);
        }

        return rows;
    }

    public void setFichier(String fichier)
    {
        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

        this.cbNomAnnee.setItems(FXCollections.observableArrayList(listerFichiers()));
        this.cbNomAnnee.setValue(fichier);
        this.cbNomAnnee.valueProperty().addListener((observable, oldValue, newValue) -> this.tabAffCsv.setItems(this.listeToObservable(this.lireCSV(this.cbNomAnnee.getValue()))));

        this.alDonnees = this.lireCSV(fichier);

        ArrayList<TableColumn<ObservableList<String>,String>> alCol = new ArrayList<>(Arrays.asList( tcCategorie, tcNom, tcPrenom, tcHServ, tcHMax, tcRatioTP, tcTheoS1, tcReelS1, tcTheoS3, tcReelS3,tcTheoS5, tcReelS5, tcTheoTotImpair, tcReelTotImpair,  tcTheoS2, tcReelS2, tcTheoS4, tcReelS4,tcTheoS6, tcReelS6, tcTheoTotPair, tcReelTotPair, tcTheoTot, tcReelTot ));

        int cpt = 0;
        for ( TableColumn<ObservableList<String>,String> col : alCol )
        {
            int cptTemp = cpt;
            col.setCellValueFactory (cellData -> new SimpleStringProperty( cellData.getValue().get(cptTemp)) );
            cpt++;
        }

        tabAffCsv.setItems(listeToObservable(this.alDonnees));
        tabAffCsv.refresh();
    }
}
