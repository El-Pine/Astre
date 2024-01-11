package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.ihm.AstreApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class Manager
{
	private static final Logger logger = LoggerFactory.getLogger(Manager.class);

	public static <T> T creer( String fxml ) {
		return Manager.creer( fxml, null );
	}

	public static <T> T creer( String fxml, Stage parent )
	{
		URL        fxmlUrl    = Manager.class.getResource(String.format("%s.fxml", fxml));
		FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);

		try
		{
			Scene scene = new Scene( fxmlLoader.load() );
			Stage stage = fxmlLoader.getController();

			if (stage == null) return null;

			AstreApplication.refreshIcon(stage);

			if (parent != null) {
				stage.initOwner(parent);
				stage.initModality(Modality.APPLICATION_MODAL);
			}

			stage.setScene( scene );

			return fxmlLoader.getController();
		}
		catch (IOException e) {
			logger.error(String.format("Il y a eu un problème lors du chargement du fxml : %s.fxml", fxml), e);

			return null;
		}
	}
}
