package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.objet.CategorieHeure;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StageAccueilConfig extends Stage implements Initializable
{
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
    private TableColumn<CategorieIntervenant,String> tcHMaxInter;
    @FXML
    private TableColumn<CategorieIntervenant,String> tcHServInter;
    @FXML
    private TableColumn<CategorieIntervenant,String> tcRatioInter;

    @FXML
    private TableView<CategorieHeure>       tabCatHeures;

    @FXML
    private TableColumn<CategorieHeure,String >  tcNomHeures;
    @FXML
    private TableColumn<CategorieHeure,String > tcEqtdHeures;
    @FXML
    private TableColumn<CategorieHeure, Boolean> tcRessourcesHeures;
    @FXML
    private TableColumn<CategorieHeure,Boolean> tcSaeHeures;
    @FXML
    private TableColumn<CategorieHeure,Boolean> tcPppHeures;
    @FXML
    private TableColumn<CategorieHeure,Boolean> tcStageHeures;


    public StageAccueilConfig() // fxml -> "accueilConfig"
    {
        this.setTitle("Accueil Config");
        this.setMinWidth(850);
        this.setMinHeight(450);
    }


    /*public static Stage creer() throws IOException
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

    private void setStage(Stage stage) { this.stage = stage; }*/

    public void onBtnConfigBdd() {
        //desactiver();
        //StageInitBd.creer( this ).show();
        Stage stage = Manager.creer( "initDb", this );

        stage.showAndWait();
    }

    public void onBtnAjouter()
    {
        Stage stage = Manager.creer( "ajouterCategories", this );

        stage.showAndWait();

        this.refresh();
    }

    public void onBtnEnregistrer()
    {
        Controleur.get().getMetier().enregistrer();

        this.refresh();
        this.close();
    }

    public void onBtnAnnuler()
    {
        Controleur.get().getMetier().rollback();

        this.refresh();
        this.close();
    }

    public void onBtnSupprimer() throws IOException
    {
        CategorieIntervenant catInter = tabCatInter .getSelectionModel().getSelectedItem();
        CategorieHeure       catHr    = tabCatHeures.getSelectionModel().getSelectedItem();

        if (catInter != null)
        {
            if (catInter.supprimer( false ))
                PopUp.confirmationR("Suppression d'une catégorie d'intervenant", null, String.format("Êtes-vous sûr de vouloir supprimer cette catégorie d'intervenant : %s", catInter.getNom()));
            else
                PopUp.error("Catégorie utilisé quelque part",null, "La catégorie que vous voulez supprimer est utilisé quelque part. ");
        }
        else if (catHr != null) {
            if(catHr.supprimer( false ))
                PopUp.confirmationR("Suppression d'une catégorie d'heure", null, String.format("Êtes-vous sûr de vouloir supprimer cette catégorie d'heure : %s", catHr.getNom()));
            else
                PopUp.error("Catégorie utilisé quelque part",null, "La catégorie que vous voulez supprimer est utilisé quelque part. ").showAndWait();
        }

        tabCatHeures.getSelectionModel().clearSelection();
        tabCatInter.getSelectionModel().clearSelection();

        this.refresh();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

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

        tcCodeInter.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
        tcNomInter.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        tcHMaxInter.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNbHeureMaxDefault().toString()));
        tcHServInter.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNbHeureServiceDefault().toString()));
        tcRatioInter.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRatioTPDefault().toString()));

        tabCatInter.setItems( FXCollections.observableArrayList(Controleur.get().getMetier().getCategorieIntervenants()) );

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

        tcNomHeures.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        tcEqtdHeures.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEquivalentTD().toString()));
        tcRessourcesHeures.setCellValueFactory(cellData -> {
            CategorieHeure categorieHeure = cellData.getValue();
            BooleanProperty ressourceProperty = new SimpleBooleanProperty(categorieHeure.estRessource());
            // Ajoute un ChangeListener à la propriété ressource
            ressourceProperty.addListener((observable, oldValue, newValue) -> {
                enregistrerModification(categorieHeure, newValue, "Ressource");
            });
            return ressourceProperty;
        });

        tcRessourcesHeures.setCellFactory(CheckBoxTableCell.forTableColumn(tcRessourcesHeures));
        tcStageHeures.setCellValueFactory(cellData -> {
            CategorieHeure categorieHeure = cellData.getValue();
            BooleanProperty stageProperty = new SimpleBooleanProperty(categorieHeure.estStage());
            // Ajoute un ChangeListener à la propriété ressource
            stageProperty.addListener((observable, oldValue, newValue) -> {
                enregistrerModification(categorieHeure, newValue, "Stage");
            });
            return stageProperty;
        });

        tcStageHeures.setCellFactory(CheckBoxTableCell.forTableColumn(tcStageHeures));
        tcPppHeures.setCellValueFactory(cellData -> {
            CategorieHeure categorieHeure = cellData.getValue();
            BooleanProperty PppProperty = new SimpleBooleanProperty(categorieHeure.estPpp());
            // Ajoute un ChangeListener à la propriété ressource
            PppProperty.addListener((observable, oldValue, newValue) -> {
                enregistrerModification(categorieHeure, newValue, "Ppp");
            });
            return PppProperty;
        });

        tcPppHeures.setCellFactory(CheckBoxTableCell.forTableColumn(tcPppHeures));
        tcSaeHeures.setCellValueFactory(cellData -> {
            CategorieHeure categorieHeure = cellData.getValue();
            BooleanProperty saeProperty = new SimpleBooleanProperty(categorieHeure.estSae());
            // Ajoute un ChangeListener à la propriété ressource
            saeProperty.addListener((observable, oldValue, newValue) -> {
                enregistrerModification(categorieHeure, newValue, "Sae");
            });
            return saeProperty;
        });

        tcSaeHeures.setCellFactory(CheckBoxTableCell.forTableColumn(tcSaeHeures));

        tabCatHeures.setItems( FXCollections.observableArrayList(Controleur.get().getMetier().getCategorieHeures()) );
        tabCatHeures.setEditable(true);

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

    public void enregistrerModification(CategorieHeure categorieHeure, boolean b,String s)
    {
        switch (s)
        {
            case "Ressource" -> categorieHeure.setRessource(b);
            case "Sae"       -> categorieHeure.setSae      (b);
            case "Ppp"       -> categorieHeure.setPpp      (b);
            case "Stage"     -> categorieHeure.setStage    (b);
        }
    }

    private String getCellValue(CategorieIntervenant categorieIntervenant)
    {
        if (categorieIntervenant.isSupprime()) {
            return "❌";
        } else if (categorieIntervenant.isAjoute()) {
            return "➕";
        } else {
            return "";
        }
    }

    private String getCellValue(CategorieHeure catHr)
    {
        if (catHr.isSupprime()) {
            return "❌";
        } else if (catHr.isAjoute()) {
            return "➕";
        } else {
            return "";
        }
    }

    public void refresh()
    {
        tabCatInter .setItems( FXCollections.observableArrayList(Controleur.get().getMetier().getCategorieIntervenants()) );
        tabCatHeures.setItems( FXCollections.observableArrayList(Controleur.get().getMetier().getCategorieHeures()) );

        tabCatInter .refresh();
        tabCatHeures.refresh();
    }
}