package fr.elpine.astre.metier.objet;

import java.util.HashMap;

public class Ressource extends Module
{
    private int nbHeurePn;
    private int nbHeurePnCours;
    private HashMap<String,Integer> heureSemestre;
    private int nbHeuretl;

    public Ressource(String nom, String code, String commentaire, int nbHeurePn, int nbHeurePnCours, HashMap<String, Integer> heureSemestre, int nbHeuretl)
    {
        super(nom, code, commentaire);
        this.nbHeurePn      = nbHeurePn; // nb d'heure du programme nationnal
        this.nbHeurePnCours = nbHeurePnCours; //
        this.heureSemestre  = heureSemestre; // nb d'heure par semestre
        this.nbHeuretl      = nbHeuretl;
    }

    /*   GETTER    */

    public int getNbHeurePn                         () { return nbHeurePn      ;}
    public int getNbHeurePnCours                    () { return nbHeurePnCours ;}
    public HashMap<String, Integer> getHeureSemestre() { return heureSemestre  ;}

    //public int getNbHeuretl       (                                      )  { return nbHeuretll                    ;}

    /*   SETTER   */

    public void setNbHeurePn        ( int nbHeurePn                         ) { this.nbHeurePn      = nbHeurePn      ;}
    public void setNbHeurePnCours   ( int nbHeurePnCours                    ) { this.nbHeurePnCours = nbHeurePnCours ;}
    //public void setHeureSemestre  ( HashMap<String, Integer> heureSemestre) { this.heureSemestre  = heureSemestre  ;}
    //public void setNbHeuretl      ( int nbHeuretll                        ) { this.nbHeuretll     = nbHeuretll     ;}
}