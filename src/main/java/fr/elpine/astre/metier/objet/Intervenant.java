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
    private String               ratioTP;

    private CategorieIntervenant   categorie;
    private ArrayList<Affectation> ensAffectation;

    private Intervenant(String nom, String prenom, String mail, CategorieIntervenant categorie, int heureService, int heureMax, String ratioTP)
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

    public static Intervenant creerIntervenant(String nom, String prenom, String email, CategorieIntervenant categorie, int heureService, int heureMax,String ratioTP)
    {
        String emailTmt = email.toLowerCase();

        return new Intervenant(nom, prenom, emailTmt, categorie, heureService, heureMax, ratioTP);
    }

    /*   GETTER    */

    public int getId                           () { return id       ;}
    public String    getNom                    () { return nom      ;}
    public String    getPrenom                 () { return prenom   ;}
    public String    getMail                   () { return mail;}
    public int getHeureService() { return heureService  ;}
    public int getHeureMax() { return heureMax; }
    public String getRatioTP() { return ratioTP; }
    public CategorieIntervenant getCategorie() { return categorie   ;}

    /*   SETTER   */

    public int     setId     (int id               ) { return this.id = id     ;}
    public void setNom     (String nom             ) { this.nom = nom           ;}
    public void setPrenom  (String prenom          ) { this.prenom = prenom     ;}
    public boolean setMail  (String mail           )
    {
        if ( mail.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"))
        {
            this.mail = mail;
            return true;
        }
        return false;
    }
    public void setHeureService (int heureService  ) { this.heureService = heureService   ;}
    public void setHeureMax (int heureMax          ) { this.heureMax = heureMax   ;}
    public boolean setRatioTP (String ratioTP      )
    {
        if ( ratioTP.matches("^(0*(0(\\.\\d+)?|0\\.[0-9]*[1-9]+)|0*([1-9]\\d*|0)\\/[1-9]\\d*)$"))
        {
            this.ratioTP = ratioTP;
            return true;
        }
        return false;
    }
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
