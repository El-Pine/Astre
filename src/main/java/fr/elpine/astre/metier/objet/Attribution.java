package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.outil.Fraction;

import java.util.HashMap;

public class Attribution
{
    private Fraction nbHeurePN;
    private Fraction nbHeure;
    private Integer  nbSemaine;

    private final Module module;
    private final CategorieHeure catHr;

    private boolean                 ajoute;
    private boolean                 supprime;
    private boolean                 modifie;
    private HashMap<String, Object> rollbackDatas;


    public Attribution(Fraction nbHeurePN, Fraction nbHeure, int nbSemaine, Module module, CategorieHeure catHr)
    {
        this.nbHeurePN = catHr.getNom().equals("HP") ? Fraction.valueOf("1") : nbHeurePN;
        this.nbHeure   = nbHeure;
        this.nbSemaine = nbSemaine;
        this.module    = module;
        this.catHr     = catHr;

        if (module != null) module.ajouterAttribution(this);
        catHr .ajouterAttribution(this);

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;

        this.setRollback();
    }

    public Attribution(Fraction nbHeurePN, Fraction nbHeure, Module module, CategorieHeure catHr)
    {
        this.nbHeurePN = nbHeurePN;
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
    public Fraction       getNbHeurePN           () { return nbHeurePN;              }
    public Fraction       getNbHeure             () { return nbHeure;                }
    public int            getNbSemaine           () { return nbSemaine;              }
    public Module         getModule              () { return module;                 }
    public CategorieHeure getCatHr               () { return catHr;                  }

    public void setNbHeurePN (Fraction nbHeurePN ) { this.nbHeurePN = nbHeurePN; this.modifState(); }
    public void setNbHeure   (Fraction nbHeure   ) { this.nbHeure   = nbHeure;   this.modifState(); }
    public void setNbSemaine (int nbSemaine      ) { this.nbSemaine = nbSemaine; this.modifState(); }

    private void modifState()
    {
	    this.modifie = !((Fraction) this.rollbackDatas.get("nbHeurePN")).equals(this.nbHeurePN);
        if ( !((Fraction) this.rollbackDatas.get("nbHeure")).equals(this.nbHeure) ) this.modifie = true;
        if ( this.hasNbSemaine() )
            if ( !this.rollbackDatas.get("nbSemaine").equals(this.nbSemaine) ) this.modifie = true;
    }

    /* Calculs */

    private int getNbGroupe()
    {
        Semestre s     = this.module.getSemestre();
        int      nbGrp = 1;

        if (this.catHr.getNom().equals("TD")) nbGrp = s.getNbGrpTD();
        if (this.catHr.getNom().equals("TP")) nbGrp = s.getNbGrpTP();

        return nbGrp;
    }

    public double getNbHeurePNPromo() {

        return this.nbHeurePN.value() * this.catHr.getEquivalentTD().value() * this.getNbGroupe();
    }

    public double getNbHeureEtd()
    {
        return this.hasNbSemaine() ? this.nbSemaine * this.nbHeure.value() : this.nbHeure.value();
    }

    public double getNbHeurePromo()
    {
        return this.getNbHeureEtd() * this.catHr.getEquivalentTD().value() * this.getNbGroupe();
    }

    public double getNbHeureAffecte()
    {
        double nb = 0.0;

        for (Affectation aff : this.module.getAffectations())
            if ( aff.getTypeHeure() == this.catHr ) nb += aff.getTotalEqtd();

        return nb;
    }


    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }
    public void reset() { this.ajoute = false; this.supprime = false; this.modifie = false; this.setRollback(); }


    public void supprimer()
    {
        // supprimer l'élement
        this.supprime = true;
    }
    public void reactiver()
    {
        // supprimer l'élement
        this.supprime = false;
    }

    public void rollback()
    {
        if (this.rollbackDatas == null) return;

        this.nbSemaine   = (Integer)  this.rollbackDatas.get("nbSemaine");
        this.nbHeure     = (Fraction) this.rollbackDatas.get("nbHeure");
        this.nbHeurePN   = (Fraction) this.rollbackDatas.get("nbHeurePN");

        this.rollbackDatas.clear();
    }

    private void setRollback()
    {
        if (this.rollbackDatas == null) this.rollbackDatas = new HashMap<>(); else this.rollbackDatas.clear();

        this.rollbackDatas.put("nbSemaine", this.nbSemaine);
        this.rollbackDatas.put("nbHeure",   this.nbHeure);
        this.rollbackDatas.put("nbHeurePN", this.nbHeurePN);
    }


    @Override
    public String toString() {
        return "Attribution{" +
                "nbHeure=" + nbHeure +
                ", nbHeurePN=" + nbHeurePN +
                ", nbSemaine=" + nbSemaine +
                ", module=" + module.getCode() +
                ", catHr=" + catHr +
                '}';
    }
}
