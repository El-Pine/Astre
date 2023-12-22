package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;

import java.util.ArrayList;

public class Semestre
{
    private int numero   ;
    private int nbGrpTD  ;
    private int nbGrpTP  ;
    private int nbEtd    ;
    private int nbSemaine;

    private Annee annee;
    private ArrayList<Module> ensModule;

    private boolean ajoute;
    private boolean supprime;
    private boolean modifie;


    public Semestre(int numero, int nbGrpTD, int nbGrpTP, int nbEtd, int nbSemaine, Annee annee)
    {
        this.numero    = numero;
        this.nbGrpTD   = nbGrpTD;
        this.nbGrpTP   = nbGrpTP;
        this.nbEtd     = nbEtd;
        this.nbSemaine = nbSemaine;
        this.annee     = annee;

        this.ensModule = new ArrayList<>();

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;
    }


    /* GETTER */

    public int   getNumero    () { return numero;    }
    public int   getNbGrpTD   () { return nbGrpTD;   }
    public int   getNbGrpTP   () { return nbGrpTP;   }
    public int   getNbEtd     () { return nbEtd;     }
    public int   getNbSemaine () { return nbSemaine; }
    public Annee getAnnee     () { return annee;     }

    /* SETTER */

    public void setNbGrpTD   ( int nbGrpTD   ) { this.nbGrpTD   = nbGrpTD; this.modifie = true;    }
    public void setNbGrpTP   ( int nbGrpTP   ) { this.nbGrpTP   = nbGrpTP; this.modifie = true;    }
    public void setNbEtd     ( int nbEtd     ) { this.nbEtd     = nbEtd; this.modifie = true;      }
    public void setNbSemaine ( int nbSemaine ) { this.nbSemaine = nbSemaine; this.modifie = true;  }

    public void setAnnee( Annee annee )
    {
        if ( annee != null )
            annee.ajouterSemestre(this);
        else if ( this.annee != null )
            this.annee.supprimerSemestre(this);

        this.annee = annee;
        this.modifie = true;
    }

    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }
    public void reset() { this.ajoute = false; this.supprime = false; this.modifie = false; }


    public void ajouterModule(Module module)
    {
        if (module != null && !this.ensModule.contains(module))
        {
            this.ensModule.add(module);

            module.setSemestre(this);
        }
    }

    public void supprimerModule(Module module)
    {
        if (module == null || !this.ensModule.contains(module)) return;

        this.ensModule.remove(module);

        module.setSemestre(null);
    }

    public ArrayList<Module> getModules() { return this.ensModule; }


    public boolean supprimer( boolean recursive )
    {
        for (Module module : this.ensModule)
            if (!recursive) return false;
            else module.supprimer(true);

        // supprimer l'élement
        return this.supprime = true;
    }


    @Override
    public String toString() {
        return "Semestre{" +
                "numero=" + numero +
                ", nbGrpTD=" + nbGrpTD +
                ", nbGrpTP=" + nbGrpTP +
                ", nbEtd=" + nbEtd +
                ", nbSemaine=" + nbSemaine +
                ", annee=" + annee.getNom() +
                ", ensModule=" + ensModule +
                '}';
    }
}
