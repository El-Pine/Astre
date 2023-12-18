package fr.elpine.astre.metier.objet;

public class Attribution
{
    private int    numeroSemestreModule;
    private int    nbHeure;
    private int    nbSemaine;

    private String anneeModule;
    private String nomCategorieHeure;
    private String codeModule;

    private Modules modules;
    private CategorieHeure catHr;


    public Attribution(int numeroSemestreModule, int nbHeure, int nbSemaine,
                       String anneeModule, String nomCategorieHeure, String codeModule,
                       Modules modules, CategorieHeure catHr)
    {
        this.numeroSemestreModule = numeroSemestreModule;
        this.nbHeure = nbHeure;
        this.nbSemaine = nbSemaine;
        this.anneeModule = anneeModule;
        this.nomCategorieHeure = nomCategorieHeure;
        this.codeModule = codeModule;
        this.modules = modules;
        this.catHr = catHr;
    }

    public int            getNumeroSemestreModule() { return numeroSemestreModule; }
    public int            getNbHeure             () { return nbHeure;              }
    public int            getNbSemaine           () { return nbSemaine;            }
    public String         getAnneeModule         () { return anneeModule;          }
    public String         getNomCategorieHeure   () { return nomCategorieHeure;    }
    public String         getCodeModule          () { return codeModule;           }
    public Modules getModule              () { return modules;               }
    public CategorieHeure getCatHr               () { return catHr;                }

    public void setNumeroSemestreModule(int numeroSemestreModule) { this.numeroSemestreModule = numeroSemestreModule; }
    public void setNbHeure             (int nbHeure             ) { this.nbHeure              = nbHeure;              }
    public void setNbSemaine           (int nbSemaine           ) { this.nbSemaine            = nbSemaine;            }
    public void setAnneeModule         (String anneeModule      ) { this.anneeModule          = anneeModule;          }
    public void setNomCategorieHeure   (String nomCategorieHeure) { this.nomCategorieHeure    = nomCategorieHeure;    }
    public void setCodeModule          (String codeModule       ) { this.codeModule           = codeModule;           }
    public void setModule              (Modules modules) { this.modules = modules;               }
    public void setCatHr               (CategorieHeure catHr    ) { this.catHr                = catHr;                }
}
