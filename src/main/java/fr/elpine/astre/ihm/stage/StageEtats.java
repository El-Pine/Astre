package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.ihm.outil.Manager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class StageEtats extends Stage implements Initializable
{
    public StageEtats() // fxml -> "etats"
    {
        this.setTitle("États");
        this.setMinWidth(300);
        this.setMinHeight(450);
        this.setResizable(false);
    }

    @FXML private void onBtnClickEtatIntervenant() {
        StageGeneration stage = Manager.creer("generation", this);

        assert stage != null;
        stage.setVue("intervenant");
        stage.showAndWait();
    }

    @FXML private void onBtnClickEtatModule() {
        StageGeneration stage = Manager.creer("generation", this);

        assert stage != null;
        stage.setVue("module");
        stage.showAndWait();
    }

    @FXML private void onBtnClickEtatCSV()
    {
        // Vérifier si le répertoire existe, sinon le créer
        File dossierSrc    = new File("Export"      );
        File dossierCSV  = new File("Export/CSV" );
        if (dossierSrc.exists()) {
            if (!dossierCSV.exists()) {
                if (!(dossierCSV.mkdirs()))
                    System.err.println("Impossible de créer le répertoire");
            }
        } else {
            if (!(dossierSrc.mkdirs() && dossierCSV.mkdirs()))
                System.err.println("Impossible de créer le répertoire");
        }


        String nomAnnee = Controleur.get().getMetier().getAnneeActuelle().getNom();
        String fichier = Controleur.get().getMetier().getDonneesCSV( nomAnnee, this );

        if ( fichier.equals("0") ) {
            PopUp.warning("Erreur de génération",null,"Les données n'ont pas pus être récupérées");
        }
        else if ( PopUp.confirmationR("Fichier CSV",null,"Le fichier CSV a bien été généré voulez vous l'ouvrir ?") ) {
            StageAffichageCSV stage = Manager.creer("affichageCSV");
            stage.setFichier(fichier);
            stage.showAndWait();
        }
    }

    @FXML private void onBtnClickEtatAnnuler() {
        this.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );
    }
}
