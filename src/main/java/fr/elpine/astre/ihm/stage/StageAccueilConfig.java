package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.CategorieHeure;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Popup;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StageAccueilConfig implements Initializable
{
    private Stage stage;
    @FXML
    private TableView<CategorieIntervenant> tabCatInter;
    @FXML
    private TableColumn<CategorieIntervenant,String> tcCodeInter;
    @FXML
    private TableColumn<CategorieIntervenant,String> tcNomInter;
    @FXML
    private TableColumn<CategorieIntervenant,Integer> tcHMaxInter;
    @FXML
    private TableColumn<CategorieIntervenant,Integer> tcHServInter;
    @FXML
    private TableColumn<CategorieIntervenant,Double> tcRatioInter;

    @FXML
    private TableView<CategorieHeure>       tabCatHeures;

    @FXML
    private TableColumn<CategorieHeure,String >  tcNomHeures;
    @FXML
    private TableColumn<CategorieHeure,Double > tcEqtdHeures;
    @FXML
    private TableColumn<CategorieHeure,Boolean> tcRessourcesHeures;
    @FXML
    private TableColumn<CategorieHeure,Boolean> tcSaeHeures;
    @FXML
    private TableColumn<CategorieHeure,Boolean> tcPppHeures;
    @FXML
    private TableColumn<CategorieHeure,Boolean> tcStageHeures;

    @FXML
    private Label lblErreur;

    public static ObservableList<CategorieHeure> ensCatHeure;
    public static ObservableList<CategorieIntervenant> ensCatInter;


    public static Stage creer() throws IOException
    {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("accueilConfig.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 700, 400);

        StageAccueilConfig stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) stageCtrl.setStage(stage);

        stage.setTitle("Accueil Config");
        stage.setScene(scene);
        //stageCtrl.majTableauCatInter();

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

    public void onBtnAjouter(ActionEvent actionEvent) throws IOException
    {
        desactiver();
        StageAjouterCategories.creer( this ).show();
    }

    public void onBtnSupprimer(ActionEvent e) throws IOException
    {
        boolean estSupprimer = true;
        CategorieIntervenant catInter = tabCatInter .getSelectionModel().getSelectedItem();
        CategorieHeure       catHr    = tabCatHeures.getSelectionModel().getSelectedItem();

        if(catInter != null)
            estSupprimer = Controleur.get().getDb().supprimerCatIntervenant(catInter);
        if(catHr != null)
            estSupprimer = Controleur.get().getDb().supprimerCategorieHeure(catHr);

        if(!estSupprimer)
        {
            lblErreur.setText("La catégorie est affectées quelque part");
            Controleur.get().getDb().reloadDB();
        }
        else
        {
            lblErreur.setText("");
        }



        this.refreshCatInter();
        this.refreshCatHr   ();
    }

    public void desactiver()
    {
        this.stage.getScene().lookup("#btnConfigBd").setDisable(true);
        this.stage.getScene().lookup("#btnAjouter").setDisable(true);
        this.stage.getScene().lookup("#btnSupprimer").setDisable(true);
    }

    public void activer() {
        this.stage.getScene().lookup("#btnConfigBd" ).setDisable(false);
        this.stage.getScene().lookup("#btnAjouter"  ).setDisable(false);
        this.stage.getScene().lookup("#btnSupprimer").setDisable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        tcCodeInter      .setCellValueFactory (cellData -> new SimpleStringProperty (cellData.getValue().getCode      ()));
        tcNomInter       .setCellValueFactory (cellData -> new SimpleStringProperty (cellData.getValue().getNom       ()));
        tcHMaxInter      .setCellValueFactory (cellData -> new SimpleIntegerProperty(cellData.getValue().getNbHeureMax()).asObject());
        tcHServInter     .setCellValueFactory (cellData -> new SimpleIntegerProperty(cellData.getValue().getService   ()).asObject());
        tcRatioInter     .setCellValueFactory (cellData -> new SimpleDoubleProperty (cellData.getValue().getRatioTd   ()).asObject());

        ObservableList ensCatInter = FXCollections.observableArrayList(Controleur.get().getDb().getAllCategorieIntervenant());
        tabCatInter.setItems(ensCatInter);

        tcNomHeures        .setCellValueFactory (cellData -> new SimpleStringProperty  (cellData.getValue().getNom         ()));
        tcEqtdHeures       .setCellValueFactory (cellData -> new SimpleDoubleProperty  (cellData.getValue().getEquivalentTD()).asObject());
        tcRessourcesHeures .setCellValueFactory (cellData -> new SimpleBooleanProperty (cellData.getValue().estRessource   ()));
        tcSaeHeures        .setCellValueFactory (cellData -> new SimpleBooleanProperty (cellData.getValue().estSae         ()));
        tcPppHeures        .setCellValueFactory (cellData -> new SimpleBooleanProperty (cellData.getValue().estPpp         ()));
        tcStageHeures      .setCellValueFactory (cellData -> new SimpleBooleanProperty (cellData.getValue().estStage       ()));

        ObservableList ensCatHeure = FXCollections.observableArrayList(Controleur.get().getDb().getAllCategorieHeure());
        tabCatHeures.setItems(ensCatHeure);
    }

    public void refreshCatHr() {
        StageAccueilConfig.ensCatHeure = FXCollections.observableArrayList(Controleur.get().getDb().getAllCategorieHeure());
        tabCatHeures.setItems(StageAccueilConfig.ensCatHeure);
    }

    public void refreshCatInter() {
        StageAccueilConfig.ensCatInter = FXCollections.observableArrayList(Controleur.get().getDb().getAllCategorieIntervenant());
        tabCatInter.setItems(StageAccueilConfig.ensCatInter);
    }


}