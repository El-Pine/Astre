package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.outil.Fraction;

import java.util.ArrayList;
import java.util.HashMap;

public class Intervenant
{
    private Integer              id;
    private String               nom;
    private String prenom;
    private String mail;
    private Fraction heureService;
    private Fraction heureMax;
    private Fraction ratioTP;

    private CategorieIntervenant   categorie;
    private ArrayList<Affectation> ensAffectation;

    private boolean ajoute;
    private boolean supprime;
    private boolean                 modifie;
    private HashMap<String, Object> rollbackDatas;

    public Intervenant(String nom, String prenom, String mail, CategorieIntervenant categorie, Fraction heureService, Fraction heureMax, Fraction ratioTP)
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

        this.setRollback();
    }

    /*   GETTER    */

    public int getId                           () { return id       ;}
    public String    getNom                    () { return nom      ;}
    public String    getPrenom                 () { return prenom   ;}
    public String    getMail                   () { return mail;}
    public Fraction getHeureService() { return heureService  ;}
    public Fraction getHeureMax() { return heureMax; }
    public Fraction getRatioTP() { return ratioTP; }
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
    public void setHeureService (Fraction heureService  ) { this.heureService = heureService   ; this.modifie = true; }
    public void setHeureMax (Fraction heureMax          ) { this.heureMax = heureMax   ; this.modifie = true; }
    public void setRatioTP (Fraction ratioTP          ) { this.ratioTP = ratioTP   ; this.modifie = true; }
    public void setCategorie  (CategorieIntervenant categorie) { this.categorie = categorie     ; this.modifie = true; }

    /* Calculs */

    public ArrayList<Double> getHeure() // récupère les heures des 6 semestres de l'année actuelle de l'intervenant
    {
        Annee a = Controleur.get().getMetier().getAnneeActuelle();
        ArrayList<Double> lst = new ArrayList<>();

        for (int i = 0; i < 6; i++) lst.add(i, 0d);

        if (a != null)
            for (Semestre s : a.getSemestres())
            {
                double d = 0d;

                for (Module m : s.getModules())
                    for (Affectation aff : m.getAffectations())
                        if (aff.getIntervenant() == this) d += aff.getTotalEqtd() * (aff.getTypeHeure().getNom().equals("TP") ? this.ratioTP.value() : 1);

                lst.add(s.getNumero(), d);
            }

        return lst;
    }


    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }
    public void reset() { this.ajoute = false; this.supprime = false; this.modifie = false; this.setRollback(); }


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

    public void rollback()
    {
        if (this.rollbackDatas == null) return;

        this.nom = (String) this.rollbackDatas.get("nom");
        this.prenom = (String) this.rollbackDatas.get("prenom");
        this.mail = (String) this.rollbackDatas.get("mail");
        this.heureMax = (Fraction) this.rollbackDatas.get("heureMax");
        this.heureService = (Fraction) this.rollbackDatas.get("heureService");
        this.ratioTP = (Fraction) this.rollbackDatas.get("ratioTP");
        this.categorie = (CategorieIntervenant) this.rollbackDatas.get("categorie");

        this.rollbackDatas.clear();
    }

    private void setRollback()
    {
        if (this.rollbackDatas == null) this.rollbackDatas = new HashMap<>(); else this.rollbackDatas.clear();

        this.rollbackDatas.put("nom", this.nom);
        this.rollbackDatas.put("prenom", this.prenom);
        this.rollbackDatas.put("mail", this.mail);
        this.rollbackDatas.put("heureMax", this.heureMax);
        this.rollbackDatas.put("heureService", this.heureService);
        this.rollbackDatas.put("ratioTP", this.ratioTP);
        this.rollbackDatas.put("categorie", this.categorie);
    }


    @Override
    public String toString() {
        return String.format("%s %s", this.nom, this.prenom);
    }
}
