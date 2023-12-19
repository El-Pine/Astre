package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;

import java.util.ArrayList;
import java.util.HashMap;

public class Semestre {

    private int numero   ;
    private int nbGrpTD  ;
    private int nbGrpTP  ;
    private int nbEtd    ;
    private int nbSemaine;
    private Annee annee;

    private ArrayList<Modules> ensModule;


    public Semestre(int numero, int nbGrpTD, int nbGrpTP, int nbEtd, int nbSemaine,Annee annee) {
        this.numero    = numero;
        this.nbGrpTD   = nbGrpTD;
        this.nbGrpTP   = nbGrpTP;
        this.nbEtd     = nbEtd;
        this.nbSemaine = nbSemaine;
        this.annee     = annee;
        this.ensModule = new ArrayList<>();
    }

    /* GETTER */

    public int   getNumero   () { return numero;    }
    public int   getNbGrpTD  () { return nbGrpTD;   }
    public int   getNbGrpTP  () { return nbGrpTP;   }
    public int   getNbEtd    () { return nbEtd;     }
    public int   getNbSemaine() { return nbSemaine; }
    public Annee getAnnee    () { return annee;     }

    /* SETTER */

    public void setNumero   (int numero   ) { this.numero    = numero;    }
    public void setNbGrpTD  (int nbGrpTD  ) { this.nbGrpTD   = nbGrpTD;   }
    public void setNbGrpTP  (int nbGrpTP  ) { this.nbGrpTP   = nbGrpTP;   }
    public void setNbEtd    (int nbEtd    ) { this.nbEtd     = nbEtd;     }
    public void setNbSemaine(int nbSemaine) { this.nbSemaine = nbSemaine; }
    public void setAnnee    (Annee annee  ) { this.annee = annee;         }

    public void ajouterModule(Modules mod)
    {
        if(mod != null)
            this.ensModule.add(mod);
    }
    public Modules getModule(int i) { return this.ensModule.get(i);}
}
