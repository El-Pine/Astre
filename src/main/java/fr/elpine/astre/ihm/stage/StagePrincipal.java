package fr.elpine.astre.ihm.stage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StagePrincipal
{
	private Stage stage;

	public static Stage creer() throws IOException
	{
		Stage stage = new Stage();

		FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("accueil.fxml"));

		Scene scene = new Scene(fxmlLoader.load(), 300, 200);

		((StagePrincipal) fxmlLoader.getController()).setStage(stage);

		stage.setTitle("Accueil");
		stage.setScene(scene);

		return stage;
	}

	private void setStage(Stage stage) { this.stage = stage; }

	@FXML
	protected void onBtnClickParametre() {
		// A FAIRE
	}

	@FXML
	protected void onBtnClickPrevisionnel() throws IOException {
		// A FAIRE
		stage.close();
		StagePrevisionnel.creer().showAndWait();
	}

	@FXML
	protected void onBtnClickIntervenant() throws IOException {
		// A FAIRE
		stage.close();
		StageIntervenant.creer().showAndWait();
	}

	@FXML
	protected void onBtnClickEtat() {
		// A FAIRE
	}
}