package fr.elpine.astre.ihm.outil;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Objects;

public enum Emoji
{
	AJOUTER("emoji/ajouter.png", "A"),
	MODIFIER("emoji/modifier.png", "M"),
	SUPPRIMER("emoji/supprimer.png", "S"),
	ATTENTION("emoji/attention.png", "W"),
	VALIDE("emoji/valide.png", "V"),
	INVALIDE("emoji/invalide.png", "I");

	private final Image  img;
	private final String lettre;

	Emoji(String fichier, String lettre)
	{
		this.img    = new Image(Objects.requireNonNull(getClass().getResourceAsStream(fichier)));
		this.lettre = lettre;
	}


	public static void creer(TableCell tb, double size, Emoji ... emojis)
	{
		HBox hBox = new HBox(5);

		for (Emoji e : emojis)
		{
			if (e != null) {
				ImageView i = new ImageView(e.img);
				i.setFitHeight(size);
				i.setFitWidth(size);

				hBox.getChildren().add(i);
			}
		}

		tb.setTextFill(Color.TRANSPARENT);
		tb.setGraphic(hBox);
	}

	public static void format(TableCell tb, double size, String text)
	{
		ArrayList<Emoji> lst = new ArrayList<>();

		if (text != null)
			for (Emoji e : Emoji.values())
				if (text.contains(e.lettre)) lst.add(e);

		creer(tb, size, lst.toArray(new Emoji[0]));
	}


	public static TableCell getCellFactory()
	{
		return new TableCell<Object, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(item);

				Emoji.format(this, 15, item); // column size for 2 icons : 45px
			}
		};
	}
}
