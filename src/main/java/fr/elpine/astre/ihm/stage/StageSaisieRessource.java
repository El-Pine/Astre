package fr.elpine.astre.ihm.stage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class StageSaisieRessource
{
	public TableView tabAffInter;
	private Stage stage;

	public static Stage creer() throws IOException
	{
		Stage stage = new Stage();

		FXMLLoader fxmlLoader = new FXMLLoader(StageSaisieRessource.class.getResource("saisieRessource.fxml"));

		Scene scene = new Scene(fxmlLoader.load(), 1000, 660);

		StageSaisieRessource stageCtrl = fxmlLoader.getController();
		if (stageCtrl != null) stageCtrl.setStage(stage);

		stage.setTitle("SaisieRessource");
		stage.setScene(scene);

		stage.setOnCloseRequest(e -> {
			// perform actions before closing
			try { StagePrincipal.creer().show(); } catch (IOException ignored) {}
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
}