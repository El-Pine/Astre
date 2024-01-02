package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;

import java.util.HashMap;

public class CategorieIntervenant
{
    private String code;
    private String nom;
    private int nbHeureMaxDefault;
    private int nbHeureServiceDefault;
    private String ratioTPDefault;

    private boolean                 ajoute;
    private boolean                 supprime;
    private boolean                 modifie;
    private HashMap<String, Object> rollbackDatas;


    public CategorieIntervenant(String code, String nom, int nbHeureMaxDefault, int nbHeureServiceDefault, String ratioTPDefault)
    {
        this.code         = code;
        this.nom          = nom;
        this.nbHeureMaxDefault   = nbHeureMaxDefault;
        this.nbHeureServiceDefault      = nbHeureServiceDefault;
        this.ratioTPDefault      = ratioTPDefault;

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;

        this.setRollback();
    }

    /*   GETTER    */

    public String getCode        () { return code         ;}
    public String getNom         () { return nom          ;}
     public int getNbHeureMaxDefault     () { return nbHeureMaxDefault   ;}
    public int getNbHeureServiceDefault        () { return nbHeureServiceDefault      ;}
    public String getRatioTPDefault() { return ratioTPDefault; }
    public double getRatioTPDefaultValue()
    {
        String[] splt = this.ratioTPDefault.split("/");

        if (splt.length == 2) {
            return (double) Integer.parseInt(splt[0]) / Integer.parseInt(splt[1]);
        } else if (splt.length == 1) {
            return Double.parseDouble(splt[0]);
        }

        return 0d;
    }


    /*   SETTER   */

    public void setNom          ( String nom           ) { this.nom          = nom          ; this.modifie = true; }
    public void setNbHeureMaxDefault   ( int nbHeureMaxDefault       ) { this.nbHeureMaxDefault   = nbHeureMaxDefault   ; this.modifie = true; }
    public void setNbHeureServiceDefault      ( int nbHeureServiceDefault          ) { this.nbHeureServiceDefault      = nbHeureServiceDefault      ; this.modifie = true; }
    public boolean setRatioTPDefault (String ratioTPDefault      )
    {
        if ( ratioTPDefault.matches("^(0*(0(\\.\\d+)?|0\\.[0-9]*[1-9]+)|0*([1-9]\\d*|0)\\/[1-9]\\d*)$"))
        {
            this.ratioTPDefault = ratioTPDefault;
            this.modifie = true;
            return true;
        }
        return false;
    }

    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }
    public void reset() { this.ajoute = false; this.supprime = false; this.modifie = false; this.setRollback(); }

    // TODO : ajouter les toString()


    public boolean supprimer()
    {
        for (Intervenant intervenant : Controleur.get().getMetier().getIntervenants())
            if (intervenant.getCategorie() == this) return false;

        Controleur.get().getMetier().getCategorieIntervenants().remove(this);

        return true;
    }


    public boolean supprimer( boolean recursive )
    {
        for (Intervenant intervenant : Controleur.get().getMetier().getIntervenants())
            if (intervenant.getCategorie() == this)
                if (!recursive) return false;
                else intervenant.supprimer(true);

        // supprimer l'élement
        return this.supprime = true;
    }

    public void rollback()
    {
        if (this.rollbackDatas == null) return;

        this.code = (String) this.rollbackDatas.get("code");
        this.nom = (String) this.rollbackDatas.get("nom");
        this.nbHeureMaxDefault = (int) this.rollbackDatas.get("nbHeureMaxDefault");
        this.nbHeureServiceDefault = (int) this.rollbackDatas.get("nbHeureServiceDefault");
        this.ratioTPDefault = (String) this.rollbackDatas.get("ratioTPDefault");

        this.rollbackDatas.clear();
    }

    private void setRollback()
    {
        if (this.rollbackDatas == null) this.rollbackDatas = new HashMap<>(); else this.rollbackDatas.clear();

        this.rollbackDatas.put("code", this.code);
        this.rollbackDatas.put("nom", this.nom);
        this.rollbackDatas.put("nbHeureMaxDefault", this.nbHeureMaxDefault);
        this.rollbackDatas.put("nbHeureServiceDefault", this.nbHeureServiceDefault);
        this.rollbackDatas.put("ratioTPDefault", this.ratioTPDefault);
    }


    @Override
    public String toString() {
	    return this.getNom();
    }
}
