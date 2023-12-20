package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.Astre;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StageAjoutRessource implements Initializable {
    private Stage stage;

    private ToggleGroup toggleGroup;

    @FXML
    private RadioButton rbHP;
    @FXML
    private RadioButton rbAutre;
    @FXML
    private ComboBox cbbInter;
    @FXML
    private TextField txtCommentaire;
    @FXML
    private TextField txtNbHeure;
    @FXML
    private ComboBox cbbCatHeure;
    @FXML
    private TextField txtNbSemaine;
    @FXML
    private TextField txtNbGp;

    public Stage creer(String code, String semestre, String nomLong, String abreviation) throws IOException
    {
        Stage stage = new Stage();
        toggleGroup = new ToggleGroup();
        rbAutre.setToggleGroup(toggleGroup);
        rbHP.setToggleGroup(toggleGroup);

        FXMLLoader fxmlLoader = new FXMLLoader(StageSaisieRessource.class.getResource("CreationModules.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1000, 660);

        StageAjoutRessource stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) stageCtrl.setStage(stage);

        stage.setTitle("Affectation");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            // perform actions before closing
            try { StagePrevisionnel.creer().show(); } catch (IOException ignored) {}
        });

        return stage;
    }

    @FXML
    private void setStage(Stage stage) { this.stage = stage; }
    @FXML
    public void onBtnAjouter() {
        //TODO faire fonctionnalité quand ajouter astre sera rajouter
    }

    public void onBtnAnnuler() {stage.close();}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rbHP.setSelected(false);
        rbAutre.setSelected(false);

        txtNbHeure.setDisable(true);
        txtNbSemaine.setDisable(true);
        txtNbGp.setDisable(true);
        cbbCatHeure.setDisable(true);
    }

    @FXML
    public void isHp() {
        txtNbHeure.setDisable(false);

        txtNbHeure.setDisable(true);
    }
    @FXML
    public void isAutreType() {
        cbbCatHeure.setDisable(false);
        txtNbSemaine.setDisable(false);
        txtNbGp.setDisable(false);

        txtNbSemaine.setDisable(true);
        txtNbGp.setDisable(true);
        cbbCatHeure.setDisable(true);

    }
}
