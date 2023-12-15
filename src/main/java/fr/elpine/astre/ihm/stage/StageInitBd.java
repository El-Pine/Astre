package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageInitBd
{
    private Stage stage;
    private static StageAccueilConfig parent;

    public static Stage creer( StageAccueilConfig parent ) throws IOException
    {
        Stage stage = new Stage();
        StageInitBd.parent = parent;

        FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("initDb.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        StageInitBd stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) stageCtrl.setStage(stage);

        stage.setTitle("Config Base de données");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            if ( parent != null )
                parent.activer();
        });

        return stage;
    }

    private void setStage(Stage stage) { this.stage = stage; }

    public void onBtnValider(ActionEvent actionEvent){
        if (Controleur.get().reloadDb()) {
            stage.close();
            if (parent != null)
                parent.activer();
            else {
                try {
                    StagePrincipal.creer().show();
                } catch (IOException e) { throw new RuntimeException(e); }
            }
        }
    }

    public void onBtnAnnuler(ActionEvent actionEvent) throws IOException {
        stage.close();
        if (parent != null)
            parent.activer();
        else
            StagePrincipal.creer().show();
    }
}