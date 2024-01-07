package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.PopUp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StageEtats extends Stage implements Initializable {
    //private Stage stage;

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
    protected void onBtnClickEtatIntervenant() throws IOException
    {
        StageGeneration stage = Manager.creer("generation", this);

        stage.setVue("intervenant");
        stage.showAndWait();
        //StageGeneration.creer("intervenant").show();
    }

    public void onBtnClickEtatModule(ActionEvent actionEvent) throws IOException {
        StageGeneration stage = Manager.creer("generation", this);

        stage.setVue("module");
        stage.showAndWait();
        //StageGeneration.creer("module").show();
    }

    public void onBtnClickEtatCSV(ActionEvent actionEvent) throws IOException {
        String nomAnnee = Controleur.get().getMetier().getAnneeActuelle().getNom();
        if ( !Controleur.get().getMetier().getDonneesCSV( nomAnnee ) ) {
            PopUp.warning("Erreur de génération","Erreur de génération","Les données n'ont pas pus être récupéré");
        }
        else if ( PopUp.confirmationR("Fichier CSV","Fichier CSV généré","Le fichier CSV a bien été généré voulez vous l'ouvrir ?") ) {
            //StageAffichageCSV.creer(nomAnnee).show();
            Stage stage = Manager.creer("affichageCSV");
            stage.show();
            this.close();
        }
    }

    public void onBtnClickEtatAnnuler(ActionEvent actionEvent) throws IOException{
        this.close();
        //StagePrincipal.creer().show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

    }
}
