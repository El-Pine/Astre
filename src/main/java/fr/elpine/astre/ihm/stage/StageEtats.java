package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.PopUp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class StageEtats {
    private Stage stage;

    public static Stage creer() throws IOException{

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
    }

    @FXML
    protected void onBtnClickEtatIntervenant() throws IOException
    {
        this.stage.close();
        StageGeneration.creer("intervenant").show();

    }

    public void onBtnClickEtatModule(ActionEvent actionEvent) throws IOException {
        this.stage.close();
        StageGeneration.creer("module").show();
    }

    public void onBtnClickEtatCSV(ActionEvent actionEvent) throws IOException {
        String nomAnnee = Controleur.get().getMetier().getAnneeActuelle().getNom();
        if ( !Controleur.get().getDb().getDonneesCSV( nomAnnee ) ) {
            PopUp.warning("Erreur de génération","Erreur de génération","Les données n'ont pas pus être récupéré");
        }
        else if ( PopUp.confirmationR("Fichier CSV","Fichier CSV généré","Le fichier CSV a bien été généré voulez vous l'ouvrir ?") ) {
            StageAffichageCSV.creer(nomAnnee).show();
            this.stage.close();
        }
    }

    public void onBtnClickEtatAnnuler(ActionEvent actionEvent) throws IOException{
        this.stage.close();
        StagePrincipal.creer().show();
    }
}
