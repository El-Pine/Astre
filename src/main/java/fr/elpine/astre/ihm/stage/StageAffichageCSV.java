package fr.elpine.astre.ihm.stage;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class StageAffichageCSV extends Stage {

    public Button btnAnnuler;
    public TextField txtFieldRecherche;

    @FXML
    private TableView<ObservableList<String>> tabAffCsv;
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
    public TableColumn<ObservableList<String>,String> tcTheoS1;
    public TableColumn<ObservableList<String>,String> tcReelS1;
    public TableColumn<ObservableList<String>,String> tcTheoS3;
    public TableColumn<ObservableList<String>,String> tcReelS3;
    public TableColumn<ObservableList<String>,String> tcTheoS5;
    public TableColumn<ObservableList<String>,String> tcReelS5;
    public TableColumn<ObservableList<String>,String> tcTheoS2;
    public TableColumn<ObservableList<String>,String> tcReelS2;
    public TableColumn<ObservableList<String>,String> tcTheoS4;
    public TableColumn<ObservableList<String>,String> tcReelS4;
    public TableColumn<ObservableList<String>,String> tcTheoS6;
    public TableColumn<ObservableList<String>,String> tcReelS6;
    public ChoiceBox<String> cbNomAnnee;
    //private Stage stage;

    private ArrayList<String[]> alDonnees;

    public StageAffichageCSV() // fxml -> "affichageCSV"
    {
        this.setTitle("Affichage CSV");
        this.setMinWidth(1370);
        this.setMinHeight(450);
    }

    public static ArrayList<String> listerFichiers() {
        ArrayList<String> fichiers = new ArrayList<String>();
        File repertoire = new File("CSV");

        if (repertoire.isDirectory()) {
            File[] files = repertoire.listFiles( new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".csv");
                }
            });

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
        try (CSVReader reader = new CSVReader(new FileReader("CSV/" + cheminFichierCSV))) {
            ArrayList<String[]> alLigne = new ArrayList<String[]>();
            String[] ligne;
            reader.readNext();
            while ((ligne = reader.readNext()) != null) {
                alLigne.add(ligne);
            }
            return alLigne;
        } catch (IOException | CsvValidationException e) { return new ArrayList<String[]>(); }

    }

    public void onBtnClickAnnuler(ActionEvent actionEvent)
    {
        /*this.setOnCloseRequest(e -> {
            try {
                StageEtats.creer().show();
            } catch (IOException ignored) { }
        });*/
        this.close();
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
        String nomAnnee = Controleur.get().getMetier().getAnneeActuelle().getNom();

        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

        this.cbNomAnnee.setItems(FXCollections.observableArrayList(listerFichiers()));
        this.cbNomAnnee.setValue(fichier);
        this.cbNomAnnee.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.tabAffCsv.setItems(this.listeToObservable(this.lireCSV(this.cbNomAnnee.getValue())));
        });

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
