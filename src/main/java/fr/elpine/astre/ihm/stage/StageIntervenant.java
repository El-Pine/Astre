package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.metier.objet.Intervenant;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StageIntervenant extends Stage implements Initializable
{
	@FXML
	private TableView<Intervenant> tabAffInter;

	@FXML
	private TableColumn<Intervenant,String> tcS1;
	@FXML
	private TableColumn<Intervenant,String> tcS3;
	@FXML
	private TableColumn<Intervenant,String> tcS5;
	@FXML
	private TableColumn<Intervenant,String> tcTotImpair;
	@FXML
	private TableColumn<Intervenant,String> tcS2;
	@FXML
	private TableColumn<Intervenant,String> tcS4;
	@FXML
	private TableColumn<Intervenant,String> tcS6;
	@FXML
	private TableColumn<Intervenant,String> tcTotPair;
	@FXML
	private TableColumn<Intervenant,String> tcTot;

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

	@FXML
	protected void onBtnClickEnregistrer()
	{
		Controleur.get().getMetier().enregistrer();
		this.close();
	}

	@FXML
	protected void onBtnClickAnnuler() {
		Controleur.get().getMetier().rollback();
		this.close();
	}

	@FXML
	protected void onBtnClickAjouter()
	{
		Stage stage = Manager.creer("saisieIntervenant", this);

		stage.showAndWait();
		this.refresh();
	}

	@FXML
	protected void onBtnClickSupprimer() {
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
		tcAjout.setCellFactory(column -> new TableCell<>() {
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

		tcCategorie.setCellValueFactory(cellData -> new SimpleStringProperty (cellData.getValue().getCategorie()  .getCode  ()));
		tcNom      .setCellValueFactory(cellData -> new SimpleStringProperty (cellData.getValue().getNom     ()));
		tcPrenom   .setCellValueFactory(cellData -> new SimpleStringProperty (cellData.getValue().getPrenom  ()));
		tcMail     .setCellValueFactory(cellData -> new SimpleStringProperty (cellData.getValue().getMail     ()));
		tcHServ    .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHeureService().toString() ));
		tcHMax     .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHeureMax().toString() ));
		tcRatioTP  .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRatioTP  ().toString() ));

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

		this.refresh();
	}

	private String getCellValue(Intervenant intervenant) {
		if (intervenant.isSupprime()) {
			return "❌";
		} else if (intervenant.isAjoute()) {
			return "➕";
		} else {
			return "";
		}
	}

	public void refresh() {
		ObservableList<Intervenant> list = FXCollections.observableArrayList( Controleur.get().getMetier().getIntervenants() );

		txtFieldRecherche.clear();
		tabAffInter.setItems(list);
		tabAffInter.refresh();
	}

	public void onBtnRechercher()
	{
		String recherche = txtFieldRecherche.getText();
		ObservableList<Intervenant> ensInter = FXCollections.observableList(Controleur.get().getMetier().rechercheIntervenantByText(recherche));
		tabAffInter.setItems(ensInter);
	}
}