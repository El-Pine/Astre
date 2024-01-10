package fr.elpine.astre;

import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.DB;
import javafx.application.Application;

public class Controleur
{
    private static Controleur ctrl;

    private final DB    db;
    private Astre astre;

    private Controleur()
    {
        Controleur.ctrl = this;

        // Mise en place de la base de données (sans connexion effective)
        this.db = new DB();

        // Mise en place de la partie IHM (qui gère la connexion à la db)
        Application.launch(AstreApplication.class);
    }

    public void startAstre()
    {
        this.astre = new Astre(this);
    }

    public static Controleur get()
    {
        return Controleur.ctrl;
    }

    public DB getDb() { return this.db; }
    public Astre getMetier() { return this.astre; }


    public static void main(String[] args)
    {
        new Controleur();
    }
}
