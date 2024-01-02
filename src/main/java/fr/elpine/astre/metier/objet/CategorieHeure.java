package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;

import java.util.ArrayList;
import java.util.HashMap;

public class CategorieHeure
{
    private String nom;
    private String equivalentTD;
    private boolean ressource;
    private boolean sae;
    private boolean ppp;
    private boolean stage;

    private ArrayList<Attribution> ensAttribution;

    private boolean ajoute;
    private boolean supprime;
    private boolean                 modifie;
    private HashMap<String, Object> rollbackDatas;

    public CategorieHeure(String nom, String equivalentTD,boolean ressource, boolean sae, boolean ppp, boolean stage)
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

        this.setRollback();
    }

    /* GETTER */

    public String getNom          () { return this.nom          ;}
    public String getEquivalentTD () { return this.equivalentTD ;}
    public boolean estRessource() { return ressource;}
    public boolean estSae      () { return sae;      }
    public boolean estPpp      () { return ppp;      }
    public boolean estStage    () { return stage;    }
    public double getEquivalentTDValue ()
    {
        String[] splt = this.equivalentTD.split("/");

        if (splt.length == 2) {
            return (double) Integer.parseInt(splt[0]) / Integer.parseInt(splt[1]);
        } else if (splt.length == 1) {
            return Double.parseDouble(splt[0]);
        }

        return 0d;
    }

    /*  SETTER   */

    public void setEquivalentTD ( String equivalentTD  ) { this.equivalentTD = equivalentTD ; this.modifie = true; }
    public void setRessource    ( boolean estRessource ) { this.ressource    = estRessource ; this.modifie = true; }
    public void setSae          ( boolean estSae       ) { this.sae          = estSae       ; this.modifie = true; }
    public void setPpp          ( boolean estPpp       ) { this.ppp          = estPpp       ; this.modifie = true; }
    public void setStage        ( boolean estStage     ) { this.stage        = estStage     ; this.modifie = true; }

    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }
    public void reset() { this.ajoute = false; this.supprime = false; this.modifie = false; this.setRollback(); }


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

    public void rollback()
    {
        if (this.rollbackDatas == null) return;

        this.nom = (String) this.rollbackDatas.get("nom");
        this.equivalentTD = (String) this.rollbackDatas.get("equivalentTD");
        this.ppp = (boolean) this.rollbackDatas.get("ppp");
        this.ressource = (boolean) this.rollbackDatas.get("ressource");
        this.stage = (boolean) this.rollbackDatas.get("stage");
        this.sae = (boolean) this.rollbackDatas.get("sae");

        this.rollbackDatas.clear();
    }

    private void setRollback()
    {
        if (this.rollbackDatas == null) this.rollbackDatas = new HashMap<>(); else this.rollbackDatas.clear();

        this.rollbackDatas.put("nom", this.nom);
        this.rollbackDatas.put("equivalentTD", this.equivalentTD);
        this.rollbackDatas.put("ppp", this.ppp);
        this.rollbackDatas.put("ressource", this.ressource);
        this.rollbackDatas.put("stage", this.stage);
        this.rollbackDatas.put("sae", this.sae);
    }


    @Override
    public String toString() {
	    return this.getNom();
    }
}
