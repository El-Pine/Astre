package fr.elpine.astre.objet;

import java.util.ArrayList;

public class Module
{
    private String nom;
    private String code;
    private String commentaire;
    private ArrayList<Intervenant> ensIntervenant;

    public Module(String nom, String code, String commentaire)
    {
        this.nom            = nom;
        this.code           = code;
        this.commentaire    = commentaire;
        this.ensIntervenant = new ArrayList<>();
    }

    public String getNom                            () { return nom                ;}
    public String getCode                           () { return code               ;}
    public String getCommentaire                    () { return commentaire        ;}
    public ArrayList<Intervenant> getEnsIntervenant () { return this.ensIntervenant;}

    public void setNom            ( String nom        ) { this.nom         = nom        ;}
    public void setCode           ( String code       ) { this.code        = code       ;}
    public void setCommentaire    ( String commentaire) { this.commentaire = commentaire;}
    public void ajouterIntervenant(Intervenant inter  ) { this.ensIntervenant.add(inter);}


}
