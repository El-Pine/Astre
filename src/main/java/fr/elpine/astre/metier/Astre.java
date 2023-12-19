package fr.elpine.astre.metier;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.*;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.HashMap;

public class Astre
{
    private Controleur ctrl;
    private ArrayList<Annee>    ensAnnee;
    private ArrayList<Semestre> ensSemestre;
    private ArrayList<Modules>  ensModules;
    private ArrayList<Intervenant> ensIntervenant;
    private ArrayList<CategorieHeure> ensCategorieHeure;
    private ArrayList<CategorieIntervenant> ensCategorieIntervenant;
    private ArrayList<Attribution> ensAttribution;
    private ArrayList<Affectation> ensAffectation;
    public Astre ( Controleur ctrl )
    {
        this.ctrl = ctrl;
        this.ensAnnee                = ctrl.getDb().getAllAnnee();
        this.ensSemestre             = ctrl.getDb().getAllSemestre();
        this.ensModules              = ctrl.getDb().getAllModule();
        this.ensIntervenant          = ctrl.getDb().getAllIntervenant();
        this.ensCategorieHeure       = ctrl.getDb().getAllCategorieHeure();
        this.ensCategorieIntervenant = ctrl.getDb().getAllCategorieIntervenant();
        this.ensAffectation          = ctrl.getDb().getAllaff();
        this.ensAttribution          = ctrl.getDb().getAllAttribution();

    }


    //Cette méthode rajoute les semestres dans une arraylist ensSemestre dans l'annee du semestre.
    public void initListSemestreAnnee()
    {
        for (Annee a: ensAnnee)
        {
            for (Semestre s : ensSemestre)
            {
                if(s.getAnnee().getNumero().equals(a.getNumero()))
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
            for (Modules m : ensModules)
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
