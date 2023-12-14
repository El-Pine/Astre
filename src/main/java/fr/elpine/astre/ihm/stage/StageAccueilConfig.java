package fr.elpine.astre.ihm.stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageAccueilConfig
{
    private Stage stage;

    public static Stage creer() throws IOException
    {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("accueilConfig.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        StageAccueilConfig stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) stageCtrl.setStage(stage);

        stage.setTitle("Accueil Config");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            // perform actions before closing
            try { StagePrincipal.creer().show(); } catch (IOException ignored) {}
        });

        return stage;
    }

    private void setStage(Stage stage) { this.stage = stage; }

    public void onBtnConfigBdd(ActionEvent actionEvent) throws IOException {
        desactiver();
        StageInitBd.creer( this ).show();
    }

    public void onBtnAjouter(ActionEvent actionEvent) throws IOException {
        desactiver();
        StageAjouterCategories.creer( this ).show();
    }

    public void desactiver()
    {
        this.stage.getScene().lookup("#btnConfigBd").setDisable(true);
        this.stage.getScene().lookup("#btnAjouter").setDisable(true);
        this.stage.getScene().lookup("#btnSupprimer").setDisable(true);
    }

    public void activer() {
        this.stage.getScene().lookup("#btnConfigBd").setDisable(false);
        this.stage.getScene().lookup("#btnAjouter").setDisable(false);
        this.stage.getScene().lookup("#btnSupprimer").setDisable(false);
    }
}