package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.stage.PopUp.StagePopUp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StagePrincipal implements Initializable
{
	public ImageView image;
	private Stage stage;

	public static Stage creer() throws IOException
	{
		Stage stage = new Stage();

		AstreApplication.refreshIcon(stage);

		FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("accueil.fxml"));

		Scene scene = new Scene(fxmlLoader.load(), 300, 400);

		StagePrincipal stageCtrl = fxmlLoader.getController();
		if (stageCtrl != null) {
			stageCtrl.setStage(stage);
		}

		stage.setTitle("Astre");
		stage.setScene(scene);

		scene.getStylesheets().add( StagePrincipal.class.getResource("style.css").toExternalForm());


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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		image.setImage(new Image(Objects.requireNonNull(AstreApplication.class.getResourceAsStream("icon.png"))));
	}
}