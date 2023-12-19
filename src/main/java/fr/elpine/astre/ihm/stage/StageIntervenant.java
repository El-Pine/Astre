package fr.elpine.astre.ihm.stage;

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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StageIntervenant implements Initializable
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
	private TableColumn<Intervenant, String> tcCategorie;
	@FXML
	private TableColumn<Intervenant, Integer> tcHServ;
	@FXML
	private TableColumn<Intervenant, Integer> tcHMax;
	@FXML
	private TableColumn<Intervenant, Double> tcRatioTP;

	private static ObservableList<Intervenant> ensInter;
	@FXML
	private TextField txtFieldRecherche;

	private Stage stage;

	public static ArrayList<Intervenant> interAAjouter;
	public static ArrayList<Intervenant> interAEnlever;

	public static Stage creer() throws IOException
	{
		Stage stage = new Stage();
		StageIntervenant.ensInter = FXCollections.observableArrayList(Controleur.get().getDb().getAllIntervenant());
		StageIntervenant.interAAjouter = new ArrayList<Intervenant>();
		StageIntervenant.interAEnlever = new ArrayList<Intervenant>();

		FXMLLoader fxmlLoader = new FXMLLoader(StageIntervenant.class.getResource("intervenant.fxml"));

		Scene scene = new Scene(fxmlLoader.load(), 700, 450);

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

	private void setStage(Stage stage) { this.stage = stage; }

	@FXML
	protected void onBtnClickEnregistrer() throws IOException, SQLException {

		for ( Intervenant inter : StageIntervenant.interAAjouter )
		{
			Controleur.get().getDb().ajouterIntervenant(inter);
		}
		for ( Intervenant inter : StageIntervenant.interAEnlever )
		{
			Controleur.get().getDb().supprimerIntervenant(inter);
		}

		Controleur.get().getDb().enregistrer();
		stage.close();
		StagePrincipal.creer().show();
	}

	@FXML
	protected void onBtnClickAnnuler() throws IOException, SQLException {
		StageIntervenant.interAAjouter = StageIntervenant.interAEnlever = new ArrayList<Intervenant>();
		stage.close();
		StagePrincipal.creer().show();
	}

	@FXML
	protected void onBtnClickAjouter() throws IOException
	{
		this.desactiver();
		StageAjoutIntervenant.creer( this ).show();
	}

	@FXML
	protected void onBtnClickSupprimer() throws IOException {
		Intervenant inter = tabAffInter.getSelectionModel().getSelectedItem();
		if (StagePopUp.PopUpConfirmation("Suprression d'intervenant", "Êtes- vous sûr de vouloir supprimer l'intervenant : " + inter.getNom() + " " + inter.getPrenom()))
		{
			StageIntervenant.ensInter.remove(inter);
			StageIntervenant.interAEnlever.add(inter);
			this.refresh();
		}
	}

	public void desactiver()
	{
		this.stage.getScene().lookup("#btnEnregistrer").setDisable(true);
		this.stage.getScene().lookup("#btnAnnuler").setDisable(true);
		this.stage.getScene().lookup("#btnAjouter").setDisable(true);
		this.stage.getScene().lookup("#btnSupprimer").setDisable(true);
	}

	public void activer() {
		this.stage.getScene().lookup("#btnEnregistrer").setDisable(false);
		this.stage.getScene().lookup("#btnAnnuler").setDisable(false);
		this.stage.getScene().lookup("#btnAjouter").setDisable(false);
		this.stage.getScene().lookup("#btnSupprimer").setDisable(false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
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
				}
			}
		});
		tcCategorie.setCellValueFactory(cellData -> new SimpleStringProperty (cellData.getValue().getStatut  ()  .getCode  ()));
		tcNom      .setCellValueFactory(cellData -> new SimpleStringProperty (cellData.getValue().getNom     ()));
		tcPrenom   .setCellValueFactory(cellData -> new SimpleStringProperty (cellData.getValue().getPrenom  ()));
		tcHServ    .setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getService () ).asObject ());
		tcHMax     .setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getHeureMax() ).asObject ());
		tcRatioTP  .setCellValueFactory(cellData -> new SimpleDoubleProperty (cellData.getValue().getRatioTP () ).asObject ());

		ObservableList<Intervenant> list = FXCollections.observableArrayList(StageIntervenant.ensInter);
		list.addAll(StageIntervenant.interAAjouter);
		list.addAll(StageIntervenant.interAEnlever);
		tabAffInter.setItems(list);
	}

	private String getCellValue(Intervenant intervenant) {
		if (StageIntervenant.interAAjouter.contains(intervenant)) {
			return "➕";
		} else if (StageIntervenant.interAEnlever.contains(intervenant)) {
			return "❌";
		} else {
			return ""; // Vous pouvez également renvoyer un autre texte ou laisser la cellule vide si l'intervenant n'est pas dans interAAjouter
		}
	}

	public void refresh() {
		ObservableList<Intervenant> list = FXCollections.observableArrayList(StageIntervenant.ensInter);
		list.addAll(StageIntervenant.interAAjouter);
		list.addAll(StageIntervenant.interAEnlever);
		tabAffInter.setItems(list);
	}

	public void onBtnRechercher(ActionEvent actionEvent)
	{
		String recherche = txtFieldRecherche.getText();
		ObservableList<Intervenant> ensInter = FXCollections.observableList(Controleur.get().getDb().getIntervenantByNom(recherche));
		tabAffInter.setItems(ensInter);
	}
}