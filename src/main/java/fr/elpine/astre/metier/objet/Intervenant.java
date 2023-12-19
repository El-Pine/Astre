package fr.elpine.astre.metier.objet;

import java.util.ArrayList;

public class Intervenant
{
    private Integer              id;
    private String               nom;
    private String prenom;
    private String mail;
    private int    heureService;
    private int                  heureMax;
    private double               ratioTP;

    private CategorieIntervenant   categorie;
    private ArrayList<Affectation> ensAffectation;

    private Intervenant(String nom, String prenom, String mail, CategorieIntervenant categorie, int heureService, int heureMax, double ratioTP)
    {
        this.id           = null;
        this.nom          = nom;
        this.prenom       = prenom;
        this.mail         = mail;
        this.categorie    = categorie;
        this.heureService = heureService;
        this.heureMax     = heureMax;
        this.ratioTP      = ratioTP;

        this.ensAffectation = new ArrayList<>();
    }

    public static Intervenant creerIntervenant(String nom, String prenom, String email, CategorieIntervenant categorie, int heureService, int heureMax,double ratioTP)
    {
        String emailTmt = email.toLowerCase();

        return validateEmail(emailTmt) ? new Intervenant(nom, prenom, emailTmt, categorie, heureService, heureMax, ratioTP) : null;
    }

    public static boolean validateEmail(String email)
    { // TODO : déplacer les regex direct dans l'IHM pour pas avoir a le faire là, toutes les vérif doivent être faite dans l'IHM
        // Expression régulière pour vérifier l'adresse e-mail
        /*String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Création du pattern avec l'expression régulière
        Pattern pattern = Pattern.compile(emailRegex);

        // Correspondance du pattern avec l'adresse e-mail fournie
        Matcher matcher = pattern.matcher(email);

        // Vérification de la correspondance
        return matcher.matches();*/

        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    /*   GETTER    */

    public int getId                           () { return id       ;}
    public String    getNom                    () { return nom      ;}
    public String    getPrenom                 () { return prenom   ;}
    public String    getMail                   () { return mail;}
    public int getHeureService() { return heureService  ;}
    public int getHeureMax() { return heureMax; }
    public double getRatioTP() { return ratioTP; }
    public CategorieIntervenant getCategorie() { return categorie   ;}

    /*   SETTER   */

    public int     setId     (int id                  ) { return this.id = id     ;}
    public void setNom     (String nom                 ) { this.nom = nom           ;}
    public void setPrenom  (String prenom              ) { this.prenom = prenom     ;}
    public void setMail  (String mail              ) { this.mail = mail     ;}
    public void setHeureService (int heureService                ) { this.heureService = heureService   ;}
    public void setHeureMax (int heureMax                ) { this.heureMax = heureMax   ;}
    public void setRatioTP (int ratioTP                ) { this.ratioTP = ratioTP   ;}
    public void setCategorie  (CategorieIntervenant categorie) { this.categorie = categorie     ;}


    public void ajouterAffectation( Affectation affectation )
    {
        if (affectation != null && !this.ensAffectation.contains(affectation))
            this.ensAffectation.add(affectation);
    }

    public void supprimerAffectation( Affectation affectation )
    {
        if (affectation == null || !this.ensAffectation.contains(affectation)) return;

        this.ensAffectation.remove(affectation);
    }

    public ArrayList<Affectation> getAffectations() { return this.ensAffectation; }
}
