package fr.elpine.astre.ihm.stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

		((StagePrevisionnel) fxmlLoader.getController()).setStage(stage);

		stage.setTitle("Previsions");
		stage.setScene(scene);

		stage.setOnCloseRequest(e -> {
			// perform actions before closing
			try { StagePrincipal.creer().show(); } catch (IOException ignored) {}
		});

		return stage;
	}

	private void setStage(Stage stage) { this.stage = stage; }

	public void onBtnCreeSAE(ActionEvent actionEvent)
	{
	}

	public void onBtnCreeStage(ActionEvent actionEvent)
	{
	}

	public void onBtnModifierPre(ActionEvent actionEvent)
	{
	}

	public void onBtnModifierSuppr(ActionEvent actionEvent)
	{
	}

	public void onBtnCreeRessource(ActionEvent actionEvent) {
	}

	public void onBtnRetour(ActionEvent actionEvent) throws IOException {
		stage.close();
		StagePrincipal.creer().show();
	}
}