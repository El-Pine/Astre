package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;

public class Affectation
{
    private Integer nbGroupe; // avec nb semaine ou vide
    private Integer nbSemaine; // avec nb grp ou vide
    private Integer nbHeure; // sans nb semaine & nb grp
    private String commentaire; // optionnel mais tjrs la au moins vide

    private CategorieHeure typeHeure; // ya tjrs
    private Intervenant intervenant;
    private Module module;

    private boolean ajoute;
    private boolean supprime;
    private boolean modifie;

    public Affectation(Module module, Intervenant intervenant, CategorieHeure typeHeure, int nbGroupe, int nbSemaine, String commentaire)
    {
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
    }

    public Affectation(Module module, Intervenant intervenant, CategorieHeure typeHeure, int nbHeure, String commentaire)
    {
        this.nbHeure     = nbHeure;
        this.commentaire = commentaire;
        this.typeHeure   = typeHeure;
        this.intervenant = intervenant;
        this.module      = module;

        if (intervenant != null) intervenant.ajouterAffectation(this);
        if (module      != null) module     .ajouterAffectation(this);

        this.ajoute = Controleur.get().getMetier() != null;
    }


    public boolean hasGrpAndNbSemaine() { return this.nbSemaine != null && this.nbGroupe != null; }
    public boolean hasNbHeure() { return this.nbHeure != null; }

    public int            getNbGroupe    () { return nbGroupe;         }
    public int            getNbSemaine   () { return nbSemaine;        }
    public int            getNbHeure     () { return nbHeure;          }
    public String         getCommentaire () { return commentaire;      }
    public CategorieHeure getTypeHeure   () { return typeHeure;        }
    public Module         getModule      () { return this.module;      }
    public Intervenant    getIntervenant () { return this.intervenant; }


    public void setNbGroupe    ( int            nbGroupe    ) { this.nbGroupe    = nbGroupe; this.modifie = true;     }
    public void setNbSemaine   ( int            nbSemaine   ) { this.nbSemaine   = nbSemaine; this.modifie = true;    }
    public void setNbHeure     ( int            nbHeure     ) { this.nbHeure     = nbHeure; this.modifie = true;      }
    public void setCommentaire ( String         commentaire ) { this.commentaire = commentaire; this.modifie = true;  }
    public void setTypeHeure   ( CategorieHeure typeHeure   ) { this.typeHeure   = typeHeure; this.modifie = true;    }

    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }


    public void supprimer()
    {
        if (this.intervenant != null) this.intervenant.supprimerAffectation(this);
        if (this.module      != null) this.module     .supprimerAffectation(this);
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
                ", module=" + module +
                '}';
    }
}
