package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;

import java.util.ArrayList;

public class CategorieIntervenant
{
    private String code;
    private String nom;
    private int nbHeureMaxDefault;
    private int nbHeureServiceDefault;
    private double ratioTPDefault;

    private boolean ajoute;
    private boolean supprime;
    private boolean modifie;


    public CategorieIntervenant(String code, String nom, int nbHeureMaxDefault, int nbHeureServiceDefault, double ratioTPDefault)
    {
        this.code         = code;
        this.nom          = nom;
        this.nbHeureMaxDefault   = nbHeureMaxDefault;
        this.nbHeureServiceDefault      = nbHeureServiceDefault;
        this.ratioTPDefault      = ratioTPDefault;

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;
    }

    /*   GETTER    */

    public String getCode        () { return code         ;}
    public String getNom         () { return nom          ;}
     public int getNbHeureMaxDefault     () { return nbHeureMaxDefault   ;}
    public int getNbHeureServiceDefault        () { return nbHeureServiceDefault      ;}
    public double getRatioTPDefault     () { return ratioTPDefault      ;}


    /*   SETTER   */

    public void setNom          ( String nom           ) { this.nom          = nom          ; this.modifie = true; }
    public void setNbHeureMaxDefault   ( int nbHeureMaxDefault       ) { this.nbHeureMaxDefault   = nbHeureMaxDefault   ; this.modifie = true; }
    public void setNbHeureServiceDefault      ( int nbHeureServiceDefault          ) { this.nbHeureServiceDefault      = nbHeureServiceDefault      ; this.modifie = true; }
    public void setRatioTPDefault      ( double ratioTPDefault       ) { this.ratioTPDefault      = ratioTPDefault      ; this.modifie = true; }

    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }

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
        {
            if (intervenant.getCategorie() == this)
            {
                if (!recursive) return false;

                intervenant.supprimer(true);
            }
        }

        // supprimer l'élement
        return this.supprime = true;
    }


    @Override
    public String toString() {
	    return this.getNom();
    }
}
