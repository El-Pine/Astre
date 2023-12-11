package fr.elpine.astre.objet;

import java.util.HashMap;

public class Ressource extends Module
{
    private int nbHeurePn;
    private int nbHeurePnCours;
    private HashMap<String,Integer> heureSemestre;
    private int nbHeuretll;

    public Ressource(String nom, String code, String commentaire, int nbHeurePn, int nbHeurePnCours, HashMap<String, Integer> heureSemestre, int nbHeuretll) {
        super(nom, code, commentaire);
        this.nbHeurePn = nbHeurePn;
        this.nbHeurePnCours = nbHeurePnCours;
        this.heureSemestre = heureSemestre;
        this.nbHeuretll = nbHeuretll;
    }

    public int getNbHeurePn() {return nbHeurePn;}

    public int getNbHeurePnCours() {
        return nbHeurePnCours;}

    public HashMap<String, Integer> getHeureSemestre() { return heureSemestre; }

    public int getNbHeuretll() { return nbHeuretll; }

    public void setNbHeurePn(int nbHeurePn) { this.nbHeurePn = nbHeurePn; }

    public void setNbHeurePnCours(int nbHeurePnCours) { this.nbHeurePnCours = nbHeurePnCours; }

    public void setHeureSemestre(HashMap<String, Integer> heureSemestre) { this.heureSemestre = heureSemestre; }

    public void setNbHeuretll(int nbHeuretll) { this.nbHeuretll = nbHeuretll; }
}
