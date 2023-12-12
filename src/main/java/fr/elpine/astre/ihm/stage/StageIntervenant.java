package fr.elpine.astre.ihm.stage;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class StageIntervenant extends Stage
{
	public TableView tabAffInter;

	public StageIntervenant() throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(StageIntervenant.class.getResource("intervenant.fxml"));

		Scene scene = new Scene(fxmlLoader.load(), 700, 450);

		this.setTitle("Intervenants");
		this.setScene(scene);
	}


	@FXML
	protected void onBtnClickEnregistrer() {
		// A FAIRE
	}

	@FXML
	protected void onBtnClickAnnuler() {
		// A FAIRE
	}

	@FXML
	protected void onBtnClickAjouter() {
		// A FAIRE
	}

	@FXML
	protected void onBtnClickSupprimer() {
		// A FAIRE
	}
}