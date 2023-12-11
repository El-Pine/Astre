package fr.elpine.astre.metier.objet;

public class CategorieHeure
{
    private String nom;
    private double equivalentTD;

    public CategorieHeure(String nom, double equivalentTD)
    {
        this.nom           = nom;
        this.equivalentTD = equivalentTD;
    }

    /* GETTER */

    public String getNom          () { return this.nom          ;}
    public double getEquivalentTD () { return this.equivalentTD ;}

    /*  SETTER   */

    public void setNom            ( String nom          ) { this.nom          = nom          ;}
    public void setEquivalentTD   ( double equivalentTD ) { this.equivalentTD = equivalentTD ;}
}
