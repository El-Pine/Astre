package fr.elpine.astre.ihm;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.stage.Manager;
import fr.elpine.astre.metier.outil.Configuration;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

//TODO: Verifiez l'orthographe de l'application
public class AstreApplication extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		Logger logger = LoggerFactory.getLogger(getClass());


		String theme = Configuration.get("theme");
		setTheme(theme != null ? theme : "cupertino-light");


		if ( !Controleur.get().getDb().reloadDB() ) {
			logger.warn("Informations de connexion à la base de données erronées, nouvelle saisie demandé !");

			Stage stage = Manager.creer( "initDb" );
			stage.show();
		} else {
			Stage stage = Manager.creer( "accueil" );
			stage.show();
		}
	}

	public static void setTheme(String theme)
	{
		if ( !theme.equals( Configuration.get("theme") ) ) Configuration.set("theme", theme);

		setUserAgentStylesheet(Objects.requireNonNull(AstreApplication.class.getResource("styles/%s.css".formatted(theme))).toExternalForm());
	}

	public static void refreshIcon(Stage stage)
	{
		stage.getIcons().add(new Image(Objects.requireNonNull(AstreApplication.class.getResourceAsStream("icon.png"))));
	}
}
