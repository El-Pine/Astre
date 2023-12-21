package fr.elpine.astre.metier.objet;

public class CategorieIntervenant
{
    private String code;
    private String nom;
    private int nbHeureMaxDefault;
    private int nbHeureServiceDefault;
    private double ratioTPDefault;


    public CategorieIntervenant(String code, String nom, int nbHeureMaxDefault, int nbHeureServiceDefault, double ratioTPDefault)
    {
        this.code         = code;
        this.nom          = nom;
        this.nbHeureMaxDefault   = nbHeureMaxDefault;
        this.nbHeureServiceDefault      = nbHeureServiceDefault;
        this.ratioTPDefault      = ratioTPDefault;
    }

    /*   GETTER    */

    public String getCode        () { return code         ;}
    public String getNom         () { return nom          ;}
     public int getNbHeureMaxDefault     () { return nbHeureMaxDefault   ;}
    public int getNbHeureServiceDefault        () { return nbHeureServiceDefault      ;}
    public double getRatioTPDefault     () { return ratioTPDefault      ;}


    /*   SETTER   */

    public void setCode         ( String code          ) { this.code         = code         ;}
    public void setNom          ( String nom           ) { this.nom          = nom          ;}
    public void setNbHeureMaxDefault   ( int nbHeureMaxDefault       ) { this.nbHeureMaxDefault   = nbHeureMaxDefault   ;}
    public void setNbHeureServiceDefault      ( int nbHeureServiceDefault          ) { this.nbHeureServiceDefault      = nbHeureServiceDefault      ;}
    public void setRatioTPDefault      ( double ratioTPDefault       ) { this.ratioTPDefault      = ratioTPDefault      ;}

    public String toString()
    {
        return this.getNom();
    }

}
