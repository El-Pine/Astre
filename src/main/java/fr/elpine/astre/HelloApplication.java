package fr.elpine.astre;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("accueil.fxml"));

		Scene scene = new Scene(fxmlLoader.load(), 300, 200);

		stage.setTitle("Accueil");
		stage.setScene(scene);
		stage.show();
	}
}