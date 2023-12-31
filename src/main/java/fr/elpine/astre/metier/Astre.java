package fr.elpine.astre.metier;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.objet.*;

import java.util.ArrayList;
import java.util.Comparator;

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

        DB db = ctrl.getDb();

        this.ensAnnee                = db.getAllAnnee();
        this.ensSemestre             = db.getAllSemestre( this.ensAnnee );
        this.ensModule               = db.getAllModule( this.ensSemestre );
        this.ensCategorieIntervenant = db.getAllCategorieIntervenant();
        this.ensIntervenant          = db.getAllIntervenant( this.ensCategorieIntervenant );
        this.ensCategorieHeure       = db.getAllCategorieHeure();
        this.ensAffectation          = db.getAllaff( this.ensIntervenant, this.ensModule, this.ensCategorieHeure );
        this.ensAttribution          = db.getAllAttribution( this.ensModule, this.ensCategorieHeure );

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

    public static boolean estCatHr(ArrayList<CategorieHeure> ensCategorie, String nomCatHr)
    {
        for (CategorieHeure catHrs : ensCategorie)
        {
            if(catHrs.getNom().equals(nomCatHr)){ return true;}
        }
        return false;
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

    public static boolean estCatInter(ArrayList<CategorieIntervenant> ensCatInter, String codeCatInter )
    {
        for (CategorieIntervenant catInter : ensCatInter)
        {
            if(catInter.getCode().equals(codeCatInter)) return true;
        }
        return false;
    }
	public Semestre               rechercheSemestreByNumero  (int numero)
	{
		for (Semestre sem : this.anneeActuelle.getSemestres())
		{
			if (sem.getNumero() == numero) return sem;
		}

		return null;
	}

    public ArrayList<Intervenant> rechercheIntervenantByText( String text )
    {
        ArrayList<Intervenant> ensTemp = new ArrayList<>();

        for (Intervenant intervenant : this.ensIntervenant)
        {
            String r = String.format(
                    "%s %s %s %s %s",
                    intervenant.getNom(),
                    intervenant.getPrenom(),
                    intervenant.getMail(),
                    intervenant.getCategorie().getNom(),
                    intervenant.getCategorie().getCode()
            ).toLowerCase();

            if (r.contains(text.toLowerCase())) ensTemp.add( intervenant );
        }

        return ensTemp;
    }

    /*-----------------------------*/
    /* Gestion de l'année actuelle */
    /*-----------------------------*/

    public Annee getAnneeActuelle() { return this.anneeActuelle; }
    public void changerAnneeActuelle( Annee a )
    {
        if (a == null) this.anneeActuelle = null;
        if (this.ensAnnee.contains(a)) this.anneeActuelle = a;
    }

    /*--------------*/
    /* Méthodes GET */
    /*--------------*/

    public ArrayList<Annee> getAnnees() { return this.ensAnnee; }
    public ArrayList<Semestre> getSemestres() { return this.ensSemestre; } // todo : à refaire en private et sans attribut d'instance
    public ArrayList<Module> getModules() { return this.ensModule; } // todo : à refaire en private et sans attribut d'instance
    public ArrayList<CategorieIntervenant> getCategorieIntervenants() { return this.ensCategorieIntervenant; }
    public ArrayList<Intervenant> getIntervenants() { return this.ensIntervenant; }
    public ArrayList<CategorieHeure> getCategorieHeures() { return this.ensCategorieHeure; }
    public ArrayList<Affectation> getAffectations() { return this.ensAffectation; } // todo : à refaire en private et sans attribut d'instance
    public ArrayList<Attribution> getAttributions() { return this.ensAttribution; } // todo : à refaire en private et sans attribut d'instance

    /*------------------*/
    /* Méthodes Ajouter */
    /*------------------*/

    // todo : à supprimer (les objets lors de leur création sont auto ajouté) & transformer en ajout interne à l'objet
    public Annee ajouterAnnee(Annee a)
    {
        if (a == null) return null;
        if (!this.ensAnnee.contains(a)) this.ensAnnee.add(a);

        this.ensAnnee.sort(Comparator.comparing(Annee::getNom));

        if (this.anneeActuelle == null) this.changerAnneeActuelle(a);

        return a;
    }

    // todo : à supprimer (les objets lors de leur création sont auto ajouté)
    public Semestre ajouterSemestre(Semestre s)
    {
        if (s == null) return null;
        if (!this.ensSemestre.contains(s)) this.ensSemestre.add(s);

        return s;
    }

    // todo : à supprimer (les objets lors de leur création sont auto ajouté)
    public Module ajouterModule(Module m)
    {
        if (m == null) return null;
        if (!this.ensModule.contains(m)) this.ensModule.add(m);

        return m;
    }

    // todo : à supprimer (les objets lors de leur création sont auto ajouté) & transformer en ajout interne à l'objet
    public Intervenant ajouterIntervenant(Intervenant i)
    {
        if (i == null) return null;
        if (!this.ensIntervenant.contains(i)) this.ensIntervenant.add(i);

        return i;
    }

    // todo : à supprimer (les objets lors de leur création sont auto ajouté) & transformer en ajout interne à l'objet
    public CategorieHeure ajouterCategorieHeure (CategorieHeure catHr)
    {
        if (catHr == null) return null;
        if (!this.ensCategorieHeure.contains(catHr)) this.ensCategorieHeure.add(catHr);

        return catHr;
    }

    // todo : à supprimer (les objets lors de leur création sont auto ajouté) & transformer en ajout interne à l'objet
    public CategorieIntervenant ajouterCategorieIntervenant( CategorieIntervenant catInter)
    {
        if (catInter == null) return null;
        if (!this.ensCategorieIntervenant.contains(catInter)) this.ensCategorieIntervenant.add(catInter);

        return catInter;
    }

    // todo : à supprimer (les objets lors de leur création sont auto ajouté)
    public Affectation ajouterAffectation(Affectation affectation)
    {
        if (affectation == null) return null;
        if (!this.ensAffectation.contains(affectation)) this.ensAffectation.add(affectation);

        return affectation;
    }

    // todo : à supprimer (les objets lors de leur création sont auto ajouté)
    public Attribution ajouterAttribution(Attribution attribution)
    {
        if (attribution == null) return null;
        if (!this.ensAttribution.contains(attribution)) this.ensAttribution.add(attribution);

        return attribution;
    }

    /*--------------------*/
    /* Méthodes Supprimer */
    /*--------------------*/

    // todo : à supprimer
    public void supprimerIntervenant   (Intervenant    i    )          { this.ensIntervenant         .remove(i)    ; }

    /*------------------------------------*/
    /* Gestion enregistrement et rollback */
    /*------------------------------------*/

    public void enregistrer()
    {
        // preparer ordre

        // appliquer

        // forcer le garbage collector
    }

    public void rollback()
    {
        // preparer ordre

        // appliquer

        // forcer le garbage collector
    }

}
