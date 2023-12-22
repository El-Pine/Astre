package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;

import java.util.ArrayList;

public class CategorieHeure
{
    private String nom;
    private double equivalentTD;
    private boolean ressource;
    private boolean sae;
    private boolean ppp;
    private boolean stage;

    private ArrayList<Attribution> ensAttribution;

    private boolean ajoute;
    private boolean supprime;
    private boolean modifie;

    public CategorieHeure(String nom, double equivalentTD,boolean ressource, boolean sae, boolean ppp, boolean stage)
    {
        this.nom           = nom;
        this.equivalentTD  = equivalentTD;
        this.ressource     = ressource;
        this.sae           = sae;
        this.ppp           = ppp;
        this.stage         = stage;

        this.ensAttribution = new ArrayList<>();

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;
    }

    /* GETTER */

    public String getNom          () { return this.nom          ;}
    public double getEquivalentTD () { return this.equivalentTD ;}
    public boolean estRessource() { return ressource;}
    public boolean estSae      () { return sae;      }
    public boolean estPpp      () { return ppp;      }
    public boolean estStage    () { return stage;    }

    /*  SETTER   */

    public void setEquivalentTD ( double equivalentTD  ) { this.equivalentTD = equivalentTD ; this.modifie = true; }
    public void setRessource    ( boolean estRessource ) { this.ressource    = estRessource ; this.modifie = true; }
    public void setSae          ( boolean estSae       ) { this.sae          = estSae       ; this.modifie = true; }
    public void setPpp          ( boolean estPpp       ) { this.ppp          = estPpp       ; this.modifie = true; }
    public void setStage        ( boolean estStage     ) { this.stage        = estStage     ; this.modifie = true; }

    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }
    public void reset() { this.ajoute = false; this.supprime = false; this.modifie = false; }


    public void ajouterAttribution( Attribution attribution )
    {
        if (attribution != null && !this.ensAttribution.contains(attribution))
            this.ensAttribution.add(attribution);
    }

    public void supprimerAttribution( Attribution attribution )
    {
        if (attribution == null || !this.ensAttribution.contains(attribution)) return;

        this.ensAttribution.remove(attribution);
    }

    public ArrayList<Attribution> getAttributions() { return this.ensAttribution; }


    public boolean supprimer( boolean recursive )
    {
        // Verification avant suppression
        if (!this.ensAttribution.isEmpty()) if (!recursive) return false;

        // Suppression des affectations
        for (Affectation affectation : Controleur.get().getMetier().getAffectations())
            if (affectation.getTypeHeure() == this)
                affectation.supprimer();

        // Suppression des attributions
        for (Attribution attribution : this.ensAttribution)
            if (!recursive) return false;
            else attribution.supprimer();

        // supprimer l'élement
        return this.supprime = true;
    }


    @Override
    public String toString() {
	    return this.getNom();
    }
}
