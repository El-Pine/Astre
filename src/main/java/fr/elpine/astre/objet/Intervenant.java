package fr.elpine.astre.objet;

import java.util.ArrayList;

public class Intervenant
{
    private String               nom;
    private String               prenom;
    private CategorieIntervenant statut;
    private ArrayList<Semestre>  alSemestre;
    private int                  service;
    private int                  heureMax;
    private int                  heureMin;
    private double               sTotalP;
    private double               sTotalI;
    private double               total;


    public Intervenant(String nom, String prenom, CategorieIntervenant statut, int service, int heureMax, int heureMin, double sTotalP, double sTotalI, double total)
    {
        this.nom        = nom;
        this.prenom     = prenom;
        this.statut     = statut;
        this.service    = service;
        this.heureMax   = heureMax;
        this.heureMin   = heureMin;
        this.sTotalP    = sTotalP;
        this.sTotalI    = sTotalI;
        this.total      = total;
        this.alSemestre = new ArrayList<Semestre>();
    }

    /*   GETTER    */


    public String    getNom                    () { return nom      ;}
    public String    getPrenom                 () { return prenom   ;}
    public CategorieIntervenant getStatut      () { return statut   ;}
    public int       getService                () { return service  ;}
    public int       getHeureMax               () { return heureMax ;}
    public int       getHeureMin               () { return heureMin ;}
    public double    getsTotalP                () { return sTotalP  ;}
    public double    getsTotalI                () { return sTotalI  ;}
    public double    getTotal                  () { return total    ;}
    public ArrayList<Semestre> getAlSemestre   () { return this.alSemestre;}

    /*   SETTER   */

    public void setNom     (String nom                 ) { this.nom = nom           ;}
    public void setPrenom  (String prenom              ) { this.prenom = prenom     ;}
    public void setStatut  (CategorieIntervenant statut) { this.statut = statut     ;}
    public void setService (int service                ) { this.service = service   ;}
    public void setHeureMax(int heureMax               ) { this.heureMax = heureMax ;}
    public void setHeureMin(int heureMin               ) { this.heureMin = heureMin ;}
    public void setsTotalP (double sTotalP             ) { this.sTotalP = sTotalP   ;}
    public void setsTotalI (double sTotalI             ) { this.sTotalI = sTotalI   ;}
    public void setTotal   (double total               ) { this.total = total       ;}
}
