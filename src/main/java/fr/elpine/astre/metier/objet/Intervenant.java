package fr.elpine.astre.metier.objet;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    public Intervenant(String nom, String prenom,CategorieIntervenant statut, int service, int heureMax,double ratioTP)
    {
        this.nom        = nom;
        this.prenom     = prenom;
        this.statut     = statut;
        this.service    = service;
        this.heureMax   = heureMax;
        this.ratioTP    = ratioTP;
    }

    public Intervenant(int id,String nom, String prenom,CategorieIntervenant statut, int service, int heureMax,double ratioTP)
    {
        this.nom        = nom;
        this.prenom     = prenom;
        this.statut     = statut;
        this.service    = service;
        this.heureMax   = heureMax;
        this.ratioTP    = ratioTP;
        this.id         = id;
    }

    public static boolean validateEmail(String email) {
        // Expression régulière pour vérifier l'adresse e-mail
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Création du pattern avec l'expression régulière
        Pattern pattern = Pattern.compile(emailRegex);

        // Correspondance du pattern avec l'adresse e-mail fournie
        Matcher matcher = pattern.matcher(email);

        // Vérification de la correspondance
        return matcher.matches();
    }

    /*   GETTER    */

    public int getId                           () { return id       ;}
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
