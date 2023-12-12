package fr.elpine.astre.ihm.stage;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StagePrincipal extends Stage
{
	public StagePrincipal()
	{
		FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("accueil.fxml"));

		Scene scene = null;
		try {
			scene = new Scene(fxmlLoader.load(), 300, 200);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		this.setTitle("Accueil");
		this.setScene(scene);
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