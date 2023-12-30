package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

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
    private boolean                 modifie;
    private HashMap<String, Object> rollbackDatas;


    public Semestre(int numero, int nbGrpTD, int nbGrpTP, int nbEtd, int nbSemaine, Annee annee)
    {
        this.numero    = numero;
        this.nbGrpTD   = nbGrpTD;
        this.nbGrpTP   = nbGrpTP;
        this.nbEtd     = nbEtd;
        this.nbSemaine = nbSemaine;
        this.annee     = annee.ajouterSemestre(this);

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


    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }
    public void reset() { this.ajoute = false; this.supprime = false; this.modifie = false; }


    public Semestre ajouterModule(Module module)
    {
        if (module != null && !this.ensModule.contains(module))
            this.ensModule.add(module);

        return this;
    }

    public void supprimerModule(Module module) // call uniquement si l'objet Module a deja ete supprimer a 100%
    {
        if (module == null || !this.ensModule.contains(module)) return;

        this.ensModule.remove(module);
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

    public void rollback()
    {
        if (this.rollbackDatas == null) return;

        this.nbGrpTD = (int) this.rollbackDatas.get("nbGrpTD");
        this.nbGrpTP = (int) this.rollbackDatas.get("nbGrpTP");
        this.nbEtd = (int) this.rollbackDatas.get("nbEtd");
        this.nbSemaine = (int) this.rollbackDatas.get("nbSemaine");

        this.rollbackDatas.clear();
    }

    public void setRollback()
    {
        if (this.rollbackDatas == null) this.rollbackDatas = new HashMap<>(); else this.rollbackDatas.clear();

        this.rollbackDatas.put("nbGrpTD", this.nbGrpTD);
        this.rollbackDatas.put("nbGrpTP", this.nbGrpTP);
        this.rollbackDatas.put("nbEtd", this.nbEtd);
        this.rollbackDatas.put("nbSemaine", this.nbSemaine);
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
