package fr.elpine.astre;

import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.db.RessourceSql;
import javafx.application.Application;

public class Controleur
{
    private static Controleur ctrl;

    private DB    db;
    private Astre astre;
    private RessourceSql ressources;

    private Controleur()
    {
        this.db    = new DB();
        this.ressources = new RessourceSql();
        this.astre = new Astre( this );

        Application.launch(AstreApplication.class);
    }

    public static Controleur get()
    {
        if (Controleur.ctrl == null) Controleur.ctrl = new Controleur();

        return Controleur.ctrl;
    }

    public DB getDb() { return this.db; }

    public void getRessources()
    {
        this.ressources.getRessources();

    }


    public static void main(String[] args)
    {
        Controleur.get();
    }
}
