package fr.elpine.astre.metier.objet;

public class CategorieHeure
{
    private String nom;
    private double equivalentTD;
    private boolean ressource;
    private boolean sae;
    private boolean ppp;
    private boolean stage;

    public CategorieHeure(String nom, double equivalentTD,boolean ressource, boolean sae, boolean ppp, boolean stage)
    {
        this.nom           = nom;
        this.equivalentTD  = equivalentTD;
        this.ressource     = ressource;
        this.sae           = sae;
        this.ppp           = ppp;
        this.stage         = stage;
    }

    /* GETTER */

    public String getNom          () { return this.nom          ;}
    public double getEquivalentTD () { return this.equivalentTD ;}
    public boolean estRessource() { return ressource;}
    public boolean estSae      () { return sae;      }
    public boolean estPpp      () { return ppp;      }
    public boolean estStage    () { return stage;    }

    /*  SETTER   */

    public void setNom            ( String nom          ) { this.nom          = nom          ;}
    public void setEquivalentTD   ( double equivalentTD ) { this.equivalentTD = equivalentTD ;}


}
