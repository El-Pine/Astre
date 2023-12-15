package fr.elpine.astre.metier.objet;

import java.util.ArrayList;

public class Intervenant
{
    private String               nom;
    private String               prenom;
    private String               email;
    private CategorieIntervenant statut;
    private int                  service;
    private int                  heureMax;
    private double               ratioTP;
    private int                  id;


    public Intervenant(int id,String nom, String prenom,CategorieIntervenant statut, int service, int heureMax,double ratioTP)
    {
        this.id         = id;
        this.nom        = nom;
        this.prenom     = prenom;
        this.statut     = statut;
        this.service    = service;
        this.heureMax   = heureMax;
        this.ratioTP    = ratioTP;
    }

    /*   GETTER    */
    public int      getId                     () { return id       ;}
    public String    getNom                    () { return nom      ;}
    public String    getPrenom                 () { return prenom   ;}
    public CategorieIntervenant getStatut      () { return statut   ;}
    public int       getService                () { return service  ;}

    /*   SETTER   */

    public int     setId     (int id                  ) { return this.id = id     ;}
    public void setNom     (String nom                 ) { this.nom = nom           ;}
    public void setPrenom  (String prenom              ) { this.prenom = prenom     ;}
    public void setStatut  (CategorieIntervenant statut) { this.statut = statut     ;}
    public void setService (int service                ) { this.service = service   ;}

    public String getEmail() {
        return email;
    }

    public int getHeureMax() {
        return heureMax;
    }

    public double getRatioTP() {
        return ratioTP;
    }
}
