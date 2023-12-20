package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.ihm.stage.PopUp.StagePopUp;
import javafx.event.ActionEvent;
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
		try {
			StagePopUp.PopUpConfirmation("Tu es sur ? ", "Tu es sur ?");
		}
		catch (IOException ignored){}
	}

	public void onBtnClickAnnee(ActionEvent actionEvent) throws IOException {
		this.stage.close();
		StageAnnee.creer().show();
	}
}