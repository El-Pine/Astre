package fr.elpine.astre.metier.objet;

public class SAE extends Module
{
    private int nbHeurePnSemestre;
    private int nbHeureTut;
    private int nbHeureSAE;// nb d'heure que dure la sae

    public SAE(String nom, String code, String commentaire, int nbHeurePnSem, int nbHeureTut, int nbHeure)
    {
        super(nom, code, commentaire);
        this.nbHeurePnSemestre = nbHeurePnSem;
        this.nbHeureTut   = nbHeureTut;
        this.nbHeureSAE      = nbHeure;
    }

    /*   GETTER    */
    public int getNbHeurePnSem() { return nbHeurePnSemestre ;}
    public int getNbHeureTut  () { return nbHeureTut   ;}
    public int getNbHeure     () { return nbHeureSAE      ;}

    /*   SETTER   */
    public void setNbHeurePnSem ( int nbHeurePnSem) { this.nbHeurePnSemestre = nbHeurePnSem;}
    public void setNbHeureTut   ( int nbHeureTut  ) { this.nbHeureTut   = nbHeureTut  ;}
    public void setNbHeure      ( int nbHeure     ) { this.nbHeureSAE      = nbHeure     ;}
}
