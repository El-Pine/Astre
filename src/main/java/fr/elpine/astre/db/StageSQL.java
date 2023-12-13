package fr.elpine.astre.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.interfaces.StageInterface;
import fr.elpine.astre.metier.objet.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class StageSQL implements StageInterface
{
    private DB db = Controleur.get().getDb();
    private Connection co;
    private PreparedStatement ps;

    public void ajoutStage(Stage Stage)
    {
        String req = "INSERT INTO Stage VALUES (?,?,?,?,?,?)";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,Stage.getCode        () );
            ps.setString(2,Stage.getNom         () );
            ps.setString(3,Stage.getCommentaire () );
            ps.setInt   (4,Stage.getNbHeureREH  () );
            ps.setInt   (5,Stage.getNbHeureTut  () );
            ps.setInt   (6,Stage.getNbHeure     () );
            ps.executeUpdate();
        }
        catch  (SQLException e)
        {}
    }

    @Override
    public void majStage(Stage Stage)
    {
        String req = "UPDATE Stage SET code = ?,nom = ?,commentaire = ?, nb_heure_pn_sem = ?, nb_heure_tl = ?,nb_semaine = ? WHERE code == ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,Stage.getCode        () );
            ps.setString(2,Stage.getNom         () );
            ps.setString(3,Stage.getCommentaire () );
            ps.setInt   (4,Stage.getNbHeureREH  () );
            ps.setInt   (5,Stage.getNbHeureTut  () );
            ps.setInt   (6,Stage.getNbHeure     () );
            ps.executeUpdate();
        }
        catch(SQLException e) {}
    }

    @Override
    public void supprStage(String code)
    {
        String req = "DELETE FROM Stage WHERE code = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,code);
            ps.executeUpdate();
        }
        catch (SQLException e){}
    }

    @Override
    public Stage getStagebyCode(String code)
    {
        String req = "SELECT * FROM Stage WHERE code = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,code);
            return (Stage) ps.executeQuery();
        }
        catch(SQLException e) {}

        return null;
    }

    @Override
    public List<Stage> getStage()
    {
        String req = "SELECT * FROM Stage";
        try
        {
            ps = co.prepareStatement(req);
            List<Stage> ensStage  = (List<Stage>) ps.executeQuery();
            return ensStage;
        }
        catch(SQLException e) {}
        return null;
    }
}
