package fr.elpine.astre.ihm;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.stage.StageInitBd;
import fr.elpine.astre.ihm.stage.StagePrincipal;
import fr.elpine.astre.metier.DB;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Objects;

public class AstreApplication extends Application
{
	@Override
	public void start(Stage primaryStage) throws IOException
	{
		/*
		* Thèmes    : cupertino, nord, primer & dracula (experimental)
		* Variantes : dark & light
		* */
		Application.setUserAgentStylesheet(AstreApplication.class.getResource("styles/cupertino-dark.css").toExternalForm());

		if ( !DB.isConnected() ) {
			StageInitBd.creer(null).show();
		} else {
			StagePrincipal.creer().show();
		}
	}

	public static void refreshIcon(Stage stage)
	{
		stage.getIcons().add(new Image(Objects.requireNonNull(AstreApplication.class.getResourceAsStream("icon.png"))));
	}
}
