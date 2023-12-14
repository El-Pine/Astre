package fr.elpine.astre.metier.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.objet.Ressource;

import java.sql.*;
import java.util.ArrayList;

public class RessourceSql
{
    private DB db;
    private Connection co;
    private PreparedStatement ps;


    public RessourceSql(Controleur ctrl) { this.db = ctrl.getDb(); }


    public void ajoutRessource(Ressource ressource)
    {
        String req = "INSERT INTO Ressource VALUES (?,?,?,?,?,?,?,?)";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,ressource.getCode                               () );
            ps.setString(2,ressource.getNom                                () );
            ps.setString(3,ressource.getCommentaire                        () );
            ps.setInt   (4,ressource.getHeureSemestre(ressource.getSemestre()));
            ps.setInt   (5,ressource.getSemestre                           () );
            ps.setInt   (6,ressource.getNbHeuretl                          () );
            ps.setInt   (7,ressource.getNbGrp                              () );
            ps.setInt   (8,ressource.getNbSemaine                          () );
            ps.executeUpdate();
        }
        catch  (SQLException e)
        {}
    }

    public void majRessource(Ressource ressource)
    {
        String req = "UPDATE Ressource SET code = ?,nom = ?,commentaire = ?, nb_heure_sem = ?, semestre = ?, nb_heure_tl = ?, nb_grp = ?,nb_semaine = ? WHERE code == ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString (1,ressource.getCode                               () );
            ps.setString (2,ressource.getNom                                () );
            ps.setString (3,ressource.getCommentaire                        () );
            ps.setInt    (4,ressource.getHeureSemestre(ressource.getSemestre()));
            ps.setInt    (5,ressource.getSemestre                           () );
            ps.setInt    (6,ressource.getNbHeuretl                          () );
            ps.setInt    (7,ressource.getNbGrp                              () );
            ps.setInt    (8,ressource.getNbSemaine                          () );
            ps.setString (9,ressource.getCode                               () );
            ps.executeUpdate();
        }
        catch(SQLException e) {}
    }

    public void supprRessource(String code)
    {
        String req = "DELETE FROM Ressource WHERE code = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,code);
            ps.executeUpdate();
        }
        catch (SQLException e){}
    }

    public Ressource getRessourcebyCode(String code)
    {
        String req = "SELECT * FROM Ressource WHERE code = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,code);
            return (Ressource) ps.executeQuery();
        }
        catch(SQLException e) {}

        return null;
    }

    public ArrayList<Ressource> getRessources()
    {
        String req = "SELECT * FROM Ressource";
        try
        {
            ps = co.prepareStatement(req);
            ArrayList<Ressource> ensRessource  = (ArrayList<Ressource>) ps.executeQuery();
            return ensRessource;
        }
        catch(SQLException e) {}
        return null;
    }
}