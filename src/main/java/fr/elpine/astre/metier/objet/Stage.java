package fr.elpine.astre.metier.objet;

public class Stage extends Module
{
    private int nbHeureREH;
    private int nbHeureTut;
    private int nbHeure;

    public Stage(String nom, String code, String commentaire, int nbHeureREH, int nbHeureTut, int nbHeure)
    {
        super(nom, code, commentaire);
        this.nbHeureREH = nbHeureREH;
        this.nbHeureTut = nbHeureTut;
        this.nbHeure = nbHeure;
    }

    /*   GETTER    */

    public int getNbHeureREH () { return nbHeureREH ;}
    public int getNbHeureTut () { return nbHeureTut ;}
    public int getNbHeure    () { return nbHeure    ;}

    /*   SETTER   */

    public void setNbHeureREH ( int nbHeureREH ) { this.nbHeureREH = nbHeureREH;}
    public void setNbHeureTut ( int nbHeureTut ) { this.nbHeureTut = nbHeureTut;}
    public void setNbHeure    ( int nbHeure    ) { this.nbHeure    = nbHeure;}
}
