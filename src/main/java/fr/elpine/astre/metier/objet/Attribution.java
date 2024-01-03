package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.outil.Fraction;

import java.util.HashMap;

public class Attribution
{
    private Fraction nbHeure;
    private Integer  nbSemaine;

    private Module module;
    private CategorieHeure catHr;

    private boolean                 ajoute;
    private boolean                 supprime;
    private boolean                 modifie;
    private HashMap<String, Object> rollbackDatas;


    public Attribution(Fraction nbHeure, int nbSemaine, Module module, CategorieHeure catHr)
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

        this.setRollback();
    }

    public Attribution(Fraction nbHeure, Module module, CategorieHeure catHr)
    {
        this.nbHeure   = nbHeure;
        this.module    = module;
        this.catHr     = catHr;

        if (module != null) module.ajouterAttribution(this);
        if (catHr  != null) catHr .ajouterAttribution(this);

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;

        this.setRollback();
    }

    public boolean        hasNbSemaine           () { return this.nbSemaine != null; }
    public Fraction       getNbHeure             () { return nbHeure;                }
    public int            getNbSemaine           () { return nbSemaine;              }
    public Module         getModule              () { return module;                 }
    public CategorieHeure getCatHr               () { return catHr;                  }

    public void setNbHeure   (Fraction nbHeure ) { this.nbHeure   = nbHeure;   this.modifie = true; }
    public void setNbSemaine (int nbSemaine    ) { this.nbSemaine = nbSemaine; this.modifie = true; }

    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }
    public void reset() { this.ajoute = false; this.supprime = false; this.modifie = false; this.setRollback(); }


    public void supprimer()
    {
        //if (this.module != null) this.module.supprimerAttribution(this);
        //if (this.catHr  != null) this.catHr .supprimerAttribution(this);

        // supprimer l'élement
        this.supprime = true;
    }

    public void rollback()
    {
        if (this.rollbackDatas == null) return;

        this.nbSemaine   = (Integer)  this.rollbackDatas.get("nbSemaine");
        this.nbHeure     = (Fraction) this.rollbackDatas.get("nbHeure");

        this.rollbackDatas.clear();
    }

    private void setRollback()
    {
        if (this.rollbackDatas == null) this.rollbackDatas = new HashMap<>(); else this.rollbackDatas.clear();

        this.rollbackDatas.put("nbSemaine", this.nbSemaine);
        this.rollbackDatas.put("nbHeure",   this.nbHeure);
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
