package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.stage.PopUp.StagePopUp;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.objet.CategorieHeure;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import fr.elpine.astre.metier.objet.Intervenant;
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
import javafx.scene.control.*;
import javafx.scene.paint.Color;
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
    private TableColumn<CategorieIntervenant,String> tcInter;
    @FXML
    private TableColumn<CategorieHeure ,String> tcHr;
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

    public static ArrayList<CategorieHeure>       catHeurAAjouter;
    public static ArrayList<CategorieIntervenant> categorieInterAAjouter;

    public static ArrayList<CategorieHeure>       catHrASupp;
    public static ArrayList<CategorieIntervenant> catInterASuppr;


    public static Stage creer() throws IOException
    {
        Stage stage = new Stage();

        AstreApplication.refreshIcon(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("accueilConfig.fxml"));
        StageAccueilConfig.ensCatHeure = FXCollections.observableArrayList(Controleur.get().getMetier().getCategorieHeures());
        StageAccueilConfig.ensCatInter = FXCollections.observableArrayList(Controleur.get().getMetier().getCategorieIntervenants());

        StageAccueilConfig.catHeurAAjouter = new ArrayList<>();
        StageAccueilConfig.categorieInterAAjouter = new ArrayList<>();

        StageAccueilConfig.catHrASupp     = new ArrayList<>();
        StageAccueilConfig.catInterASuppr = new ArrayList<>();

        Scene scene = new Scene(fxmlLoader.load(), 850, 450);

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
        boolean estSupprimer          = false;
        CategorieIntervenant catInter = tabCatInter .getSelectionModel().getSelectedItem();
        CategorieHeure       catHr    = tabCatHeures.getSelectionModel().getSelectedItem();

        if(catInter != null)
        {
            estSupprimer = Astre.estCatInter(Controleur.get().getMetier().getCategorieIntervenants(), catInter.getCode());
            if(estSupprimer)
            {
                if (StagePopUp.PopUpConfirmation("Suppression d'une catégorie d'intervenant", "Êtes- vous sûr de vouloir supprimer cette catégorie d'intervenant : " + catInter.getNom()));
                {
                    System.out.println("Avant le remove : " + StageAccueilConfig.ensCatInter);
                    StageAccueilConfig.ensCatInter.remove(catInter);
                    System.out.println("Apres le remove : " + StageAccueilConfig.ensCatInter);
                    StageAccueilConfig.catInterASuppr.add(catInter);
                }

            }
        }
        if(catHr != null)
        {
            estSupprimer = Astre.estCatHr(Controleur.get().getMetier().getCategorieHeures(), catHr.getNom());
            if(estSupprimer)
            {
                if (StagePopUp.PopUpConfirmation("Suppression d'une catégorie d'heure", "Êtes- vous sûr de vouloir supprimer cette catégorie d'heure : " + catHr.getNom()))
                {
                    StageAccueilConfig.ensCatHeure.remove(catHr);
                    StageAccueilConfig.catHrASupp .add   (catHr);
                }

            }
        }

        tabCatHeures.getSelectionModel().clearSelection();
        tabCatInter.getSelectionModel().clearSelection();
        this.refresh();
    }

    public void desactiver()
    {
        this.stage.getScene().lookup("#btnConfigBd") .setDisable(true);
        this.stage.getScene().lookup("#btnAjouter")  .setDisable(true);
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
        tcInter.setCellValueFactory(cellData -> new SimpleStringProperty(getCellValue(cellData.getValue())));
        tcInter.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);

                if (item != null && item.equals("❌")) {
                    setTextFill(Color.RED);
                } else if (item != null && item.equals("➕")) {
                    setTextFill(Color.LIGHTGREEN);
                } else {
                    setTextFill(Color.BLACK);
                    setText("");
                }
            }
        });

        tcCodeInter      .setCellValueFactory (cellData -> new SimpleStringProperty (cellData.getValue().getCode      ()));
        tcNomInter       .setCellValueFactory (cellData -> new SimpleStringProperty (cellData.getValue().getNom       ()));
        tcHMaxInter      .setCellValueFactory (cellData -> new SimpleIntegerProperty(cellData.getValue().getNbHeureMaxDefault()).asObject());
        tcHServInter     .setCellValueFactory (cellData -> new SimpleIntegerProperty(cellData.getValue().getNbHeureServiceDefault   ()).asObject());
        tcRatioInter     .setCellValueFactory (cellData -> new SimpleDoubleProperty (cellData.getValue().getRatioTPDefault  ()).asObject());

        tabCatInter.setItems(ensCatInter);

        tcHr.setCellValueFactory(cellData -> new SimpleStringProperty(getCellValue(cellData.getValue())));
        tcHr.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);

                if (item != null && item.equals("❌")) {
                    setTextFill(Color.RED);
                } else if (item != null && item.equals("➕")) {
                    setTextFill(Color.LIGHTGREEN);
                } else {
                    setTextFill(Color.BLACK);
                    setText("");
                }
            }
        });

        tcNomHeures        .setCellValueFactory (cellData -> new SimpleStringProperty  (cellData.getValue().getNom         ()));
        tcEqtdHeures       .setCellValueFactory (cellData -> new SimpleDoubleProperty  (cellData.getValue().getEquivalentTD()).asObject());
        tcRessourcesHeures .setCellValueFactory (cellData -> new SimpleBooleanProperty (cellData.getValue().estRessource   ()));
        tcSaeHeures        .setCellValueFactory (cellData -> new SimpleBooleanProperty (cellData.getValue().estSae         ()));
        tcPppHeures        .setCellValueFactory (cellData -> new SimpleBooleanProperty (cellData.getValue().estPpp         ()));
        tcStageHeures      .setCellValueFactory (cellData -> new SimpleBooleanProperty (cellData.getValue().estStage       ()));

        tabCatHeures.setItems(ensCatHeure);

        tabCatInter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tabCatHeures.getSelectionModel().clearSelection();
            }
        });

        tabCatHeures.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tabCatInter.getSelectionModel().clearSelection();
            }
        });
    }
    private String getCellValue(CategorieIntervenant categorieIntervenant)
    {
        if (StageAccueilConfig.categorieInterAAjouter.contains(categorieIntervenant)) {
            return "➕";
        } else if (StageAccueilConfig.catInterASuppr.contains(categorieIntervenant)) {
            return "❌";
        } else {
            return "";
        }
    }

    private String getCellValue(CategorieHeure catHr)
    {
        if (StageAccueilConfig.catHeurAAjouter.contains(catHr) ) {
            return "➕";
        } else if (StageAccueilConfig.catHrASupp.contains(catHr)) {
            return "❌";
        } else {
            return "";
        }
    }

    public void refresh()
    {
        System.out.println("Debut refresh " + StageAccueilConfig.ensCatInter );
        ObservableList<CategorieIntervenant> list1 = FXCollections.observableArrayList(StageAccueilConfig.ensCatInter);
        ObservableList<CategorieHeure>       list2 = FXCollections.observableArrayList(StageAccueilConfig.ensCatHeure);


        list1.addAll(StageAccueilConfig.categorieInterAAjouter);
        list1.addAll(StageAccueilConfig.catInterASuppr        );

        list2.addAll(StageAccueilConfig.catHeurAAjouter);
        list2.addAll(StageAccueilConfig.catHrASupp     );

        System.out.println("Dans refresh : "+StageAccueilConfig.ensCatInter);
        System.out.println(StageAccueilConfig.catHrASupp);


        tabCatInter .setItems (list1);
        tabCatHeures.setItems (list2);

        tabCatInter .refresh();
        tabCatHeures.refresh();
    }
}