package fr.elpine.astre;

import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.ihm.stage.StagePrincipal;
import javafx.application.Application;

import java.io.IOException;

public class Controleur
{
    private static Controleur ctrl;

    private DB db;

    private Controleur()
    {
        this.db = new DB();

        Astre astre = new Astre( this );

        System.out.println("test222222222");

        //Application.launch(StagePrincipal.class);
        Application.launch(AstreApplication.class);

        System.out.println("testtttttttt");

        StagePrincipal stage = new StagePrincipal();
        stage.show();

	    //StageIntervenant stageIntervenant = new StageIntervenant();

        //stageIntervenant.show();

        //init

        //semestre

        //module
        //intervenant

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
