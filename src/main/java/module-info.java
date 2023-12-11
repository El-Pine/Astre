module fr.elpine.astre {
	requires javafx.controls;
	requires javafx.fxml;

	opens fr.elpine.astre to javafx.fxml;
	exports fr.elpine.astre;
}