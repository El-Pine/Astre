package fr.elpine.astre.metier;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.outil.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Astre
{
    private static Logger     logger = LoggerFactory.getLogger(Astre.class);

    private        Controleur ctrl;
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
        this.ensAffectation          = db.getAllAffectation( this.ensIntervenant, this.ensModule, this.ensCategorieHeure );
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

    public static boolean estCatHr(ArrayList<Affectation> ensAffectation, String nomCatHr)
    {
        for (Affectation aff: ensAffectation)
        {
            if(aff.getTypeHeure().getNom().equals(nomCatHr))
                return false;
        }
        return true;
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

    public static boolean estCatInter(ArrayList<Affectation> ensAffectation, String codeCatInter )
    {
        for (Affectation aff : ensAffectation)
        {
            if(aff.getIntervenant().getCategorie().getCode().equals(codeCatInter)) return false;
        }
        return true;
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

    public void supprimerCatHr         (CategorieHeure catHr) {this.ensCategorieHeure.remove(catHr); catHr.supprimer(false);}

    public void supprimerCatInter  (CategorieIntervenant catInter) {this.ensCategorieIntervenant.remove(catInter); catInter.supprimer(false);}

    /*------------------------------------*/
    /* Gestion enregistrement et rollback */
    /*------------------------------------*/

    public void enregistrer() { this.validation( false ); }
    public void rollback()    { this.validation( true  ); }

    private void validation( boolean rollback )
    {
        // preparer l'ordre d'exécution
        ArrayList<Action> lstAction = rollback ? ordreExecutionRollback() : ordreExecution();


        // débug
        logger.debug("Listes des actions de sauvegarde :\n");
        for (Action a : lstAction) logger.debug(String.valueOf(a));


        // appliquer l'ordre d'exécution
        for (Action a : lstAction) a.executer( rollback );
    }

    private ArrayList<Action> ordreExecution()
    {
        ArrayList<Action> lstAction = new ArrayList<>();

        /*
         * actions normal :
         *   supprimer           -> supprimer
         *   ajouter             -> ajouter
         *   modifier            -> modifier (modif code au top)
         *   supprimer, ajouter  -> supprimer (rien dans db)
         *   ajouter, modifier   -> ajouter
         *   supprimer, modifier -> supprimer (modif code au top)
         *   les 3               -> supprimer (rien dans db)
         *
         * */

        for (Annee a : this.ensAnnee)
        {
            if (a.isSupprime())     lstAction.add( new Action(0, a, !a.isAjoute()) );
            else if (a.isAjoute())  lstAction.add( new Action(1, a, true) );
            else if (a.isModifie()) lstAction.add( new Action(2, a, true) );

            for (Semestre s : a.getSemestres())
            {
                if (s.isSupprime())     lstAction.add( new Action(0, s, !s.isAjoute()) );
                else if (s.isAjoute())  lstAction.add( new Action(1, s, true) );
                else if (s.isModifie()) lstAction.add( new Action(2, s, true) );

                for (Module m : s.getModules())
                {
                    if (m.isModifie() && !m.isAjoute()) lstAction.add( new Action(-1, m, true) );

                    if (m.isSupprime())     lstAction.add( new Action(0, m, !m.isAjoute()) );
                    else if (m.isAjoute())  lstAction.add( new Action(1, m, true) );
                    else if (m.isModifie()) lstAction.add( new Action(2, m, true) );

                    for (Affectation aff : m.getAffectations())
                    {
                        if (aff.isSupprime())     lstAction.add( new Action(0, aff, !aff.isAjoute()) );
                        else if (aff.isAjoute())  lstAction.add( new Action(1, aff, true) );
                        else if (aff.isModifie()) lstAction.add( new Action(2, aff, true) );
                    }

                    for (Attribution att : m.getAttributions())
                    {
                        if (att.isSupprime())     lstAction.add( new Action(0, att, !att.isAjoute()) );
                        else if (att.isAjoute())  lstAction.add( new Action(1, att, true) );
                        else if (att.isModifie()) lstAction.add( new Action(2, att, true) );
                    }
                }
            }
        }

        for (CategorieHeure catHr : this.ensCategorieHeure)
        {
            if (catHr.isSupprime())     lstAction.add( new Action(0, catHr, !catHr.isAjoute()) );
            else if (catHr.isAjoute())  lstAction.add( new Action(1, catHr, true) );
            else if (catHr.isModifie()) lstAction.add( new Action(2, catHr, true) );
        }

        for (CategorieIntervenant catInt : this.ensCategorieIntervenant)
        {
            if (catInt.isSupprime())     lstAction.add( new Action(0, catInt, !catInt.isAjoute()) );
            else if (catInt.isAjoute())  lstAction.add( new Action(1, catInt, true) );
            else if (catInt.isModifie()) lstAction.add( new Action(2, catInt, true) );
        }

        for (Intervenant i : this.ensIntervenant)
        {
            if (i.isSupprime())     lstAction.add( new Action(0, i, !i.isAjoute()) );
            else if (i.isAjoute())  lstAction.add( new Action(1, i, true) );
            else if (i.isModifie()) lstAction.add( new Action(2, i, true) );
        }

        Collections.sort(lstAction);

        return lstAction;
    }

    private ArrayList<Action> ordreExecutionRollback()
    {
        ArrayList<Action> lstAction = new ArrayList<>();

        /*
         * actions rollback :
         *   supprimer           -> reset (2)
         *   ajouter             -> del obj (0)
         *   modifier            -> rollback + reset (1)
         *   supprimer, ajouter  -> del obj
         *   ajouter, modifier   -> del obj
         *   supprimer, modifier -> rollback + reset
         *   les 3               -> del obj
         *
         * */

        for (Annee a : this.ensAnnee)
        {
            if (a.isAjoute())        lstAction.add( new Action(0, a, false ) );
            else if (a.isModifie())  lstAction.add( new Action(1, a, false ) );
            else if (a.isSupprime()) lstAction.add( new Action(2, a, false ) ); // faire pareil partout

            for (Semestre s : a.getSemestres())
            {
                if (s.isAjoute())        lstAction.add( new Action(0, s, false ) );
                else if (s.isModifie())  lstAction.add( new Action(1, s, false ) );
                else if (s.isSupprime()) lstAction.add( new Action(2, s, false ) );

                for (Module m : s.getModules())
                {
                    if (m.isAjoute())        lstAction.add( new Action(0, m, false ) );
                    else if (m.isModifie())  lstAction.add( new Action(1, m, false ) );
                    else if (m.isSupprime()) lstAction.add( new Action(2, m, false ) );

                    for (Affectation aff : m.getAffectations())
                    {
                        if (aff.isAjoute())        lstAction.add( new Action(0, aff, false ) );
                        else if (aff.isModifie())  lstAction.add( new Action(1, aff, false ) );
                        else if (aff.isSupprime()) lstAction.add( new Action(2, aff, false ) );
                    }

                    for (Attribution att : m.getAttributions())
                    {
                        if (att.isAjoute())        lstAction.add( new Action(0, att, false ) );
                        else if (att.isModifie())  lstAction.add( new Action(1, att, false ) );
                        else if (att.isSupprime()) lstAction.add( new Action(2, att, false ) );
                    }
                }
            }
        }

        for (CategorieHeure catHr : this.ensCategorieHeure)
        {
            if (catHr.isAjoute())        lstAction.add( new Action(0, catHr, false ) );
            else if (catHr.isModifie())  lstAction.add( new Action(1, catHr, false ) );
            else if (catHr.isSupprime()) lstAction.add( new Action(2, catHr, false ) );
        }

        for (CategorieIntervenant catInt : this.ensCategorieIntervenant)
        {
            if (catInt.isAjoute())        lstAction.add( new Action(0, catInt, false ) );
            else if (catInt.isModifie())  lstAction.add( new Action(1, catInt, false ) );
            else if (catInt.isSupprime()) lstAction.add( new Action(2, catInt, false ) );
        }

        for (Intervenant i : this.ensIntervenant)
        {
            if (i.isAjoute())        lstAction.add( new Action(0, i, false ) );
            else if (i.isModifie())  lstAction.add( new Action(1, i, false ) );
            else if (i.isSupprime()) lstAction.add( new Action(2, i, false ) );
        }

        Collections.sort(lstAction);

        return lstAction;
    }
}
