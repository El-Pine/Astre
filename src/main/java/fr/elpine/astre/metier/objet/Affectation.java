package fr.elpine.astre.metier.objet;

public class Affectation
{

    private Module codeModule;
    private int    numeroSemestreModule;
    private String anneeModule;
    private Intervenant   inter;
    private CategorieHeure typeHeure;
    private int nbGroupe;
    private int nbSemaine;
    private int nbHeure;
    private String commentaire;

    public Affectation( Module codeModule, int numeroSemestreModule, String anneeModule,
                        Intervenant inter, CategorieHeure typeHeure, int nbGroupe, int nbSemaine,
                       int nbHeure, String commentaire)
    {
        this.codeModule           = codeModule;
        this.numeroSemestreModule = numeroSemestreModule;
        this.anneeModule          = anneeModule;
        this.inter                = inter;
        this.typeHeure            = typeHeure;
        this.nbGroupe             = nbGroupe;
        this.nbSemaine            = nbSemaine;
        this.nbHeure              = nbHeure;
        this.commentaire          = commentaire;
    }


    public CategorieHeure getTypeHeure   () { return typeHeure;   }
    public int            getNbGroupe    () { return nbGroupe;    }
    public int            getNbSemaine   () { return nbSemaine;   }
    public int            getNbHeure     () { return nbHeure;     }
    public String         getCommentaire () { return commentaire; }


    public void setTypeHeure   ( CategorieHeure typeHeure   ) { this.typeHeure   = typeHeure;   }
    public void setNbGroupe    ( int            nbGroupe    ) { this.nbGroupe    = nbGroupe;    }
    public void setNbSemaine   ( int            nbSemaine   ) { this.nbSemaine   = nbSemaine;   }
    public void setNbHeure     ( int            nbHeure     ) { this.nbHeure     = nbHeure;     }
    public void setCommentaire ( String         commentaire ) { this.commentaire = commentaire; }

    public String getCodeModule          () { return codeModule.getCode();}
    public int    getNumeroSemestreModule() { return numeroSemestreModule;}
    public String getAnneeModule         () { return anneeModule;         }
    public Intervenant  getIdInter             () { return inter;             }
}
