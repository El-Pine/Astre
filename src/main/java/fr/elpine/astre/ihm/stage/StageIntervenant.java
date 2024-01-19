package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.ihm.outil.Emoji;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import fr.elpine.astre.metier.objet.Intervenant;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StageIntervenant extends Stage implements Initializable
{
	@FXML private TableView<Intervenant> tabAffInter;

	@FXML private TableColumn<Intervenant,String> tcS1;
	@FXML private TableColumn<Intervenant,String> tcS3;
	@FXML private TableColumn<Intervenant,String> tcS5;
	@FXML private TableColumn<Intervenant,String> tcTotImpair;
	@FXML private TableColumn<Intervenant,String> tcS2;
	@FXML private TableColumn<Intervenant,String> tcS4;
	@FXML private TableColumn<Intervenant,String> tcS6;
	@FXML private TableColumn<Intervenant,String> tcTotPair;
	@FXML private TableColumn<Intervenant,String> tcTot;

	@FXML
	private TableColumn<Intervenant,String> tcAjout;
	@FXML
	private TableColumn<Intervenant,String> tcNom;
	@FXML
	private TableColumn<Intervenant, String> tcPrenom;
	@FXML
	private TableColumn<Intervenant,String> tcMail;
	@FXML
	private TableColumn<Intervenant, String> tcCategorie;
	@FXML
	private TableColumn<Intervenant, String> tcHServ;
	@FXML
	private TableColumn<Intervenant, String> tcHMax;
	@FXML
	private TableColumn<Intervenant, String> tcRatioTP;

	@FXML
	private TextField txtFieldRecherche;


	public StageIntervenant() // fxml -> "intervenant"
	{
		this.setTitle("Intervenants");

		this.setMinWidth(1250);
		this.setMinHeight(600);
	}

	@FXML private void onBtnClickEnregistrer()
	{
		Controleur.get().getMetier().enregistrer();
		this.close();
	}

	@FXML private void onBtnClickAnnuler() {
		Controleur.get().getMetier().rollback();
		this.close();
	}

	@FXML private void onBtnClickAjouter()
	{
		Stage stage = Manager.creer("saisieIntervenant", this);

        assert stage != null;
        stage.showAndWait();
		this.refresh();
	}

	@FXML private void onBtnClickSupprimer() {
		Intervenant inter = tabAffInter.getSelectionModel().getSelectedItem();

		if (PopUp.confirmationR("Suppression d'un intervenant", null, String.format("Êtes-vous sûr de vouloir supprimer l'intervenant : %s %s", inter.getNom(), inter.getPrenom())))
			if (!inter.supprimer(false))
				PopUp.warning("Suppression impossible", null, "L'intervenant '%s %s' est lié à un module, il n'est donc pas possible de le supprimer.".formatted(inter.getNom(), inter.getPrenom())).showAndWait();

		this.refresh();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.setWidth( this.getMinWidth() );
		this.setHeight( this.getMinHeight() );

		tcAjout.setCellValueFactory(cellData -> new SimpleStringProperty(getCellValue(cellData.getValue())));
		tcAjout.setCellFactory(column -> Emoji.getCellFactory());

		tcCategorie.setCellValueFactory(cellData -> new SimpleStringProperty (cellData.getValue().getCategorie()  .getCode  ()));

		ArrayList<CategorieIntervenant> ensCatInter = Controleur.get().getMetier().getCategorieIntervenants();
		ArrayList<String> ensNomCatInter = new ArrayList<>();
		for ( CategorieIntervenant catInter : ensCatInter)
			ensNomCatInter.add(catInter.getCode());

		tcCategorie.setCellFactory(column -> new TableCell<>() {
            final ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(ensNomCatInter));
            {
                comboBox.setOnAction(event -> {
                    String newValue = comboBox.getValue();
                    String oldValue = getItem();

                    Intervenant afc = getTableView().getItems().get(getIndex());
                    if(newValue != null && !newValue.equals(oldValue))
                    {
                        afc.setCategorie(Astre.rechercherCatInter(ensCatInter, newValue));
                    }

                    tabAffInter.refresh();
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

		tcNom      .setCellValueFactory(cellData -> new SimpleStringProperty (cellData.getValue().getNom     ()));
		tcNom.setCellFactory(column -> new TableCell<>() {
            final TextField textField = new TextField();
            {
                creerFormatter(textField,"^[a-zA-ZÀ-ÖØ-öø-ÿ\\s]+(\\(\\d+\\))?$");

                textField.setOnAction(event -> {
                    String newValue = textField.getText();
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Intervenant afc = getTableView().getItems().get(index);
                        afc.setNom(newValue); // Mettre à jour votre donnée
                        tabAffInter.refresh();
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

		tcPrenom   .setCellValueFactory(cellData -> new SimpleStringProperty (cellData.getValue().getPrenom  ()));
		tcPrenom.setCellFactory(column -> new TableCell<>() {
            final TextField textField = new TextField();
            {
                creerFormatter(textField,"^[a-zA-ZÀ-ÖØ-öø-ÿ]+$");

                textField.setOnAction(event -> {
                    String newValue = textField.getText();
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Intervenant afc = getTableView().getItems().get(index);
                        afc.setPrenom(newValue); // Mettre à jour votre donnée
                        tabAffInter.refresh();
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

		tcMail     .setCellValueFactory(cellData -> new SimpleStringProperty (cellData.getValue().getMail     ()));
		tcMail.setCellFactory(column -> new TableCell<>() {
            final TextField textField = new TextField();

            {
                textField.setOnAction(event -> {
                    String newValue = textField.getText();
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Intervenant afc = getTableView().getItems().get(index);
                        boolean updateSuccess = afc.setMail(newValue); // Mettre à jour votre donnée
                        if (updateSuccess) {
                            tabAffInter.refresh();
                        } else {
                            textField.setText(getItem()); // Réinitialise le texte avec la valeur précédente
                        }
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

		tcHServ    .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHeureService().toString() ));
		tcHServ.setCellFactory(column -> new TableCell<>() {
            final TextField textField = new TextField();
            {
                creerFormatter(textField,"^\\d+$");

                textField.setOnAction(event -> {
                    String newValue = textField.getText();
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Intervenant afc = getTableView().getItems().get(index);
                        afc.setHeureService(Fraction.valueOf(newValue)); // Mettre à jour votre donnée
                        tabAffInter.refresh();
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

		tcHMax     .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHeureMax().toString() ));
		tcHMax.setCellFactory(column -> new TableCell<>() {
            final TextField textField = new TextField();
            {
                creerFormatter(textField,"^\\d+$");

                textField.setOnAction(event -> {
                    String newValue = textField.getText();
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Intervenant afc = getTableView().getItems().get(index);
                        afc.setHeureMax(Fraction.valueOf(newValue)); // Mettre à jour votre donnée
                        tabAffInter.refresh();
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

		tcRatioTP  .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRatioTP  ().toString() ));
		tcRatioTP.setCellFactory(column -> new TableCell<>() {
            final TextField textField = new TextField();
            {
                textField.setOnAction(event -> {
                    String newValue = textField.getText();
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Intervenant afc = getTableView().getItems().get(index);
                        afc.setRatioTP(Fraction.valueOf(newValue)); // Mettre à jour votre donnée
                        tabAffInter.refresh();
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

		tcS1       .setCellValueFactory(cellData -> new SimpleStringProperty(Fraction.simplifyDouble(cellData.getValue().getHeure( true ).get(1), true)));
		tcS2       .setCellValueFactory(cellData -> new SimpleStringProperty(Fraction.simplifyDouble(cellData.getValue().getHeure( true ).get(2), true)));
		tcS3       .setCellValueFactory(cellData -> new SimpleStringProperty(Fraction.simplifyDouble(cellData.getValue().getHeure( true ).get(3), true)));
		tcS4       .setCellValueFactory(cellData -> new SimpleStringProperty(Fraction.simplifyDouble(cellData.getValue().getHeure( true ).get(4), true)));
		tcS5       .setCellValueFactory(cellData -> new SimpleStringProperty(Fraction.simplifyDouble(cellData.getValue().getHeure( true ).get(5), true)));
		tcS6       .setCellValueFactory(cellData -> new SimpleStringProperty(Fraction.simplifyDouble(cellData.getValue().getHeure( true ).get(6), true)));

		tcTotImpair.setCellValueFactory(cellData -> {
			ArrayList<Double> h = cellData.getValue().getHeure( true );
			double s = h.get(1) + h.get(3) + h.get(5);
			return new SimpleStringProperty(Fraction.simplifyDouble(s, true));
		});

		tcTotPair  .setCellValueFactory(cellData -> {
			ArrayList<Double> h = cellData.getValue().getHeure( true );
			double s = h.get(2) + h.get(4) + h.get(6);
			return new SimpleStringProperty(Fraction.simplifyDouble(s, true));
		});

		tcTot      .setCellValueFactory(cellData -> {
			ArrayList<Double> h = cellData.getValue().getHeure( true );
			double s = h.get(1) + h.get(2) + h.get(3) + h.get(4) + h.get(5) + h.get(6);
			return new SimpleStringProperty(Fraction.simplifyDouble(s, true));
		});

        ContextMenu contextMenu = new ContextMenu();
        MenuItem duplicateItem = new MenuItem("Dupliquer");

        duplicateItem.setOnAction(event -> {
            Intervenant sltItem = tabAffInter.getSelectionModel().getSelectedItem();
            if (sltItem != null) {
                boolean nomCorrect;
                String nomTemp;
                int cpt = 1;
                do {
                    nomCorrect = true;
                    nomTemp = sltItem.getNom() + " (" + cpt + ")";
                    for (Intervenant inter : Controleur.get().getMetier().getIntervenants())
                        if ( inter.getNom().equals(nomTemp) ) {
                            nomCorrect = false;
                            break;
                        }
                    cpt++;
                }while(!nomCorrect);
                Intervenant duplicatedItem = new Intervenant(nomTemp,sltItem.getPrenom(),sltItem.getMail(),sltItem.getCategorie(),sltItem.getHeureService(),sltItem.getHeureMax(),sltItem.getRatioTP()); // Cloner l'élément sélectionné
                tabAffInter.getItems().add(duplicatedItem);
                tabAffInter.refresh();
            } else {
                PopUp.error("Duplication de duplication",null,"Vous ne pouvez pas dupliquer une ligne qui n'as pas été modifié depuis sa duplication");
            }
        });

        contextMenu.getItems().add(duplicateItem);

        tabAffInter.setOnContextMenuRequested(event -> {
            Intervenant sltItem = tabAffInter.getSelectionModel().getSelectedItem();
            if  (!sltItem.getNom().contains("("))
            	contextMenu.show(tabAffInter, event.getScreenX(), event.getScreenY());
        });

        tabAffInter.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                contextMenu.hide();
            }
        });

		this.refresh();
	}

	private String getCellValue(Intervenant intervenant)
	{
		String warn = intervenant.estIntervenantInvalide() ? "W" : "";

		if (intervenant.isSupprime()) {
			return "S" + warn;
		} else if (intervenant.isAjoute()) {
			return "A" + warn;
		} else if (intervenant.isModifie()) {
			return "M" + warn;
		} else {
			return warn;
		}
	}

	public void refresh() {
		ObservableList<Intervenant> list = FXCollections.observableArrayList( Controleur.get().getMetier().getIntervenants() );

		txtFieldRecherche.clear();
		tabAffInter.setItems(list);
		tabAffInter.refresh();
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

	@FXML private void onBtnRechercher()
	{
		String recherche = txtFieldRecherche.getText();
		ObservableList<Intervenant> ensInter = FXCollections.observableList(Controleur.get().getMetier().rechercheIntervenantByText(recherche));
		tabAffInter.setItems(ensInter);
	}
}