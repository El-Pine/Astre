package fr.elpine.astre;

import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.DB;
import javafx.application.Application;

import java.io.*;

// TODO : mettre des couleurs par modules

public class Controleur
{
    private static Controleur ctrl;

    private DB    db;
    private Astre astre;

    private Controleur()
    {
        Controleur.ctrl = this;

        this.startApplication();

        this.db = new DB();

        // Attente de la connexion à la base de données
        while (!DB.isConnected()) try { Thread.sleep(50); } catch (Exception ignored) {}

        this.astre = new Astre(this);
    }

    private void startApplication()
    {
        new Thread(() -> Application.launch(AstreApplication.class)).start();
    }

    public static Controleur get()
    {
        return Controleur.ctrl == null ? new Controleur() : Controleur.ctrl;
    }

    public DB getDb() { return this.db; }
    public Astre getMetier() { return this.astre; }

    public static void main(String[] args)
    {
        Controleur.get();
    }
}
