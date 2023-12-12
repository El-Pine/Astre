package fr.elpine.astre.ihm;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StagePrincipal extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("accueil.fxml"));

		Scene scene = new Scene(fxmlLoader.load(), 300, 200);

		stage.setTitle("Accueil");
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	protected void onBtnClickParametre() {
		// A FAIRE
	}

	@FXML
	protected void onBtnClickPrevisionnel() {
		// A FAIRE
	}

	@FXML
	protected void onBtnClickIntervenant() {
		// A FAIRE
	}

	@FXML
	protected void onBtnClickEtat() {
		// A FAIRE
	}
}