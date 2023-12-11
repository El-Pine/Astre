package fr.elpine.astre.metier.objet;

public class CategorieIntervenant //Statut
{
    private String code;
    private String nom;
    private int nbHeureMax;
    private int service;
    private double ratioTd;
    private boolean estCompleter;


    public CategorieIntervenant(String code, String nom, int nbHeureMax, int service, double ratioTd, boolean estCompleter)
    {
        this.code         = code;
        this.nom          = nom;
        this.nbHeureMax   = nbHeureMax;
        this.service      = service;
        this.ratioTd      = ratioTd;
        this.estCompleter = estCompleter;
    }

    /*   GETTER    */

    public String getCode        () { return code         ;}
    public String getNom         () { return nom          ;}
    public int getNbHeureMax     () { return nbHeureMax   ;}
    public int getService        () { return service      ;}
    public double getRatioTd     () { return ratioTd      ;}
    public boolean estCompleter  () { return estCompleter ;}

    /*   SETTER   */

    public void setCode         ( String code          ) { this.code         = code         ;}
    public void setNom          ( String nom           ) { this.nom          = nom          ;}
    public void setNbHeureMax   ( int nbHeureMax       ) { this.nbHeureMax   = nbHeureMax   ;}
    public void setService      ( int service          ) { this.service      = service      ;}
    public void setRatioTd      ( double ratioTd       ) { this.ratioTd      = ratioTd      ;}
    public void setEstCompleter ( boolean estCompleter ) { this.estCompleter = estCompleter ;}
}
