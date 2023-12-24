package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.metier.objet.Annee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
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
                StageEtats.creer().show();
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

    public void onBtnClickEtatCSV(ActionEvent actionEvent) {
    }

    public void onBtnClickEtatAnnuler(ActionEvent actionEvent) throws IOException{
        this.stage.close();
        StagePrincipal.creer().show();
    }
}
