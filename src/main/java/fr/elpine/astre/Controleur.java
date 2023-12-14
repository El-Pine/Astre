package fr.elpine.astre;

import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.db.RessourceSql;
import fr.elpine.astre.metier.objet.Ressource;
import javafx.application.Application;

import java.util.ArrayList;

public class Controleur
{
    private static Controleur ctrl;

    private DB    db;
    private Astre astre;
    private RessourceSql ressources;

    private Controleur()
    {
        this.db         = new DB();
        this.ressources = new RessourceSql( this );
        this.astre      = new Astre( this );
    }

    public void start()
    {
        Application.launch(AstreApplication.class);
    }

    public static Controleur get()
    {
        if (Controleur.ctrl == null) Controleur.ctrl = new Controleur();

        return Controleur.ctrl;
    }

    public DB getDb() { return this.db; }

    public ArrayList<Ressource> getRessources()
    {
        return this.ressources.getRessources();
    }


    public static void main(String[] args)
    {
        Controleur.get().start();
    }
}
