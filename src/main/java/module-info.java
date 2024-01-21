module fr.elpine.astre {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires java.desktop;
	requires org.slf4j;
	requires org.postgresql.jdbc;
    requires com.opencsv;
	requires ch.qos.logback.classic;

    exports fr.elpine.astre;
	exports fr.elpine.astre.ihm;
	exports fr.elpine.astre.ihm.stage;
	exports fr.elpine.astre.metier;
	exports fr.elpine.astre.metier.objet;
	exports fr.elpine.astre.metier.outil;

	opens fr.elpine.astre                 to javafx.fxml;
	opens fr.elpine.astre.ihm             to javafx.fxml;
	opens fr.elpine.astre.ihm.stage       to javafx.fxml;
	exports fr.elpine.astre.ihm.outil;
	opens fr.elpine.astre.ihm.outil to javafx.fxml;
}