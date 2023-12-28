package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;

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

    private boolean ajoute;
    private boolean supprime;
    private boolean modifie;

    public Intervenant(String nom, String prenom, String mail, CategorieIntervenant categorie, int heureService, int heureMax, String ratioTP)
    {
        this.id           = null;
        this.nom          = nom;
        this.prenom       = prenom;
        this.mail         = mail.toLowerCase();
        this.categorie    = categorie;
        this.heureService = heureService;
        this.heureMax     = heureMax;
        this.ratioTP      = ratioTP;

        this.ensAffectation = new ArrayList<>();

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;
    }

    /*   GETTER    */

    public int getId                           () { return id       ;}
    public String    getNom                    () { return nom      ;}
    public String    getPrenom                 () { return prenom   ;}
    public String    getMail                   () { return mail;}
    public int getHeureService() { return heureService  ;}
    public int getHeureMax() { return heureMax; }
    public String getRatioTP() { return ratioTP; }
    public double getRatioTPValue()
    {
        String[] splt = this.ratioTP.split("/");

        if (splt.length == 2) {
            return (double) Integer.parseInt(splt[0]) / Integer.parseInt(splt[1]);
        } else if (splt.length == 1) {
            return Double.parseDouble(splt[0]);
        }

        return 0d;
    }
    public CategorieIntervenant getCategorie() { return categorie   ;}

    /*   SETTER   */

    public void setId     (int id               ) { this.id = id; }
    public void setNom     (String nom             ) { this.nom = nom           ; this.modifie = true; }
    public void setPrenom  (String prenom          ) { this.prenom = prenom     ; this.modifie = true; }
    public boolean setMail  (String mail           )
    {
        if ( mail.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"))
        {
            this.mail = mail;
            this.modifie = true;
            return true;
        }
        return false;
    }
    public void setHeureService (int heureService  ) { this.heureService = heureService   ; this.modifie = true; }
    public void setHeureMax (int heureMax          ) { this.heureMax = heureMax   ; this.modifie = true; }
    public boolean setRatioTP (String ratioTP      )
    {
        if ( ratioTP.matches("^(0*(0(\\.\\d+)?|0\\.[0-9]*[1-9]+)|0*([1-9]\\d*|0)\\/[1-9]\\d*)$"))
        {
            this.ratioTP = ratioTP;
            this.modifie = true;
            return true;
        }
        return false;
    }
    public void setCategorie  (CategorieIntervenant categorie) { this.categorie = categorie     ; this.modifie = true; }

    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }
    public void reset() { this.ajoute = false; this.supprime = false; this.modifie = false; }


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


    public boolean supprimer( boolean recursive )
    {
        for (Affectation affectation : this.getAffectations())
            if (!recursive) return false;
            else affectation.supprimer();

        // supprimer l'élement
        return this.supprime = true;
    }


    @Override
    public String toString() {
        return String.format("%s %s", this.nom, this.prenom);
    }
}
