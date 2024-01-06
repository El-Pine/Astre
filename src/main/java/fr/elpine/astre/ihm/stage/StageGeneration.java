package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.stage.PopUp.StagePopUp;
import fr.elpine.astre.metier.objet.Intervenant;
import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.Module;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class StageGeneration extends Stage implements Initializable
{
    //private Stage stage;
    private ObservableList<Intervenant> ensInter;
    private ObservableList<Module> ensMod;

    @FXML
    private TableView<String>tabGeneration;
    @FXML
    private TableColumn<String,String> g_1;
    @FXML
    private TableColumn<String,String>g_2;


    public StageGeneration()
    {
        this.setTitle("Generation");
    }



    /*public static Stage creer(String vue) throws IOException {

        Stage stage = new Stage();

        AstreApplication.refreshIcon(stage);

        if(vue=="intervenant")
        {
            StageGeneration.ensInter = FXCollections.observableArrayList(Controleur.get().getMetier().getIntervenants());
        }
        else if(vue=="module")
        {
            StageGeneration.ensMod = FXCollections.observableArrayList(Controleur.get().getMetier().getModules());
        }

        FXMLLoader fxmlLoader = new FXMLLoader(StageGeneration.class.getResource("generation.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1400, 600);

        StageGeneration stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) stageCtrl.setStage(stage);

        stage.setTitle("Generation");
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
    }*/

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {


    }


    public void setVue(String vue) {
        if(vue=="intervenant")
        {
            this.ensInter = FXCollections.observableArrayList(Controleur.get().getMetier().getIntervenants());
        }
        else if(vue=="module")
        {
            this.ensMod = FXCollections.observableArrayList(Controleur.get().getMetier().getModules());
        }
    }
}
