package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.ihm.stage.PopUp.StagePopUp;
import fr.elpine.astre.metier.objet.Intervenant;
import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.beans.property.SimpleDoubleProperty;
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

	/*public static Stage creer() throws IOException
	{
		Stage stage = new Stage();

		AstreApplication.refreshIcon(stage);

		StageIntervenant.ensInter      = FXCollections.observableArrayList(Controleur.get().getMetier().getIntervenants());
		StageIntervenant.interAAjouter = new ArrayList<>();
		StageIntervenant.interAEnlever = new ArrayList<>();

		FXMLLoader fxmlLoader = new FXMLLoader(StageIntervenant.class.getResource("intervenant.fxml"));

		Scene scene = new Scene(fxmlLoader.load(), 1400, 600);

		StageIntervenant stageCtrl = fxmlLoader.getController();
		if (stageCtrl != null) stageCtrl.setStage(stage);

		stage.setTitle("Intervenants");
		stage.setScene(scene);

		stage.setOnCloseRequest(e -> {
			// perform actions before closing
			try { StagePrincipal.creer().show(); } catch (IOException ignored) {}
		});
		
		return stage;
	}

	private void setStage(Stage stage) { this.stage = stage; }*/

	@FXML
	protected void onBtnClickEnregistrer() throws IOException, SQLException
	{

		/*for ( Intervenant inter : this.interAAjouter )
		{
			Controleur.get().getMetier().ajouterIntervenant(inter);
		}
		for ( Intervenant inter : this.interAEnlever )
		{
			Controleur.get().getMetier().supprimerIntervenant(inter);
		}*/

		Controleur.get().getMetier().enregistrer();
		this.close();
		//StagePrincipal.creer().show();
	}

	@FXML
	protected void onBtnClickAnnuler() throws IOException {
		//this.interAAjouter = this.interAEnlever = new ArrayList<>();
		Controleur.get().getMetier().rollback();
		this.close();
		//StagePrincipal.creer().show();
	}

	@FXML
	protected void onBtnClickAjouter() throws IOException
	{
		//this.desactiver();
		//StageAjoutIntervenant.creer( this ).show();
		Stage stage = Manager.creer("saisieIntervenant", this);

		stage.showAndWait();
		this.refresh();
	}

	@FXML
	protected void onBtnClickSupprimer() throws IOException {
		Intervenant inter = tabAffInter.getSelectionModel().getSelectedItem();

		if (PopUp.confirmationR("Suppression d'un intervenant", null, String.format("Êtes-vous sûr de vouloir supprimer l'intervenant : %s %s", inter.getNom(), inter.getPrenom())))
			inter.supprimer(false);

		this.refresh();
	}

	/*public void desactiver()
	{
		this.getScene().lookup("#btnEnregistrer").setDisable(true);
		this.getScene().lookup("#btnAnnuler").setDisable(true);
		this.getScene().lookup("#btnAjouter").setDisable(true);
		this.getScene().lookup("#btnSupprimer").setDisable(true);
	}

	public void activer() {
		this.getScene().lookup("#btnEnregistrer").setDisable(false);
		this.getScene().lookup("#btnAnnuler").setDisable(false);
		this.getScene().lookup("#btnAjouter").setDisable(false);
		this.getScene().lookup("#btnSupprimer").setDisable(false);
	}*/

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

		tcS1       .setCellValueFactory(cellData -> new SimpleStringProperty(Fraction.simplifyDouble(cellData.getValue().getHeure().get(1), true)));
		tcS2       .setCellValueFactory(cellData -> new SimpleStringProperty(Fraction.simplifyDouble(cellData.getValue().getHeure().get(2), true)));
		tcS3       .setCellValueFactory(cellData -> new SimpleStringProperty(Fraction.simplifyDouble(cellData.getValue().getHeure().get(3), true)));
		tcS4       .setCellValueFactory(cellData -> new SimpleStringProperty(Fraction.simplifyDouble(cellData.getValue().getHeure().get(4), true)));
		tcS5       .setCellValueFactory(cellData -> new SimpleStringProperty(Fraction.simplifyDouble(cellData.getValue().getHeure().get(5), true)));
		tcS6       .setCellValueFactory(cellData -> new SimpleStringProperty(Fraction.simplifyDouble(cellData.getValue().getHeure().get(6), true)));

		tcTotImpair.setCellValueFactory(cellData -> {
			ArrayList<Double> h = cellData.getValue().getHeure();
			double s = h.get(1) + h.get(3) + h.get(5);
			return new SimpleStringProperty(Fraction.simplifyDouble(s, true));
		});

		tcTotPair  .setCellValueFactory(cellData -> {
			ArrayList<Double> h = cellData.getValue().getHeure();
			double s = h.get(2) + h.get(4) + h.get(6);
			return new SimpleStringProperty(Fraction.simplifyDouble(s, true));
		});

		tcTot      .setCellValueFactory(cellData -> {
			ArrayList<Double> h = cellData.getValue().getHeure();
			double s = h.get(1) + h.get(2) + h.get(3) + h.get(4) + h.get(5) + h.get(6);
			return new SimpleStringProperty(Fraction.simplifyDouble(s, true));
		});

/*
		tcNom.setCellFactory(TextFieldTableCell.forTableColumn());
		tcNom.setOnEditCommit(e-> {
			System.out.printf("%s -> %s\n", e.getOldValue(), e.getNewValue());

			e.getTableView().getItems().get(e.getTablePosition().getRow()).setNom(e.getNewValue());
		});



		ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(
				"John",
				"Jane",
				"Bob"
		));

		// Définition des cellules éditables avec ComboBox
		tcCategorie.setCellFactory(column -> new ComboBoxTableCell<>(new StringConverter<>() {
			@Override
			public String toString(String object) {
				return (String)object;
			}

			@Override
			public String fromString(String string) {
				return string;
			}
		}, comboBox.getItems()));

		tcPrenom.set

		tabAffInter.setEditable(true);
		*/

		/*ObservableList<Intervenant> list = FXCollections.observableArrayList(this.ensInter);
		list.addAll(this.interAAjouter);
		list.addAll(this.interAEnlever);
		tabAffInter.setItems(list);*/

		this.refresh();
	}

	private String getCellValue(Intervenant intervenant) {
		if (intervenant.isAjoute()) {
			return "➕";
		} else if (intervenant.isSupprime()) {
			return "❌";
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

	public void onBtnRechercher(ActionEvent actionEvent)
	{
		String recherche = txtFieldRecherche.getText();
		ObservableList<Intervenant> ensInter = FXCollections.observableList(Controleur.get().getMetier().rechercheIntervenantByText(recherche));
		tabAffInter.setItems(ensInter);
	}
}