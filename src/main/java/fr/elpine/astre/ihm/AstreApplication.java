package fr.elpine.astre.ihm;

import fr.elpine.astre.ihm.stage.StageIntervenant;
import fr.elpine.astre.ihm.stage.StagePrincipal;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AstreApplication extends Application
{
	@Override
	public void start(Stage primaryStage) throws IOException
	{
		StagePrincipal.creer().show();
	}
}
