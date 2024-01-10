package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class StageEtats extends Stage implements Initializable {
    public StageEtats() // fxml -> "etats"
    {
        this.setTitle("Etats");
        this.setMinWidth(300);
        this.setMinHeight(450);
        this.setResizable(false);
    }

    /*public static Stage creer() throws IOException{

        Stage stage = new Stage();

        AstreApplication.refreshIcon(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("etats.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 300, 400);

        StageEtats stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) stageCtrl.setStage(stage);

        stage.setTitle("Etats");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            try {
                StagePrincipal.creer().show();
            } catch (IOException ignored) { }
        });

        return stage;
    }

    private void setStage(Stage stage) {
        this.stage = stage;
    }*/

    @FXML
    protected void onBtnClickEtatIntervenant() {
        StageGeneration stage = Manager.creer("generation", this);

        assert stage != null;
        stage.setVue("intervenant");
        stage.showAndWait();
    }

    public void onBtnClickEtatModule() {
        StageGeneration stage = Manager.creer("generation", this);

        assert stage != null;
        stage.setVue("module");
        stage.showAndWait();
    }

    public void onBtnClickEtatCSV() {
        String nomAnnee = Controleur.get().getMetier().getAnneeActuelle().getNom();
        String fichier = Controleur.get().getMetier().getDonneesCSV( nomAnnee );
        if ( fichier.equals("0") ) {
            PopUp.warning("Erreur de génération","Erreur de génération","Les données n'ont pas pus être récupéré");
        }
        else if ( PopUp.confirmationR("Fichier CSV","Fichier CSV généré","Le fichier CSV a bien été généré voulez vous l'ouvrir ?") ) {
            StageAffichageCSV stage = Manager.creer("affichageCSV");
            stage.setFichier(fichier);
            stage.showAndWait();
        }

    }

    public void onBtnClickEtatAnnuler() {
        this.close();
        //StagePrincipal.creer().show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

    }
}
