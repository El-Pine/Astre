package fr.elpine.astre.objet;

public class SAE extends Module
{
    private int nbHeurePnSem;
    private int nbHeureTut;
    private int nbHeure;

    public SAE(String nom, String code, String commentaire, int nbHeurePnSem, int nbHeureTut, int nbHeure)
    {
        super(nom, code, commentaire);
        this.nbHeurePnSem = nbHeurePnSem;
        this.nbHeureTut   = nbHeureTut;
        this.nbHeure      = nbHeure;
    }

    public int getNbHeurePnSem() { return nbHeurePnSem ;}
    public int getNbHeureTut  () { return nbHeureTut   ;}
    public int getNbHeure     () { return nbHeure      ;}
}
