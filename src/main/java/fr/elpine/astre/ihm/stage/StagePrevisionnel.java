package fr.elpine.astre.ihm.stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class StagePrevisionnel
{
	private Stage stage;

	public static Stage creer() throws IOException
	{
		Stage stage = new Stage();

		FXMLLoader fxmlLoader = new FXMLLoader(StagePrevisionnel.class.getResource("previsionnel.fxml"));

		Scene scene = new Scene(fxmlLoader.load(), 700, 450);

		StagePrevisionnel stageCtrl = fxmlLoader.getController();
		if (stageCtrl != null) stageCtrl.setStage(stage);

		stage.setTitle("Previsions");
		stage.setScene(scene);

		stage.setOnCloseRequest(e -> {
			// perform actions before closing
			try { StagePrincipal.creer().show(); } catch (IOException ignored) {}
		});

		return stage;
	}

	private void setStage(Stage stage) { this.stage = stage; }

	@FXML
	public void onBtnCreerSae(ActionEvent actionEvent) throws IOException {
		stage.close();
		StageSaisieSae.creer().show();
	}

	@FXML
	public void onBtnCreerStage(ActionEvent actionEvent)
	{
	}

	@FXML
	public void onBtnCreerRessource(ActionEvent actionEvent) throws IOException {
		stage.close();
		StageSaisieRessource.creer().show();
	}

	@FXML
	public void onBtnSupprimer(ActionEvent actionEvent)
	{
	}

	@FXML
	public void onBtnModifier(ActionEvent actionEvent)
	{
	}
}