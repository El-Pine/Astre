package fr.elpine.astre.metier.objet;

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

    public CategorieHeure(String nom, double equivalentTD,boolean ressource, boolean sae, boolean ppp, boolean stage)
    {
        this.nom           = nom;
        this.equivalentTD  = equivalentTD;
        this.ressource     = ressource;
        this.sae           = sae;
        this.ppp           = ppp;
        this.stage         = stage;

        this.ensAttribution = new ArrayList<>();
    }

    /* GETTER */

    public String getNom          () { return this.nom          ;}
    public double getEquivalentTD () { return this.equivalentTD ;}
    public boolean estRessource() { return ressource;}
    public boolean estSae      () { return sae;      }
    public boolean estPpp      () { return ppp;      }
    public boolean estStage    () { return stage;    }

    /*  SETTER   */

    public void setNom          ( String nom           ) { this.nom          = nom          ;}
    public void setEquivalentTD ( double equivalentTD  ) { this.equivalentTD = equivalentTD ;}
    public void setRessource    ( boolean estRessource ) { this.ressource    = estRessource ;}
    public void setSae          ( boolean estSae       ) { this.sae          = estSae       ;}
    public void setPpp          ( boolean estPpp       ) { this.ppp          = estPpp       ;}
    public void setStage        ( boolean estStage     ) { this.stage        = estStage     ;}


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
}
