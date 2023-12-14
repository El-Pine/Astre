package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.ihm.model.SaisieRessource;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StageSaisieSae
{
	private Stage stage;

	public static Stage creer() throws IOException
	{
		Stage stage = new Stage();

		FXMLLoader fxmlLoader = new FXMLLoader(StageSaisieSae.class.getResource("saisieSae.fxml"));

		Scene scene = new Scene(fxmlLoader.load(), 1000, 660);

		StageSaisieSae stageCtrl = fxmlLoader.getController();
		if (stageCtrl != null) stageCtrl.setStage(stage);

		stage.setTitle("Saisie Sae");
		stage.setScene(scene);

		stage.setOnCloseRequest(e -> {
			// perform actions before closing
			try { StagePrevisionnel.creer().show(); } catch (IOException ignored) {}
		});

		return stage;
	}

	private void setStage(Stage stage) { this.stage = stage; }

	@FXML
	protected void onBtnAjouter() throws IOException {
	}

	@FXML
	protected void onBtnSupprimer() throws IOException {
	}

	@FXML
	protected void onBtnDetail() {
		// A FAIRE
	}

	@FXML
	protected void onBtnEnregistrer() throws IOException {
		stage.close();
		StagePrevisionnel.creer().show();
	}

	@FXML
	protected void onBtnAnnuler() throws IOException {
		stage.close();
		StagePrevisionnel.creer().show();
	}
}