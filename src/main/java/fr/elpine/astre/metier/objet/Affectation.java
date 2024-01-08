package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.outil.Fraction;

import java.util.HashMap;

public class Affectation
{
    private Integer nbGroupe; // avec nb semaine ou vide
    private Integer  nbSemaine; // avec nb grp ou vide
    private Fraction nbHeure; // sans nb semaine & nb grp
    private String   commentaire; // optionnel mais tjrs la au moins vide

    private CategorieHeure typeHeure; // ya tjrs
    private Intervenant intervenant;
    private Module module;
    private Integer id;

    private boolean ajoute;
    private boolean supprime;
    private boolean modifie;
    private HashMap<String, Object> rollbackDatas;

    public Affectation(Module module, Intervenant intervenant, CategorieHeure typeHeure, int nbGroupe, int nbSemaine, String commentaire)
    {
        this.id          = null;
        this.nbGroupe    = nbGroupe;
        this.nbSemaine   = nbSemaine;
        this.commentaire = commentaire;
        this.typeHeure   = typeHeure;
        this.intervenant = intervenant;
        this.module      = module;

        if (intervenant != null) intervenant.ajouterAffectation(this);
        if (module      != null) module     .ajouterAffectation(this);

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;

        this.setRollback();
    }

    public Affectation(Module module, Intervenant intervenant, CategorieHeure typeHeure, Fraction nbHeure, String commentaire)
    {
        this.id          = null;
        this.nbHeure     = nbHeure;
        this.commentaire = commentaire;
        this.typeHeure   = typeHeure;
        this.intervenant = intervenant;
        this.module      = module;

        if (intervenant != null) intervenant.ajouterAffectation(this);
        if (module      != null) module     .ajouterAffectation(this);

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;

        this.setRollback();
    }


    public boolean hasGrpAndNbSemaine() { return this.nbSemaine != null && this.nbGroupe != null; }
    public boolean hasNbHeure() { return this.nbHeure != null; }

    public int            getId          () { return id;         }
    public int            getNbGroupe    () { return nbGroupe;         }
    public int            getNbSemaine   () { return nbSemaine;        }
    public Fraction       getNbHeure     () { return this.nbHeure; }
    public String         getCommentaire () { return commentaire;      }
    public CategorieHeure getTypeHeure   () { return typeHeure;        }
    public Module         getModule      () { return this.module;      }
    public Intervenant    getIntervenant () { return this.intervenant; }


    public void setId          ( int            id          ) { this.id          = id;                                }
    public void setNbGroupe    ( int            nbGroupe    ) { this.nbGroupe    = nbGroupe; this.modifie = true;     }
    public void setNbSemaine   ( int            nbSemaine   ) { this.nbSemaine   = nbSemaine; this.modifie = true;    }
    public void setNbHeure     ( Fraction       nbHeure     ) { this.nbHeure     = nbHeure; this.modifie = true;      }
    public void setCommentaire ( String         commentaire ) { this.commentaire = commentaire; this.modifie = true;  }
    public void setTypeHeure   ( CategorieHeure typeHeure   ) { this.typeHeure   = typeHeure; this.modifie = true;    }

    /* Calculs */
    public double getTotalEqtd( boolean avecEqtd ) {
        if (this.hasNbHeure()) return this.nbHeure.value() * (avecEqtd ? this.typeHeure.getEquivalentTD().value() : 1);
        else {
            Attribution att = this.module.getAttribution( this.typeHeure );

            return this.nbSemaine * this.nbGroupe * ( att == null ? 1 : ( att.hasNbSemaine() ? att.getNbSemaine() : 1 ) ) * (avecEqtd ? this.typeHeure.getEquivalentTD().value() : 1);
        }
    }

    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }
    public void reset() { this.ajoute = false; this.supprime = false; this.modifie = false; this.setRollback(); }


    public void supprimer()
    {
        // if (this.intervenant != null) this.intervenant.supprimerAffectation(this);
        // if (this.module      != null) this.module     .supprimerAffectation(this);

        // supprimer l'élement
        this.supprime = true;
    }

    public void rollback()
    {
        if (this.rollbackDatas == null) return;

        this.nbGroupe    = (Integer)        this.rollbackDatas.get("nbGroupe");
        this.nbSemaine   = (Integer)        this.rollbackDatas.get("nbSemaine");
        this.nbHeure     = (Fraction)       this.rollbackDatas.get("nbHeure");
        this.commentaire = (String)         this.rollbackDatas.get("commentaire");
        this.typeHeure   = (CategorieHeure) this.rollbackDatas.get("typeHeure");

        this.rollbackDatas.clear();
    }

    private void setRollback()
    {
        if (this.rollbackDatas == null) this.rollbackDatas = new HashMap<>(); else this.rollbackDatas.clear();

        this.rollbackDatas.put("nbGroupe",    this.nbGroupe);
        this.rollbackDatas.put("nbSemaine",   this.nbSemaine);
        this.rollbackDatas.put("nbHeure",     this.nbHeure);
        this.rollbackDatas.put("commentaire", this.commentaire);
        this.rollbackDatas.put("typeHeure",   this.typeHeure);
    }


    @Override
    public String toString() {
        return "Affectation{" +
                "nbGroupe=" + nbGroupe +
                ", nbSemaine=" + nbSemaine +
                ", nbHeure=" + nbHeure +
                ", commentaire='" + commentaire + '\'' +
                ", typeHeure=" + typeHeure +
                ", intervenant=" + intervenant +
                ", module=" + module.getCode() +
                '}';
    }
}
