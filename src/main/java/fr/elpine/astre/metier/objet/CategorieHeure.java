package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.outil.Fraction;

import java.util.ArrayList;
import java.util.HashMap;

public class CategorieHeure
{
    private String   nom;
    private Fraction equivalentTD;
    private boolean  ressource;
    private boolean  sae;
    private boolean  ppp;
    private boolean  stage;
    private boolean  hebdo;
    private String typeGroupe;

    private final ArrayList<Attribution> ensAttribution;

    private boolean ajoute;
    private boolean supprime;
    private boolean                 modifie;
    private HashMap<String, Object> rollbackDatas;

    public CategorieHeure(String nom, Fraction equivalentTD,boolean ressource, boolean sae, boolean ppp, boolean stage, boolean hebdo,String typeGroupe)
    {
        this.nom           = nom;
        this.equivalentTD  = equivalentTD;
        this.ressource     = ressource;
        this.sae           = sae;
        this.ppp           = ppp;
        this.stage         = stage;
        this.hebdo         = hebdo;
        this.typeGroupe    = typeGroupe;

        this.ensAttribution = new ArrayList<>();

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;

        if (this.ajoute) {
            Astre metier = Controleur.get().getMetier();

            if (!metier.getCategorieHeures().contains( this )) metier.getCategorieHeures().add( this );
        }

        this.setRollback();
    }

    /* GETTER */

    public String getNom          () { return this.nom          ;}
    public Fraction getEquivalentTD () { return this.equivalentTD ;}
    public boolean estRessource() { return ressource;}
    public boolean estSae      () { return sae;   }
    public boolean estPpp      () { return ppp;   }
    public boolean estStage    () { return stage; }
    public boolean estHebdo    () { return hebdo; }
    public String getTypeGroupe() { return this.typeGroupe;}



    /*  SETTER   */

    public void setEquivalentTD ( Fraction equivalentTD  ) { this.equivalentTD = equivalentTD ; this.modifie = !((Fraction) this.rollbackDatas.get("equivalentTD")).equals(equivalentTD); }
    public void setRessource    ( boolean estRessource   ) { this.ressource    = estRessource ; this.modifie = ((boolean) this.rollbackDatas.get("estRessource")) != estRessource; }
    public void setSae          ( boolean estSae         ) { this.sae          = estSae       ; this.modifie = ((boolean) this.rollbackDatas.get("estSae")) != estSae; }
    public void setPpp          ( boolean estPpp         ) { this.ppp          = estPpp       ; this.modifie = ((boolean) this.rollbackDatas.get("estPpp")) != estPpp; }
    public void setStage        ( boolean estStage       ) { this.stage        = estStage     ; this.modifie = ((boolean) this.rollbackDatas.get("estStage")) != estStage; }
    public void setTypeGroupe   ( String  typeGroupe     ) { this.typeGroupe   = typeGroupe   ; this.modifie = !this.rollbackDatas.get("typeGroupe").equals(typeGroupe); }
    public void setHebdo        ( boolean hebdo          ) { this.hebdo        = hebdo        ; this.modifie = ((boolean) this.rollbackDatas.get("hebdo")) != hebdo; }

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
        for (Affectation affectation : this.getAffectations())
            if (affectation.getTypeHeure() == this)
                if (!recursive) return false;
                else affectation.supprimer();

        // Suppression des attributions
        for (Attribution attribution : this.ensAttribution)
            attribution.supprimer();

        // supprimer l'élement
        return this.supprime = true;
    }

    private ArrayList<Affectation> getAffectations()
    {
        ArrayList<Affectation> lst = new ArrayList<>();

        for (Annee a : Controleur.get().getMetier().getAnnees())
            for (Semestre s : a.getSemestres())
                for (Module m : s.getModules())
                    for (Affectation aff : m.getAffectations())
                        if (aff.getTypeHeure() == this) lst.add(aff);

        return lst;
    }


    public void rollback()
    {
        if (this.rollbackDatas == null) return;

        this.nom = (String) this.rollbackDatas.get("nom");
        this.equivalentTD = (Fraction) this.rollbackDatas.get("equivalentTD");
        this.ppp = (boolean) this.rollbackDatas.get("ppp");
        this.ressource = (boolean) this.rollbackDatas.get("ressource");
        this.stage = (boolean) this.rollbackDatas.get("stage");
        this.sae = (boolean) this.rollbackDatas.get("sae");
        this.hebdo = (boolean) this.rollbackDatas.get("hebdo");

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
        this.rollbackDatas.put("hebdo", this.hebdo);
    }


    @Override
    public String toString() {
	    return this.getNom();
    }
}
