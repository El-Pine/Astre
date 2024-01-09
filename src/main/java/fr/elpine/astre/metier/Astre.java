package fr.elpine.astre.metier;

import com.opencsv.CSVWriter;
import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.outil.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class Astre
{
    private static Logger     logger = LoggerFactory.getLogger(Astre.class);

    private Annee anneeActuelle;

    private ArrayList<Annee>    ensAnnee;
    private ArrayList<Intervenant> ensIntervenant;
    private ArrayList<CategorieHeure> ensCategorieHeure;
    private ArrayList<CategorieIntervenant> ensCategorieIntervenant;


    public Astre ( Controleur ctrl )
    {
        DB db = ctrl.getDb();

        this.ensAnnee                   = db.getAllAnnee();
        ArrayList<Semestre> ensSemestre = db.getAllSemestre( this.ensAnnee );
        ArrayList<Module> ensModule     = db.getAllModule( ensSemestre );
        this.ensCategorieIntervenant    = db.getAllCategorieIntervenant();
        this.ensIntervenant             = db.getAllIntervenant( this.ensCategorieIntervenant );
        this.ensCategorieHeure          = db.getAllCategorieHeure();

        db.getAllAffectation( this.ensIntervenant, ensModule, this.ensCategorieHeure );
        db.getAllAttribution( ensModule, this.ensCategorieHeure );

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
    public ArrayList<CategorieIntervenant> getCategorieIntervenants() { return this.ensCategorieIntervenant; }
    public ArrayList<Intervenant> getIntervenants() { return this.ensIntervenant; }
    public ArrayList<CategorieHeure> getCategorieHeures() { return this.ensCategorieHeure; }

    /*--------------------------*/
    /* Méthodes de Verification */
    /*--------------------------*/

    public boolean existeAnnee( String nom )
    {
        for (Annee a : this.ensAnnee) if (a.getNom().equals(nom)) return true;

        return false;
    }

    public boolean existeSemestre( int numero )
    {
        if (this.anneeActuelle == null) return false;

        for (Semestre s : this.anneeActuelle.getSemestres()) if (s.getNumero() == numero) return true;

        return false;
    }

    public boolean existeModule( Semestre semestre, String code )
    {
        for (Module m : semestre.getModules()) if (m.getCode().equals(code)) return true;

        return false;
    }

    public boolean existeCatInter( String code )
    {
        for (CategorieIntervenant c : this.ensCategorieIntervenant) if (c.getCode().equals(code)) return true;

        return false;
    }

    public boolean existeCatHeure( String nom )
    {
        for (CategorieHeure c : this.ensCategorieHeure) if (c.getNom().equals(nom)) return true;

        return false;
    }

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
        if (lstAction.isEmpty()) logger.debug("Aucune actions à sauvegarder");
        else {
            logger.debug("Listes des actions de sauvegarde :\n");
            for (Action a : lstAction) logger.debug(String.valueOf(a));
        }

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
                    if (m.isModifie() && !m.isAjoute() && m.isCodeModifie()) lstAction.add( new Action(-1, m, true) );

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

    public String getDonneesCSV(String annee) {
        String nomDossierSrc = "Export";
        String nomDossier    = "CSV";
        String nomFichierCSV = "resultat-" + annee + ".csv";

        // Vérifier si le répertoire existe, sinon le créer
        File dossierSrc = new File(nomDossierSrc );
        File dossier    = new File(nomDossierSrc + "/" + nomDossier    );
        if (dossierSrc.exists()) {
            if (!dossier.exists()) {
                boolean success = dossier.mkdirs(); // Créer le répertoire si besoin
                if (!success) {
                    System.err.println("Impossible de créer le répertoire");
                    return "0";
                }
            }
        } else {
            boolean success = dossierSrc.mkdirs() && dossier.mkdirs(); // Créer le répertoire si besoin
            if (!success) {
                System.err.println("Impossible de créer le répertoire");
                return "0";
            }
        }

        File fichier = new File(nomDossier + "/" + nomFichierCSV);
        System.out.println(fichier.getPath());
        while (fichier.exists() || fichier.getPath().endsWith(".csv")) {
            nomFichierCSV =  demanderNouveauNom(nomFichierCSV);
            fichier = new File(nomDossier + "/" + nomFichierCSV);
        }

        try (FileWriter writer = new FileWriter("CSV/" + nomFichierCSV);
             CSVWriter csvWriter = new CSVWriter(writer)) {

            String[] headerRecord = {"codeCategorie", "nom", "prenom", "heureService", "heureMax", "ratioTP", "S1 Théo", "S1 Réel", "S3 Théo", "S3 Réel", "S5 Théo", "S5 Réel", "TotImp Théo", "TotImp Réel", "S2 Théo", "S2 Réel", "S4 Théo", "S4 Réel", "S6 Théo", "S6 Réel", "TotPair Théo", "TotPair Réel", "Total Théo", "Total Réel"};
            csvWriter.writeNext(headerRecord);

                for ( Intervenant intervenant : this.ensIntervenant)
                {
                    ArrayList<Double> listHT = intervenant.getHeure(true );
                    ArrayList<Double> listHR = intervenant.getHeure(false);
                    String[] data = {
                            intervenant.getCategorie().getCode(),
                            intervenant.getNom(),
                            intervenant.getPrenom(),
                            intervenant.getHeureService().toString(),
                            intervenant.getHeureMax().toString(),
                            intervenant.getRatioTP().toString(),
                            "" + listHT.get(0),
                            "" + listHR.get(0),
                            "" + listHT.get(2),
                            "" + listHR.get(2),
                            "" + listHT.get(4),
                            "" + listHR.get(4),
                            "" + (listHT.get(0)+listHT.get(2)+listHT.get(4)),
                            "" + (listHR.get(0)+listHR.get(2)+listHR.get(4)),
                            "" + listHT.get(1),
                            "" + listHR.get(1),
                            "" + listHT.get(3),
                            "" + listHR.get(3),
                            "" + listHT.get(5),
                            "" + listHR.get(5),
                            "" + (listHT.get(1)+listHT.get(3)+listHT.get(5)),
                            "" + (listHR.get(1)+listHR.get(3)+listHR.get(5)),
                            "" + (listHT.get(0)+listHT.get(1)+listHT.get(2)+listHT.get(3)+listHT.get(4)+listHT.get(5)),
                            "" + (listHR.get(0)+listHR.get(1)+listHR.get(2)+listHR.get(3)+listHR.get(4)+listHR.get(5))
                    };
                    csvWriter.writeNext(data);
                }
            return nomFichierCSV;
        }
        catch (IOException e) { logger.error("Erreur lors de la récupération des données pour le fichier csv", e); return "0"; }
    }

    private String demanderNouveauNom(String nomFichierCSV) {
        return PopUp.textInputDialog(
                nomFichierCSV,
                "Dupliquer",
                String.format("Le fichier : %s existe déjà", nomFichierCSV),
                "Nouveau nom :"
        ).showAndWait().orElse(null);
    }
}
