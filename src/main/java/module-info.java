module fr.elpine.astre {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;

	exports fr.elpine.astre;
	exports fr.elpine.astre.ihm;
	exports fr.elpine.astre.ihm.stage;

	opens fr.elpine.astre           to javafx.fxml;
	opens fr.elpine.astre.ihm       to javafx.fxml;
	opens fr.elpine.astre.ihm.stage to javafx.fxml;
}