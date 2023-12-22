package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;

public class Attribution
{
    private int        nbHeure;
    private Integer    nbSemaine;

    private Module module;
    private CategorieHeure catHr;

    private boolean ajoute;
    private boolean supprime;
    private boolean modifie;


    public Attribution(int nbHeure, int nbSemaine, Module module, CategorieHeure catHr)
    {
        this.nbHeure   = nbHeure;
        this.nbSemaine = nbSemaine;
        this.module    = module;
        this.catHr     = catHr;

        if (module != null) module.ajouterAttribution(this);
        if (catHr  != null) catHr .ajouterAttribution(this);

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;
    }

    public Attribution(int nbHeure, Module module, CategorieHeure catHr)
    {
        this.nbHeure   = nbHeure;
        this.module    = module;
        this.catHr     = catHr;

        if (module != null) module.ajouterAttribution(this);
        if (catHr  != null) catHr .ajouterAttribution(this);

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;
    }

    public boolean        hasNbSemaine           () { return this.nbSemaine != null; }
    public int            getNbHeure             () { return nbHeure;                }
    public int            getNbSemaine           () { return nbSemaine;              }
    public Module         getModule              () { return module;                 }
    public CategorieHeure getCatHr               () { return catHr;                  }

    public void setNbHeure             (int nbHeure             ) { this.nbHeure              = nbHeure;    this.modifie = true;            }
    public void setNbSemaine           (int nbSemaine           ) { this.nbSemaine            = nbSemaine;  this.modifie = true;            }

    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }
    public void reset() { this.ajoute = false; this.supprime = false; this.modifie = false; }


    public void supprimer()
    {
        //if (this.module != null) this.module.supprimerAttribution(this);
        //if (this.catHr  != null) this.catHr .supprimerAttribution(this);

        // supprimer l'élement
        this.supprime = true;
    }


    @Override
    public String toString() {
        return "Attribution{" +
                "nbHeure=" + nbHeure +
                ", nbSemaine=" + nbSemaine +
                ", module=" + module.getCode() +
                ", catHr=" + catHr +
                '}';
    }
}
