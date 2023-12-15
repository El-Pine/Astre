package fr.elpine.astre.ihm;

import fr.elpine.astre.ihm.stage.StageInitBd;
import fr.elpine.astre.ihm.stage.StagePrincipal;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;

public class AstreApplication extends Application
{
	private static int codeInit;
	@Override
	public void start(Stage primaryStage) throws IOException
	{
		StagePrincipal.creer().show();
		if (codeInit!=0)
		{
			StageInitBd.creer(null).show();
		}
	}

	public static void init(int codeInit)
	{
		AstreApplication.codeInit = codeInit;
	}
}
