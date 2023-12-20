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
    private ArrayList<Module>      ensModules;
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
        this.ensModules              = ctrl.getDb().getAllModule( this.ensModules );
        this.ensCategorieIntervenant = ctrl.getDb().getAllCategorieIntervenant();
        this.ensIntervenant          = ctrl.getDb().getAllIntervenant( this.ensCategorieIntervenant );
        this.ensCategorieHeure       = ctrl.getDb().getAllCategorieHeure();
        this.ensAffectation          = ctrl.getDb().getAllAffectation( this.ensCategorieHeure, this.ensModules, this.ensIntervenant );
        this.ensAttribution          = ctrl.getDb().getAllAttribution( this.ensCategorieHeure, this.ensModules );

    }


    //Cette méthode rajoute les semestres dans une arraylist ensSemestre dans l'annee du semestre.
    public void initListSemestreAnnee()
    {
        for (Annee a: ensAnnee)
        {
            for (Semestre s : ensSemestre)
            {
                if(s.getAnnee().getNom().equals(a.getNom()))
                {
                    a.ajouterSemestre(s);
                    s.setAnnee(a);
                }
            }
        }
    }
    public void initListModuleSemestre()
    {
        for (Semestre s : ensSemestre)
        {
            for (Module m : ensModules)
            {
                if(m.getSemestre().getNumero() == s.getNumero())
                {
                    s.ajouterModule(m);
                    m.setSemestre(s);
                }
            }
        }
    }




}
