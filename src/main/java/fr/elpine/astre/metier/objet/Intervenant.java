package fr.elpine.astre.metier.objet;

import java.util.ArrayList;

public class Intervenant
{
    private String               nom;
    private String               prenom;
    private String               email;
    private CategorieIntervenant statut;
    private ArrayList<Semestre>  alSemestre;
    private int                  service;
    private double               total;


    public Intervenant(String nom, String prenom, String email,CategorieIntervenant statut, int service, double total)
    {
        this.nom        = nom;
        this.prenom     = prenom;
        this.email      = email;
        this.statut     = statut;
        this.service    = service;
        this.total      = total;
        this.alSemestre = new ArrayList<Semestre>();
    }

    /*   GETTER    */


    public String    getNom                    () { return nom      ;}
    public String    getPrenom                 () { return prenom   ;}
    public String    getEmail                  () { return email    ;}
    public CategorieIntervenant getStatut      () { return statut   ;}
    public int       getService                () { return service  ;}
    public double    getTotal                  () { return total    ;}
    public ArrayList<Semestre> getAlSemestre   () { return this.alSemestre;}

    /*   SETTER   */

    public void setNom     (String nom                 ) { this.nom = nom           ;}
    public void setPrenom  (String prenom              ) { this.prenom = prenom     ;}
    public void setEmail   (String email               ) { this.email = email;       }
    public void setStatut  (CategorieIntervenant statut) { this.statut = statut     ;}
    public void setService (int service                ) { this.service = service   ;}
    public void setTotal   (double total               ) { this.total = total       ;}
}
