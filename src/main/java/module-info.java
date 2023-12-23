module fr.elpine.astre {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires org.postgresql.jdbc;
	requires java.desktop;

	exports fr.elpine.astre;
	exports fr.elpine.astre.ihm;
	exports fr.elpine.astre.ihm.stage;
	exports fr.elpine.astre.metier.objet;

	opens fr.elpine.astre           to javafx.fxml;
	opens fr.elpine.astre.ihm       to javafx.fxml;
	opens fr.elpine.astre.ihm.stage to javafx.fxml;
    exports fr.elpine.astre.ihm.stage.PopUp;
    opens fr.elpine.astre.ihm.stage.PopUp to javafx.fxml;
}