package fr.elpine.astre.ihm.stage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class StageIntervenant
{
	public TableView tabAffInter;
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
		stage.close();
		StageAjoutIntervenant.creer().show();
	}

	@FXML
	protected void onBtnClickSupprimer() {
		// A FAIRE
	}
}