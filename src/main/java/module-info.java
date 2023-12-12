module fr.elpine.astre {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;

	opens fr.elpine.astre to javafx.fxml;
	exports fr.elpine.astre;
}