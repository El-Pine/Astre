package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class StageAccueilConfig
{
    private Stage stage;
    @FXML
    private TableColumn tcCode;
    @FXML
    private TableColumn tcNom;
    @FXML
    private TableColumn tcHMax;
    @FXML
    private TableColumn tcHServ;


    public static Stage creer() throws IOException
    {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("accueilConfig.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        StageAccueilConfig stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) stageCtrl.setStage(stage);

        stage.setTitle("Accueil Config");
        stage.setScene(scene);
        stageCtrl.majTableauCatInter();

        stage.setOnCloseRequest(e -> {
            // perform actions before closing
            try { StagePrincipal.creer().show(); } catch (IOException ignored) {}
        });

        return stage;
    }

    private void majTableauCatInter()
    {

        ArrayList<CategorieIntervenant> ensCatInter = Controleur.get().getDb().getAllCategorieIntervenant();


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