package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.metier.objet.CategorieHeure;
import fr.elpine.astre.metier.objet.Intervenant;
import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.Module;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;


public class StageGeneration extends Stage implements Initializable
{
    //private Stage stage;
    private ObservableList<Object> ens;

    @FXML
    private TableView<Object>tabGeneration;
    @FXML
    private TableColumn<Object,String> g_1;
    @FXML
    private TableColumn<Object,Boolean>g_2;

    private String vue;


    public StageGeneration() // fxml -> "generation"
    {
        this.setTitle("Generation");
        this.setMinWidth(1400);
        this.setMinHeight(600);


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
        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );
    }


    public void setVue(String vue) {
        if(vue.equals("intervenant"))
        {
            this.ens = FXCollections.observableArrayList(Controleur.get().getMetier().getIntervenants());
            this.vue = "intervenant";
        }
        else if(vue.equals("module"))
        {
            ArrayList<Module> lst = new ArrayList<>();

            Controleur.get().getMetier().getAnneeActuelle().getSemestres().forEach(s -> lst.addAll(s.getModules()));

            this.ens = FXCollections.observableArrayList(lst);
            this.vue = "module";
        }

        this.g_2.setCellValueFactory(cellData -> new SimpleBooleanProperty(false));
        g_2.setCellFactory(CheckBoxTableCell.forTableColumn(g_2));


        if (this.vue.equals("intervenant"))
        {
            this.g_1.setCellValueFactory( cellData -> new SimpleStringProperty( ((Intervenant)cellData.getValue()).getNom() ) );
        }
        else if (this.vue.equals("module"))
        {
            this.g_1.setCellValueFactory( cellData -> new SimpleStringProperty( ((Module)cellData.getValue()).getCode() ) );
        }

        this.tabGeneration.setItems(this.ens);
        this.tabGeneration.setEditable(true);
    }

}
