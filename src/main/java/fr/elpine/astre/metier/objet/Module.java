package fr.elpine.astre.metier.objet;

public class Module
{
    private String nom;
    private String code;
    private String commentaire;

    public Module(String nom, String code, String commentaire)
    {
        this.nom            = nom;
        this.code           = code;
        this.commentaire    = commentaire;
    }

    /*   GETTER    */

    public String getNom                            () { return nom                ;}
    public String getCode                           () { return code               ;}
    public String getCommentaire                    () { return commentaire        ;}

    /*   SETTER   */

    public void setNom            ( String nom        ) { this.nom         = nom        ;}
    public void setCode           ( String code       ) { this.code        = code       ;}
    public void setCommentaire    ( String commentaire) { this.commentaire = commentaire;}

}
