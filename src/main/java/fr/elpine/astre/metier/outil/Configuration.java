package fr.elpine.astre.metier.outil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Configuration
{
	public static final String CONFIGURATION_FILE_NAME = "config.txt";
	public static final String CONFIGURATION_FILE_REGEX = "^([^=#]+)[=](.+)$";
	private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

	public static String get(String key)
	{
		key = key.replace("=", "");

		prepare();

		try {

			Scanner sc = new Scanner( new File(CONFIGURATION_FILE_NAME) );

			while (sc.hasNextLine()) {
				Pattern pattern = Pattern.compile(CONFIGURATION_FILE_REGEX);
				Matcher matcher = pattern.matcher(sc.nextLine());

				if (matcher.find() && matcher.group(1).equals(key))
				{
					return matcher.group(2);
				}
			}

			sc.close();

		} catch (Exception e) {
			logger.error(String.format("Erreur lors de la récupération de la clé '%s' dans la configuration", key), e);
		}

		return null;
	}

	public static void set(String key, String value)
	{
		key = key.replace("=", "");

		prepare();

		try {
			File          fichier = new File(CONFIGURATION_FILE_NAME);
			Scanner       sc      = new Scanner( fichier );
			StringBuilder contenu = new StringBuilder();

			boolean find = false;

			while (sc.hasNextLine()) {
				String ligne = sc.nextLine();

				Pattern pattern = Pattern.compile(CONFIGURATION_FILE_REGEX);
				Matcher matcher = pattern.matcher(ligne);

				if (matcher.find() && matcher.group(1).equals(key))
				{
					contenu.append(String.format("%s=%s", key, value)).append(System.lineSeparator());

					find = true;
				}
				else
				{
					contenu.append(ligne).append(System.lineSeparator());
				}
			}

			sc.close();

			if (!find) contenu.append(String.format("%s=%s", key, value)).append(System.lineSeparator());

			FileWriter writer = new FileWriter( fichier, false );
			writer.write(contenu.toString());
			writer.close();

			logger.info("Configuration mise à jour ({})", key);
		} catch (IOException e) {
			logger.error(String.format("Erreur lors de la mise à jour de la configuration (%s)", key), e);
		}
	}

	private static void prepare()
	{
		File f = new File(CONFIGURATION_FILE_NAME);

		if (!f.exists())
		{
			try {
				if (f.createNewFile()) logger.info("Fichier de configuration crée avec succès");
				else logger.warn("Fichier de configuration déjà existant");
			} catch (IOException e) {
				logger.error("Erreur lors de la création du fichier de configuration", e);
			}
		}
	}
}
