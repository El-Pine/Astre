package fr.elpine.astre.metier.objet;

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

    public void setCode        ( String code       )   { this.code        = code        ;}
    public void setNom         ( String nom        )   { this.nom         = nom         ;}
    public void setAbreviation ( String  abreviation ) { this.abreviation = abreviation ;}
    public void setCouleur     ( Color  couleur      ) { this.couleur     = couleur     ;}
    public void setTypeModule  ( String  typeModule )  { this.typeModule  = typeModule  ;}
    public void setValidation  ( boolean validation )  { this.validation  = validation  ;}

    public void setSemestre( Semestre semestre  )
    {
        if ( semestre != null )
            semestre.ajouterModule(this);
        else if ( this.semestre != null )
            this.semestre.supprimerModule(this);

        this.semestre = semestre;
    }


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
}
