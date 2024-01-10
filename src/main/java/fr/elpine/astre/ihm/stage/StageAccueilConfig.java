package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.objet.Affectation;
import fr.elpine.astre.metier.objet.CategorieHeure;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import fr.elpine.astre.metier.outil.Fraction;
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
import javafx.scene.control.cell.TextFieldTableCell;
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
                } else if (item != null && item.equals("🖉")) {
                    setTextFill(Color.BLUE);
                } else {
                    setTextFill(Color.BLACK);
                    setText("");
                }
            }
        });

        tcCodeInter.setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getCode()));

        tcNomInter.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        tcNomInter.setCellFactory(column -> {
            return new TableCell<>() {
                TextField textField = new TextField();
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
            };
        });

        tcHMaxInter.setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getNbHeureMaxDefault().toString()));
        tcHMaxInter.setCellFactory(column -> {
            return new TableCell<>() {
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
            };
        });


        tcHServInter.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNbHeureServiceDefault().toString()));
        tcHServInter.setCellFactory(column -> {
            return new TableCell<>() {
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
            };
        });

        tcRatioInter.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRatioTPDefault().toString()));
        tcRatioInter.setCellFactory(column -> {
            return new TableCell<>() {
                final TextField textField = new TextField();
                {
                    textField.setOnAction(event -> {
                        String newValue = textField.getText();
                        int index = getIndex();
                        if (index >= 0 && index < getTableView().getItems().size()) {
                            CategorieIntervenant afc = getTableView().getItems().get(index);
                            afc.setRatioTPDefault(Fraction.valueOf(newValue)); // Mettre à jour votre donnée
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
            };
        });

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
                } else if (item != null && item.equals("🖉")) {
                    setTextFill(Color.BLUE);
                } else {
                    setTextFill(Color.BLACK);
                    setText("");
                }
            }
        });

        tcNomHeures.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        tcEqtdHeures.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEquivalentTD().toString()));
        tcEqtdHeures.setCellFactory(column -> {
            return new TableCell<>() {
                final TextField textField = new TextField();
                {
                    textField.setOnAction(event -> {
                        String newValue = textField.getText();
                        int index = getIndex();
                        if (index >= 0 && index < getTableView().getItems().size()) {
                            CategorieHeure afc = getTableView().getItems().get(index);
                            afc.setEquivalentTD(Fraction.valueOf(newValue)); // Mettre à jour votre donnée
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
            };
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
            return "❌";
        } else if (categorieIntervenant.isAjoute()) {
            return "➕";
        } else if (categorieIntervenant.isModifie()) {
            return "🖉";
        } else {
            return "";
        }
    }

    private String getCellValue(CategorieHeure catHr) {
        if (catHr.isSupprime()) {
            return "❌";
        } else if (catHr.isAjoute()) {
            return "➕";
        } else if (catHr.isModifie()) {
            return "🖉";
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
}