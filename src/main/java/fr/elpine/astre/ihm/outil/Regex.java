package fr.elpine.astre.ihm.outil;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.HashMap;

public class Regex
{
	// All
	public static final String REGEX_ALL           = "^.*$";
	public static final String REGEX_ALL_NOT_EMPTY = "^.+$";

	// Nom, Prénom
	public static final String REGEX_NOM = "^[A-Za-zÀ-ÿ'-]+(?: [A-Za-zÀ-ÿ'-]+)*$";
	public static final String REGEX_NOM_CARAC = "^[A-Za-zÀ-ÿ' -]*$";

	// Mail
	public static final String REGEX_MAIL = "^[a-zA-Z0-9._%+-]+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
	public static final String REGEX_MAIL_CARAC = "^[a-zA-Z0-9._%+-@]*$";

	// Entier
	public static final String REGEX_INT = "^\\d*$";
	public static final String REGEX_INT_NOT_EMPTY = "^\\d+$";

	private Regex() {}

	public static void activerRegex(String regex, String regexCaractere, TextField txt, HashMap<TextField,Boolean> validMap, boolean optionel)
	{
		validMap.put(txt, txt.getText().isEmpty() && optionel);

		txt.setTextFormatter( new TextFormatter<>( change -> {

			String texte = change.getControlNewText();

			if (!texte.matches(regexCaractere)) return null;

			if (texte.matches(regex) || (optionel && texte.isEmpty()))
			{
				txt.setStyle("");
				validMap.put(txt, true);
			}
			else
			{
				if (texte.isEmpty()) txt.setStyle("");
				else                 txt.setStyle("-fx-border-color: red; -fx-border-radius: 6px; -fx-border-width: 1px");

				validMap.put(txt, false);
			}

			return change;
		}));
	}

	public static boolean estValide(HashMap<TextField,Boolean> map)
	{
		boolean valid = true;

		for (boolean b : map.values()) if (!b) { valid = false; break; }

		return valid;
	}
}
