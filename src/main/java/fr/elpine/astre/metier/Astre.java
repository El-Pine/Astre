package fr.elpine.astre.metier;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.objet.Module;

import java.util.ArrayList;

public class Astre
{
    private Controleur ctrl;
    private Annee anneeActuelle;

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

        this.anneeActuelle = this.ensAnnee.isEmpty() ? null : this.ensAnnee.get(0);
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

	public Semestre               rechercheSemestreByNumero  (int numero)
	{
		for (Semestre sem : this.ensSemestre)
		{
			if(sem.getNumero() == numero) return sem;
		}
		return null;
	}

    /*-----------------------------*/
    /* Gestion de l'année actuelle */
    /*-----------------------------*/

    public Annee getAnneeActuelle() { return this.anneeActuelle; }
    public void changerAnneeActuelle( Annee a )
    {
        if (this.ensAnnee.contains(a)) this.anneeActuelle = a;
    }

    /*--------------*/
    /* Méthodes GET */
    /*--------------*/

    public ArrayList<Annee> getAnnees() { return this.ensAnnee; }
    public ArrayList<Semestre> getSemestres() { return this.ensSemestre; }
    public ArrayList<Module> getModules() { return this.ensModule; }
    public ArrayList<CategorieIntervenant> getCategorieIntervenants() { return this.ensCategorieIntervenant; }
    public ArrayList<Intervenant> getIntervenants() { return this.ensIntervenant; }
    public ArrayList<CategorieHeure> getCategorieHeures() { return this.ensCategorieHeure; }
    public ArrayList<Affectation> getAffectations() { return this.ensAffectation; }
    public ArrayList<Attribution> getAttributions() { return this.ensAttribution; }

    /*------------------*/
    /* Méthodes Ajouter */
    /*------------------*/

    public Annee ajouterAnnee(Annee a)
    {
        this.ensAnnee.add(a);

        return a;
    }
}
