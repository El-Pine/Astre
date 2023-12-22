package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Module
{
    private String code;
    private String nom;
    private String abreviation;
    private String typeModule;
    private Color couleur;
    private boolean validation;

    private Semestre semestre;
    private ArrayList<Attribution> ensAttribution;
    private ArrayList<Affectation> ensAffectation;

    private boolean ajoute;
    private boolean supprime;
    private boolean modifie;

    public Module(String nom, String code, String abreviation, String typeModule, Color couleur, boolean validation, Semestre semestre)
    {
        this.nom            = nom;
        this.code           = code;
        this.abreviation    = abreviation;
        this.typeModule     = typeModule;
        this.couleur        = couleur;
        this.validation     = validation;
        this.semestre       = semestre;

        this.ensAttribution = new ArrayList<>();
        this.ensAffectation = new ArrayList<>();

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;
    }

    /*   GETTER    */

    public String  getCode        () { return code        ;}
    public String  getNom         () { return nom         ;}
    public String  getAbreviation () { return abreviation ;}
    public Color   getCouleur     () { return couleur     ;}
    public String  getTypeModule  () { return typeModule  ;}
    public boolean estValide      () { return validation  ;}
    public Semestre getSemestre   () { return semestre    ;}

    /*   SETTER   */

    //TODO : verif le changement du code

    public void setCode       ( String code        )   { this.code        = code        ; this.modifie = true; }
    public void setNom         ( String nom        )   { this.nom         = nom         ; this.modifie = true; }
    public void setAbreviation ( String  abreviation ) { this.abreviation = abreviation ; this.modifie = true; }
    public void setCouleur     ( Color  couleur      ) { this.couleur     = couleur     ; this.modifie = true; }
    public void setTypeModule  ( String  typeModule )  { this.typeModule  = typeModule  ; this.modifie = true; }
    public void setValidation  ( boolean validation )  { this.validation  = validation  ; this.modifie = true; }

    public void setSemestre( Semestre semestre  )
    {
        if ( semestre != null )
            semestre.ajouterModule(this);
        else if ( this.semestre != null )
            this.semestre.supprimerModule(this);

        this.semestre = semestre;
    }

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
        // Verification avant suppression
        if (!this.ensAttribution.isEmpty()) if (!recursive) return false;

        // Suppression des affectations
        for (Affectation affectation : this.ensAffectation)
            if (!recursive) return false;
            else affectation.supprimer();

        // Suppression des attributions
        for (Attribution attribution : this.ensAttribution)
	        attribution.supprimer();

        // supprimer l'élement
        return this.supprime = true;
    }


    @Override
    public String toString() {
        return "Module{" +
                "code='" + code + '\'' +
                ", nom='" + nom + '\'' +
                ", abreviation='" + abreviation + '\'' +
                ", typeModule='" + typeModule + '\'' +
                ", couleur=" + couleur +
                ", validation=" + validation +
                ", semestre=" + semestre.getNumero() +
                ", ensAttribution=" + ensAttribution +
                ", ensAffectation=" + ensAffectation +
                '}';
    }
}
