package fr.elpine.astre.ihm.stage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;

public class StagePrincipal
{
	private Stage stage;

	public static Stage creer() throws IOException
	{
		Stage stage = new Stage();

		FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("accueil.fxml"));

		Scene scene = new Scene(fxmlLoader.load(), 300, 200);

		StagePrincipal stageCtrl = fxmlLoader.getController();
		if (stageCtrl != null) {
			stageCtrl.setStage(stage);
		}

		stage.setTitle("Accueil");
		stage.setScene(scene);

		return stage;
	}

	private void setStage(Stage stage) { this.stage = stage; }

	@FXML
	protected void onBtnClickParametre() throws IOException {
		stage.close();
		StageAccueilConfig.creer().show();
	}

	@FXML
	protected void onBtnClickPrevisionnel() throws IOException {
		stage.close();
		StagePrevisionnel.creer().show();
	}

	@FXML
	protected void onBtnClickIntervenant() throws IOException {
		stage.close();
		StageIntervenant.creer().show();
	}

	@FXML
	protected void onBtnClickEtat()
	{

	}
}