package fr.elpine.astre.metier;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.objet.Module;

import java.util.ArrayList;

public class Astre
{
    private Controleur ctrl;
    private ArrayList<Annee>    ensAnnee;
    private ArrayList<Semestre>    ensSemestre;
    private ArrayList<Module> ensModule;
    private ArrayList<Intervenant> ensIntervenant;
    private ArrayList<CategorieHeure> ensCategorieHeure;
    private ArrayList<CategorieIntervenant> ensCategorieIntervenant;
    private ArrayList<Attribution> ensAttribution;
    private ArrayList<Affectation> ensAffectation;
    public Astre ( Controleur ctrl )
    {
        this.ctrl = ctrl;

        this.ensAnnee                = ctrl.getDb().getAllAnnee();
        this.ensSemestre             = ctrl.getDb().getAllSemestre( this.ensAnnee );
        this.ensModule               = ctrl.getDb().getAllModule(this.ensSemestre );
        this.ensCategorieIntervenant = ctrl.getDb().getAllCategorieIntervenant();
        this.ensIntervenant          = ctrl.getDb().getAllIntervenant( this.ensCategorieIntervenant );
        this.ensCategorieHeure       = ctrl.getDb().getAllCategorieHeure();
        this.ensAffectation          = ctrl.getDb().getAllaff      ( this.ensIntervenant, this.ensModule, this.ensCategorieHeure );
        this.ensAttribution          = ctrl.getDb().getAllAttribution(this.ensModule, this.ensCategorieHeure);
    }

    /*---------------------*/
    /* Méthode de recherche*/
    /*---------------------*/

    public static  Module rechercherModule(ArrayList<Module> ensModule, String codeModule,int numSem, String anneeSem)
    {
        for (Module mod : ensModule)
        {
            if(mod.getCode    ().equals(codeModule)    &&
                    mod.getSemestre().getNumero() == numSem &&
                    mod.getSemestre().getAnnee().getNom().equals(anneeSem))
            { return mod;}
        }
        return null;
    }
    public static CategorieHeure rechercherCatHr(ArrayList<CategorieHeure> ensCategorie, String nomCatHr)
    {
        for (CategorieHeure catHrs : ensCategorie)
        {
            if(catHrs.getNom().equals(nomCatHr)){ return catHrs;}
        }
        return null;
    }

    public static Intervenant rechercherInter(ArrayList<Intervenant> ensInter, int idInter)
    {
        for (Intervenant inter : ensInter)
        {
            if(idInter == inter.getId()) return inter;
        }
        return null;
    }

    public static CategorieIntervenant rechercherCatInter(ArrayList<CategorieIntervenant> ensCatInter, String codeCatInter )
    {
        for (CategorieIntervenant catInter : ensCatInter)
        {
            if(catInter.getCode().equals(codeCatInter)) return catInter;
        }
        return null;
    }
}
