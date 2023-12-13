package fr.elpine.astre;

import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.db.DB;
import javafx.application.Application;

public class Controleur
{
    private static Controleur ctrl;

    private DB    db;
    private Astre astre;

    private Controleur()
    {
        this.db    = new DB();
        this.astre = new Astre( this );

        Application.launch(AstreApplication.class);
    }

    public static Controleur get()
    {
        if (Controleur.ctrl == null) Controleur.ctrl = new Controleur();

        return Controleur.ctrl;
    }

    public DB getDb() { return this.db; }

    public static void main(String[] args)
    {
        Controleur.get();
    }
}
