package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.ihm.outil.Emoji;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.objet.CategorieHeure;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import fr.elpine.astre.metier.outil.Configuration;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class StageAccueilConfig extends Stage implements Initializable
{
    @FXML private Label lblState;
    @FXML private Label lblIp;
    @FXML private Label lblPort;
    @FXML private Label lblIdentifiant;
    @FXML private Label lblBase;
    @FXML private ToggleGroup theme;
    @FXML private ImageView imgSombre;
    @FXML private ImageView imgClair;
    @FXML private ImageView imgViolet;

    @FXML private TableView<CategorieIntervenant> tabCatInter;
    @FXML private TableColumn<CategorieIntervenant,String> tcInter;
    @FXML private TableColumn<CategorieHeure ,String> tcHr;
    @FXML private TableColumn<CategorieIntervenant,String> tcCodeInter;
    @FXML private TableColumn<CategorieIntervenant,String> tcNomInter;
    @FXML private TableColumn<CategorieIntervenant,String> tcHMaxInter;
    @FXML private TableColumn<CategorieIntervenant,String> tcHServInter;
    @FXML private TableColumn<CategorieIntervenant,String> tcRatioInter;

    @FXML private TableView<CategorieHeure>       tabCatHeures;
    @FXML private TableColumn<CategorieHeure,String >  tcNomHeures;
    @FXML private TableColumn<CategorieHeure,String > tcEqtdHeures;
    @FXML private TableColumn<CategorieHeure, Boolean> tcRessourcesHeures;
    @FXML private TableColumn<CategorieHeure,Boolean> tcSaeHeures;
    @FXML private TableColumn<CategorieHeure,Boolean> tcPppHeures;
    @FXML private TableColumn<CategorieHeure,Boolean> tcStageHeures;
    @FXML private TableColumn<CategorieHeure,Boolean> tcHebdo;
    @FXML private TableColumn<CategorieHeure,String> tcTypeGroupe;


    public StageAccueilConfig() // fxml -> "accueilConfig"
    {
        this.setTitle("Paramètres");
        this.setMinWidth(900);
        this.setMinHeight(500);
    }

    @FXML private void onBtnConfigBdd() {
        //desactiver();
        //StageInitBd.creer( this ).show();

        Stage stage = Manager.creer( "initDb", this );

        assert stage != null;
        stage.showAndWait();

        initDBMessage();
    }

    @FXML private void onBtnAjouterInter()
    {
        Stage stage = Manager.creer( "ajouterCategorieInter", this );

        assert stage != null;
        stage.showAndWait();

        this.refresh();
    }

    @FXML private void onBtnAjouterHeure()
    {
        Stage stage = Manager.creer( "ajouterCategorieHeure", this );

        assert stage != null;
        stage.showAndWait();

        this.refresh();
    }

    @FXML private void onBtnEnregistrer()
    {
        Controleur.get().getMetier().enregistrer();
        this.refresh();
        this.close();
    }

    @FXML private void onBtnAnnuler()
    {
        Controleur.get().getMetier().rollback();

        this.refresh();
        this.close();
    }

    @FXML private void onBtnSupprimer()
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
        tcInter.setCellFactory(column -> Emoji.getCellFactory());

        tcCodeInter.setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getCode()));

        tcNomInter.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        tcNomInter.setCellFactory(column -> new TableCell<>() {
            final TextField textField = new TextField();
            {
                creerFormatter(textField,"^[a-zA-ZÀ-ÖØ-öø-ÿ]+$");

                textField.setOnAction(event -> {
                    String newValue = textField.getText();
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        CategorieIntervenant afc = getTableView().getItems().get(index);
                        afc.setNom(newValue); // Mettre à jour votre donnée
                        tabCatInter.refresh();
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(null); // Assurez-vous que le texte dans la cellule de la TableView est vide
                    textField.setText(item); // Définit le texte dans le TextField
                    setGraphic(textField);
                }
            }
        });

        tcHMaxInter.setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getNbHeureMaxDefault().toString()));
        tcHMaxInter.setCellFactory(column -> new TableCell<>() {
            final TextField textField = new TextField();
            {
                creerFormatter(textField,"^\\d+$");

                textField.setOnAction(event -> {
                    String newValue = textField.getText();
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        CategorieIntervenant afc = getTableView().getItems().get(index);
                        afc.setNbHeureMaxDefault(Fraction.valueOf(newValue)); // Mettre à jour votre donnée
                        tabCatInter.refresh();
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(null); // Assurez-vous que le texte dans la cellule de la TableView est vide
                    textField.setText(item); // Définit le texte dans le TextField
                    setGraphic(textField);
                }
            }
        });


        tcHServInter.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNbHeureServiceDefault().toString()));
        tcHServInter.setCellFactory(column -> new TableCell<>() {
            final TextField textField = new TextField();
            {
                creerFormatter(textField,"^\\d+$");

                textField.setOnAction(event -> {
                    String newValue = textField.getText();
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        CategorieIntervenant afc = getTableView().getItems().get(index);
                        afc.setNbHeureServiceDefault(Fraction.valueOf(newValue)); // Mettre à jour votre donnée
                        tabCatInter.refresh();
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(null); // Assurez-vous que le texte dans la cellule de la TableView est vide
                    textField.setText(item); // Définit le texte dans le TextField
                    setGraphic(textField);
                }
            }
        });

        tcRatioInter.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRatioTPDefault().toString()));
        tcRatioInter.setCellFactory(column -> new TableCell<>() {
            final TextField textField = new TextField();
            {
                textField.setOnAction(event -> {
                    String newValue = textField.getText();
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        CategorieIntervenant afc = getTableView().getItems().get(index);
                        Fraction f = Fraction.valueOf(newValue);
                        if (f != null) afc.setRatioTPDefault(f); // Mettre à jour votre donnée
                        tabCatInter.refresh();
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(null); // Assurez-vous que le texte dans la cellule de la TableView est vide
                    textField.setText(item); // Définit le texte dans le TextField
                    setGraphic(textField);
                }
            }
        });

        tabCatInter.setItems( FXCollections.observableArrayList(Controleur.get().getMetier().getCategorieIntervenants()) );

        tcHr.setCellValueFactory(cellData -> new SimpleStringProperty(getCellValue(cellData.getValue())));
        tcHr.setCellFactory(column -> Emoji.getCellFactory());

        tcNomHeures.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        tcEqtdHeures.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEquivalentTD().toString()));
        tcEqtdHeures.setCellFactory(column -> new TableCell<>() {
            final TextField textField = new TextField();
            {
                textField.setOnAction(event -> {
                    String newValue = textField.getText();
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        CategorieHeure afc = getTableView().getItems().get(index);

                        Fraction f = Fraction.valueOf(newValue);
                        if (f != null) afc.setEquivalentTD(f); // Mettre à jour votre donnée
                        tabCatHeures.refresh();
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(null); // Assurez-vous que le texte dans la cellule de la TableView est vide
                    textField.setText(item); // Définit le texte dans le TextField
                    setGraphic(textField);
                }
            }
        });

        tcRessourcesHeures.setCellValueFactory(cellData -> {
            CategorieHeure categorieHeure = cellData.getValue();
            BooleanProperty ressourceProperty = new SimpleBooleanProperty(categorieHeure.estRessource());
            // Ajoute un ChangeListener à la propriété ressource
            ressourceProperty.addListener((observable, oldValue, newValue) -> {
                enregistrerModification(categorieHeure, newValue, "Ressource");
                tabCatHeures.refresh();
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
                tabCatHeures.refresh();
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
                tabCatHeures.refresh();
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
                tabCatHeures.refresh();
            });
            return saeProperty;
        });

        tcSaeHeures.setCellFactory(CheckBoxTableCell.forTableColumn(tcSaeHeures));
        tcHebdo.setCellValueFactory(cellData -> {
            CategorieHeure categorieHeure = cellData.getValue();
            BooleanProperty hebdoProperty = new SimpleBooleanProperty(categorieHeure.estHebdo());
            // Ajoute un ChangeListener à la propriété ressource
            hebdoProperty.addListener((observable, oldValue, newValue) -> {
                categorieHeure.setHebdo(newValue);
                tabCatHeures.refresh();
            });
            return hebdoProperty;
        });

        tcHebdo.setCellFactory(CheckBoxTableCell.forTableColumn(tcHebdo));

        tcTypeGroupe.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeGroupe().toUpperCase()));
        tcTypeGroupe.setCellFactory(column -> new TableCell<>() {
            final ChoiceBox<String> comboBox = new ChoiceBox<>(FXCollections.observableArrayList(Arrays.asList("TP", "TD", "CM")));
            {
                comboBox.setOnAction(event -> {
                    String newValue = comboBox.getValue();
                    String oldValue = getItem();

                    if (newValue != null && !newValue.equals(oldValue)) {
                        CategorieHeure cat = getTableView().getItems().get(getIndex());

                        cat.setTypeGroupe(newValue.toLowerCase());

                        tabCatHeures.refresh();
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    comboBox.setValue(item);
                    setGraphic(comboBox);
                }
            }
        });

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

        initDBMessage();

        this.imgClair.setImage(new Image(Objects.requireNonNull(AstreApplication.class.getResourceAsStream("theme_image/cupertino-light.png"))));
        this.imgSombre.setImage(new Image(Objects.requireNonNull(AstreApplication.class.getResourceAsStream("theme_image/cupertino-dark.png"))));
        this.imgViolet.setImage(new Image(Objects.requireNonNull(AstreApplication.class.getResourceAsStream("theme_image/dracula.png"))));

        String theme = Configuration.get("theme");

        if (theme != null)
        {
            int id = 0;

            if (theme.equals( "cupertino-light" )) id = 1;
            if (theme.equals( "dracula"         )) id = 2;

            this.theme.getToggles().get(id).setSelected(true);
        }
    }

    private void initDBMessage()
    {
        if (Controleur.get().getDb().getStatus()) {
            lblState.setText("Connecté");
            lblState.setTextFill(Color.LIGHTGREEN);
        } else {
            lblState.setText("Déconnecté");
            lblState.setTextFill(Color.RED);
        }

        String[] infos = DB.getInformations();

        if (infos != null) {
            this.lblIp.setText(infos[0]);
            this.lblPort.setText(infos[1]);
            this.lblBase.setText(infos[2]);
            this.lblIdentifiant.setText(infos[3]);
        }
    }

    public void enregistrerModification(CategorieHeure categorieHeure, boolean b,String s)
    {
        switch (s.toUpperCase())
        {
            case "RESSOURCE" -> categorieHeure.setRessource(b);
            case "SAE"       -> categorieHeure.setSae      (b);
            case "PPP"       -> categorieHeure.setPpp      (b);
            case "STAGE"     -> categorieHeure.setStage    (b);
        }
    }

    private String getCellValue(CategorieIntervenant categorieIntervenant)
    {
        if (categorieIntervenant.isSupprime()) {
            return "S";
        } else if (categorieIntervenant.isAjoute()) {
            return "A";
        } else if (categorieIntervenant.isModifie()) {
            return "M";
        } else {
            return "";
        }
    }

    private String getCellValue(CategorieHeure catHr) {
        if (catHr.isSupprime()) {
            return "S";
        } else if (catHr.isAjoute()) {
            return "A";
        } else if (catHr.isModifie()) {
            return "M";
        } else {
            return "";
        }
    }

    private void creerFormatter(TextField txtf, String regex) {
        txtf.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches(regex)) {
                return change;
            } else if (change.getText().isEmpty()) {
                return change;
            } else {
                return null;
            }
        }));
    }

    public void refresh()
    {
        tabCatInter .setItems( FXCollections.observableArrayList(Controleur.get().getMetier().getCategorieIntervenants()) );
        tabCatHeures.setItems( FXCollections.observableArrayList(Controleur.get().getMetier().getCategorieHeures()) );

        tabCatInter .refresh();
        tabCatHeures.refresh();
    }

    @FXML private void onThemeChange() {
        int selected = this.theme.getToggles().indexOf(this.theme.getSelectedToggle());

        switch (selected) {
            case 0  -> AstreApplication.setTheme("cupertino-dark");
            case 2  -> AstreApplication.setTheme("dracula");
            default -> AstreApplication.setTheme("cupertino-light");
        }
    }
}