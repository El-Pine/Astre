
package fr.elpine.astre.ihm.stage;


import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.Affectation;
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

public class StageAffectation implements Initializable
{

	public TableView  <Affectation> tableau;
	public TableColumn<Affectation, Integer> tcIntervenant;
	public TableColumn<Affectation, String > tcType;
	public TableColumn<Affectation, Integer> tcSemaine;
	public TableColumn<Affectation, Integer> tcNbH;
	public TableColumn<Affectation, Integer> tcGrp;
	public TableColumn<Affectation, Double>  tcTotalEqtd;
	public TableColumn<Affectation, String> tcCommentaire;
	private Stage stage;

	public static Stage creer() throws IOException
	{
		Stage stage = new Stage();

		FXMLLoader fxmlLoader = new FXMLLoader(StageAffectation.class.getResource("Affectation.fxml"));

		Scene scene = new Scene(fxmlLoader.load(), 1000, 660);

		StageAffectation stageCtrl = fxmlLoader.getController();
		if (stageCtrl != null) stageCtrl.setStage(stage);

		stage.setTitle("Affectation");
		stage.setScene(scene);

		stage.setOnCloseRequest(e -> {
			// perform actions before closing
			try { StagePrevisionnel.creer().show(); } catch (IOException ignored) {}
		});

		return stage;
	}

	private void setStage(Stage stage) { this.stage = stage; }

	@FXML
	protected void onBtnAjouter() throws IOException {
	}

	@FXML
	protected void onBtnSupprimer() throws IOException {
	}

	@FXML
	protected void onBtnDetail() {
		// A FAIRE
	}

	@FXML
	protected void onBtnEnregistrer() throws IOException {
		stage.close();
		StagePrevisionnel.creer().show();
	}

	@FXML
	protected void onBtnAnnuler() throws IOException {
		stage.close();
		StagePrevisionnel.creer().show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		tcIntervenant.setCellValueFactory (cellData -> new SimpleIntegerProperty(cellData.getValue().getIdInter().getId()).asObject());
		tcType.setCellValueFactory        (cellData -> new SimpleStringProperty(cellData.getValue().getTypeHeure().getNom()));
		tcSemaine.setCellValueFactory         (cellData -> new SimpleIntegerProperty(cellData.getValue().getNbSemaine()).asObject());
		tcNbH.setCellValueFactory   (cellData -> new SimpleIntegerProperty(cellData.getValue().getNbHeure()).asObject());
		tcGrp.setCellValueFactory (cellData -> new SimpleIntegerProperty(cellData.getValue().getNbGroupe()).asObject());
		tcTotalEqtd.setCellValueFactory (cellData -> new SimpleDoubleProperty(cellData.getValue().getNbGroupe()).asObject()); //TODO: A remplacer par un getTotalEqtd()
		tcCommentaire.setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getCommentaire()));


		ObservableList<Affectation> elements = FXCollections.observableArrayList(Controleur.get().getDb().getAllaff());

		tableau.setItems(elements);
	}
}