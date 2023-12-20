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
        this.ensModule               = ctrl.getDb().getAllModule( this.ensSemestre );
        this.ensCategorieIntervenant = ctrl.getDb().getAllCategorieIntervenant();
        this.ensIntervenant          = ctrl.getDb().getAllIntervenant( this.ensCategorieIntervenant );
        this.ensCategorieHeure       = ctrl.getDb().getAllCategorieHeure();
        this.ensAffectation          = ctrl.getDb().getAllAffectation( this.ensCategorieHeure, this.ensModule, this.ensIntervenant );
        this.ensAttribution          = ctrl.getDb().getAllAttribution( this.ensCategorieHeure, this.ensModule);
    }
}
