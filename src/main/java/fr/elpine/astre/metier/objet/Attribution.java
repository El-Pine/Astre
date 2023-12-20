package fr.elpine.astre.metier.objet;

public class Attribution
{
    private int        nbHeure;
    private Integer    nbSemaine;

    private Module         module;
    private CategorieHeure catHr;


    public Attribution(int nbHeure, int nbSemaine, Module module, CategorieHeure catHr)
    {
        this.nbHeure   = nbHeure;
        this.nbSemaine = nbSemaine;
        this.module    = module;
        this.catHr     = catHr;

        if (module != null) module.ajouterAttribution(this);
        if (catHr  != null) catHr .ajouterAttribution(this);
    }

    public Attribution(int nbHeure, Module module, CategorieHeure catHr)
    {
        this.nbHeure   = nbHeure;
        this.module    = module;
        this.catHr     = catHr;

        if (module != null) module.ajouterAttribution(this);
        if (catHr  != null) catHr .ajouterAttribution(this);
    }

    public boolean hasNbSemaine() { return this.nbSemaine != null; }

    public int            getNbHeure             () { return nbHeure;              }
    public int            getNbSemaine           () { return nbSemaine;            }
    public Module getModule              () { return module;               }
    public CategorieHeure getCatHr               () { return catHr;                }

    public void setNbHeure             (int nbHeure             ) { this.nbHeure              = nbHeure;              }
    public void setNbSemaine           (int nbSemaine           ) { this.nbSemaine            = nbSemaine;            }

    public void supprimer()
    {
        if (this.module != null) this.module.supprimerAttribution(this);
        if (this.catHr  != null) this.catHr .supprimerAttribution(this);
    }
}
