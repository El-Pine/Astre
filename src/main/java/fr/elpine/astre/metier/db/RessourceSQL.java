package fr.elpine.astre.metier.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.interfaces.RessourceInterface;
import fr.elpine.astre.metier.objet.Ressource;

import java.sql.*;
import java.util.List;
public class RessourceSQL implements RessourceInterface
{
    private DB db = Controleur.get().getDb();
    private Connection co;
    private PreparedStatement ps;

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public List<Ressource> getRessource()
    {
        String req = "SELECT * FROM Ressource";
        try
        {
            ps = co.prepareStatement(req);
            List<Ressource> ensRessource  = (List<Ressource>) ps.executeQuery();
            return ensRessource;
        }
        catch(SQLException e) {}
        return null;
    }
}
