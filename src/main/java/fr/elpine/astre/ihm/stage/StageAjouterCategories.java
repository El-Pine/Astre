package fr.elpine.astre.ihm.stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageAjouterCategories
{
    private Stage stage;
    private static StageAccueilConfig parent;

    public static Stage creer( StageAccueilConfig parent ) throws IOException
    {
        Stage stage = new Stage();
        StageAjouterCategories.parent = parent;

        FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("ajouterCategories.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        StageAjouterCategories stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) stageCtrl.setStage(stage);

        stage.setTitle("Accueil Config");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            parent.activer();
        });

        return stage;
    }

    private void setStage(Stage stage) { this.stage = stage; }

    public void onBtnEnregistrerCatInter(ActionEvent actionEvent) {
        parent.activer();
        stage.close();
    }

    public void onBtnAnnulerCatInter(ActionEvent actionEvent) {
        parent.activer();
        stage.close();
    }

    public void onBtnEnregistrerCatH(ActionEvent actionEvent) {
        parent.activer();
        stage.close();
    }

    public void onBtnAnnulerCatH(ActionEvent actionEvent) {
        parent.activer();
        stage.close();
    }
}