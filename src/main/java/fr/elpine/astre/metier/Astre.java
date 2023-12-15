package fr.elpine.astre.metier;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.*;

import java.util.HashMap;

public class Astre
{
    private Controleur ctrl;

    public Astre ( Controleur ctrl )
    {
        this.ctrl = ctrl;
    }
    


    /*
    public void ajouterRessource(String nom, String code, String commentaire, int nbHeurePn, int nbHeurePnCours, HashMap<String, Integer> heureSemestre, int nbHeuretl)
    {
        Ressource ressource = new Ressource(nom,code, commentaire, nbHeurePn, nbHeurePnCours, heureSemestre, nbHeuretl);

        ctrl.getDb().ajouterRessource(ressource);
    }

    public void ajouterStage(String nom, String code, String commentaire, int nbHeureREH, int nbHeureTut, int nbHeure)
    {
        Stage stage = new Stage (nom, code, commentaire, nbHeureREH, nbHeureTut, nbHeure);

        ctrl.getDb().ajouterStage(stage);
    }

    public void ajouterSAE(String nom, String code, String commentaire, int nbHeurePnSem, int nbHeureTut, int nbHeure)
    {
        SAE sae = new SAE(nom, code, commentaire, nbHeurePnSem, nbHeureTut, nbHeure);

        ctrl.getDb().ajouterSAE(sae);
    }

    public void ajouterCategorieIntervenant(String code, String nom, int nbHeureMax,int service, double ratioTd, boolean estCompleter)
    {
        CategorieIntervenant catInter = new CategorieIntervenant(code, nom, nbHeureMax,service,ratioTd,estCompleter);

        ctrl.getDb().ajouterCategorieIntervenant(catInter);
    }
    */
}
