module fr.elpine.astre {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;

	opens fr.elpine.astre to javafx.fxml;
	exports fr.elpine.astre;
	exports fr.elpine.astre.ihm;
	opens fr.elpine.astre.ihm to javafx.fxml;
}