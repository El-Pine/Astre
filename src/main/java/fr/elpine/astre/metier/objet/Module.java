package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class Module
{
    private String code;
    private String nom;
    private String abreviation;
    private String typeModule;
    private Color couleur;
    private boolean validation;

    private final Semestre semestre;
    private final ArrayList<Attribution> ensAttribution;
    private final ArrayList<Affectation> ensAffectation;

    private boolean ajoute;
    private boolean supprime;
    private boolean                 modifie;
    private HashMap<String, Object> rollbackDatas;

    public Module(String nom, String code, String abreviation, String typeModule, Color couleur, boolean validation, Semestre semestre)
    {
        this.nom            = nom;
        this.code           = code;
        this.abreviation    = abreviation;
        this.typeModule     = typeModule;
        this.couleur        = couleur;
        this.validation     = validation;
        this.semestre       = semestre.ajouterModule(this);

        this.ensAttribution = new ArrayList<>();
        this.ensAffectation = new ArrayList<>();

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;

        this.setRollback();
    }

    /*   GETTER    */

    public String  getCode        () { return code                               ;}
    public String  getLastCode    () { return (String) rollbackDatas.get("code") ;}
    public String  getNom         () { return nom                                ;}
    public String  getAbreviation () { return abreviation                        ;}
    public Color   getCouleur     () { return couleur                            ;}
    public String  getCouleurF    () { return String.format("rgb(%d,%d,%d)", (int) (couleur.getRed()*255), (int) (couleur.getGreen()*255), (int) (couleur.getBlue()*255)); }
    public String  getTypeModule  () { return typeModule                         ;}
    public boolean estValide      () { return validation                         ;}
    public Semestre getSemestre   () { return semestre                           ;}

    /*   SETTER   */

    public void setCode       ( String code        )   { this.code        = code        ; this.modifie = true; }
    public void setNom         ( String nom        )   { this.nom         = nom         ; this.modifie = true; }
    public void setAbreviation ( String  abreviation ) { this.abreviation = abreviation ; this.modifie = true; }
    public void setCouleur     ( Color  couleur      ) { this.couleur     = couleur     ; this.modifie = true; }
    public void setTypeModule  ( String  typeModule )  { this.typeModule  = typeModule  ; this.modifie = true; }
    public void setValidation  ( boolean validation )  { this.validation  = validation  ; this.modifie = true; }


    /* Calculs */

    public double getSommePN() {
        double s = 0.0;
        for (Attribution att : this.ensAttribution) s += att.getNbHeurePN().value();
        return s;
    }
    public double getSommePNPromo() {
        double s = 0.0;
        for (Attribution att : this.ensAttribution) s += att.getNbHeurePNPromo();
        return s;
    }

    public double getSomme() {
        double s = 0.0;
        for (Attribution att : this.ensAttribution) s += att.getNbHeureEtd();
        return s;
    }

    public double getSommePromo() {
        double s = 0.0;
        for (Attribution att : this.ensAttribution) s += att.getNbHeurePromo();
        return s;
    }

    public double getSommeAffecte() {
        double s = 0.0;
        for (Attribution att : this.ensAttribution) s += att.getNbHeureAffecte();
        return s;
    }


    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }
    public boolean isCodeModifie() { return !this.code.equals(this.rollbackDatas.get("code")); }
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

    public Attribution getAttribution(CategorieHeure typeHeure)
    {
        for (Attribution att : this.ensAttribution) if (att.getCatHr() == typeHeure) return att;
        return null;
    }


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


    public boolean supprimer( boolean recursive, boolean skipAttribution )
    {
        // Verification avant suppression
        if ((!this.ensAttribution.isEmpty() && !skipAttribution) || !this.ensAffectation.isEmpty()) if (!recursive) return false;

        // Suppression des affectations
        for (Affectation affectation : this.ensAffectation) affectation.supprimer();

        // Suppression des attributions
        for (Attribution attribution : this.ensAttribution) attribution.supprimer();

        // supprimer l'élement
        return this.supprime = true;
    }

    public void rollback()
    {
        if (this.rollbackDatas == null) return;

        this.code = (String) this.rollbackDatas.get("code");
        this.nom = (String) this.rollbackDatas.get("nom");
        this.abreviation = (String) this.rollbackDatas.get("abreviation");
        this.couleur = (Color) this.rollbackDatas.get("couleur");
        this.typeModule = (String) this.rollbackDatas.get("typeModule");
        this.validation = (boolean) this.rollbackDatas.get("validation");

        this.rollbackDatas.clear();
    }

    private void setRollback()
    {
        if (this.rollbackDatas == null) this.rollbackDatas = new HashMap<>(); else this.rollbackDatas.clear();

        this.rollbackDatas.put("code", this.code);
        this.rollbackDatas.put("nom", this.nom);
        this.rollbackDatas.put("abreviation", this.abreviation);
        this.rollbackDatas.put("couleur", this.couleur);
        this.rollbackDatas.put("typeModule", this.typeModule);
        this.rollbackDatas.put("validation", this.validation);
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
