package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.ihm.stage.PopUp.StagePopUp;
import fr.elpine.astre.metier.objet.Intervenant;
import fr.elpine.astre.Controleur;
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
	private TableColumn<Intervenant,String> tc;
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

	private ObservableList<Intervenant> ensInter;
	@FXML
	private TextField txtFieldRecherche;

	//private Stage stage;




	public ArrayList<Intervenant> interAAjouter;
	public ArrayList<Intervenant> interAEnlever;


	public StageIntervenant() // fxml -> "intervenant"
	{
		this.setTitle("Intervenants");

		this.setMinWidth(1500);
		this.setMinHeight(600);

		this.ensInter      = FXCollections.observableArrayList(Controleur.get().getMetier().getIntervenants());
		this.interAAjouter = new ArrayList<>();
		this.interAEnlever = new ArrayList<>();
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

		for ( Intervenant inter : this.interAAjouter )
		{
			Controleur.get().getMetier().ajouterIntervenant(inter);
		}
		for ( Intervenant inter : this.interAEnlever )
		{
			Controleur.get().getMetier().supprimerIntervenant(inter);
		}

		Controleur.get().getMetier().enregistrer();
		this.close();
		//StagePrincipal.creer().show();
	}

	@FXML
	protected void onBtnClickAnnuler() throws IOException {
		this.interAAjouter = this.interAEnlever = new ArrayList<>();
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
		if (PopUp.confirmationR("Suprression d'intervenant", null, String.format("Êtes-vous sûr de vouloir supprimer l'intervenant : %s %s", inter.getNom(), inter.getPrenom())) && this.ensInter.contains(inter))
		{
			this.ensInter.remove(inter);
			this.interAEnlever.add(inter);
			inter.supprimer(false);
		}
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

		tc.setCellValueFactory(cellData -> new SimpleStringProperty(getCellValue(cellData.getValue())));
		tc.setCellFactory(column -> new TableCell<>() {
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

		ObservableList<Intervenant> list = FXCollections.observableArrayList(this.ensInter);
		list.addAll(this.interAAjouter);
		list.addAll(this.interAEnlever);
		tabAffInter.setItems(list);
	}

	private String getCellValue(Intervenant intervenant) {
		if (this.interAAjouter.contains(intervenant)) {
			return "➕";
		} else if (this.interAEnlever.contains(intervenant)) {
			return "❌";
		} else {
			return "";
		}
	}

	public void refresh() {
		ObservableList<Intervenant> list = FXCollections.observableArrayList(this.ensInter);
		list.addAll(this.interAAjouter);
		list.addAll(this.interAEnlever);
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