package fr.elpine.astre.metier.objet;

public class Affectation
{
    private Integer nbGroupe; // avec nb semaine ou vide
    private Integer nbSemaine; // avec nb grp ou vide
    private Integer nbHeure; // sans nb semaine & nb grp
    private String commentaire; // optionnel mais tjrs la au moins vide

    private CategorieHeure typeHeure; // ya tjrs
    private Intervenant intervenant;
    private Module module;

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


    public void setNbGroupe    ( int            nbGroupe    ) { this.nbGroupe    = nbGroupe;    }
    public void setNbSemaine   ( int            nbSemaine   ) { this.nbSemaine   = nbSemaine;   }
    public void setNbHeure     ( int            nbHeure     ) { this.nbHeure     = nbHeure;     }
    public void setCommentaire ( String         commentaire ) { this.commentaire = commentaire; }
    public void setTypeHeure   ( CategorieHeure typeHeure   ) { this.typeHeure   = typeHeure;   }


    public void supprimer()
    {
        if (this.intervenant != null) this.intervenant.supprimerAffectation(this);
        if (this.module      != null) this.module     .supprimerAffectation(this);
    }
}
