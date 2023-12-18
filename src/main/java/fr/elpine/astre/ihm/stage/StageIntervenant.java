package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.metier.objet.Intervenant;
import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.Intervenant;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StageIntervenant implements Initializable
{
	@FXML
	private TableView<Intervenant> tabAffInter;

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



	private Stage stage;

	public static Stage creer() throws IOException
	{
		Stage stage = new Stage();

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

	// TODO : barre de recherche des intervenants

	private void setStage(Stage stage) { this.stage = stage; }

	@FXML
	protected void onBtnClickEnregistrer() throws IOException {
		stage.close();
		StagePrincipal.creer().show();
	}

	@FXML
	protected void onBtnClickAnnuler() throws IOException {
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
	protected void onBtnClickSupprimer() {
		Intervenant inter = tabAffInter.getSelectionModel().getSelectedItem();
		Controleur.get().getDb().supprimerIntervenant(inter);
	}

	public void desactiver()
	{
		this.stage.getScene().lookup("#onBtnClickEnregistrer").setDisable(true);
		this.stage.getScene().lookup("#onBtnClickAnnuler").setDisable(true);
		this.stage.getScene().lookup("#onBtnClickAjouter").setDisable(true);
		this.stage.getScene().lookup("#onBtnClickSupprimer").setDisable(true);
	}

	public void activer() {
		this.stage.getScene().lookup("#onBtnClickEnregistrer").setDisable(false);
		this.stage.getScene().lookup("#onBtnClickAnnuler").setDisable(false);
		this.stage.getScene().lookup("#onBtnClickAjouter").setDisable(false);
		this.stage.getScene().lookup("#onBtnClickSupprimer").setDisable(false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		tcCategorie.setCellValueFactory(cellData -> new SimpleStringProperty (cellData.getValue().getStatut  ()  .getCode  ()));
		tcNom      .setCellValueFactory(cellData -> new SimpleStringProperty (cellData.getValue().getNom     ()));
		tcPrenom   .setCellValueFactory(cellData -> new SimpleStringProperty (cellData.getValue().getPrenom  ()));
		tcHServ    .setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getService () ).asObject ());
		tcHMax     .setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getHeureMax() ).asObject ());
		tcRatioTP  .setCellValueFactory(cellData -> new SimpleDoubleProperty (cellData.getValue().getRatioTP () ).asObject ());

		ObservableList<Intervenant> ensInter = FXCollections.observableArrayList(Controleur.get().getDb().getAllIntervenant());

		System.out.println(ensInter);

		tabAffInter.setItems(ensInter);

	}
}