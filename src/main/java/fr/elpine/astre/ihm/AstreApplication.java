package fr.elpine.astre.ihm;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.stage.StageInitBd;
import fr.elpine.astre.ihm.stage.StagePrincipal;
import fr.elpine.astre.metier.DB;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;



import java.io.IOException;

public class AstreApplication extends Application
{
	@Override
	public void start(Stage primaryStage) throws IOException
	{
		Application.setUserAgentStylesheet(AstreApplication.class.getResource("cupertino-light.css").toExternalForm());


		if ( !DB.isConnected() ) {
			StageInitBd.creer(null).show();
		} else {
			StagePrincipal.creer().show();
		}
	}
}
