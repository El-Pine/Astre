package fr.elpine.astre.ihm;

import fr.elpine.astre.ihm.stage.StageInitBd;
import fr.elpine.astre.ihm.stage.StagePrincipal;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;

public class AstreApplication extends Application
{
	public static boolean erreur = false;

	@Override
	public void start(Stage primaryStage) throws IOException
	{
		if ( erreur )
			StageInitBd.creer( null ).show();
		else
			StagePrincipal.creer().show();
	}
}
