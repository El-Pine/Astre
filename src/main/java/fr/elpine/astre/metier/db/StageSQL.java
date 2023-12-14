package fr.elpine.astre.metier.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.objet.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class StageSQL
{
    private DB db;
    private Connection co;
    private PreparedStatement ps;


    public StageSQL(Controleur ctrl)
    {
        this.db = ctrl.getDb();
    }


    public void ajoutStage(Stage stage)
    {
        String req = "INSERT INTO Stage VALUES (?,?,?,?,?,?)";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,stage.getCode        () );
            ps.setString(2,stage.getNom         () );
            ps.setString(3,stage.getCommentaire () );
            ps.setInt   (4,stage.getNbHeureREH  () );
            ps.setInt   (5,stage.getNbHeureTut  () );
            ps.setInt   (6,stage.getNbHeure     () );
            ps.executeUpdate();
        }
        catch  (SQLException e)
        {}
    }

    public void majStage(Stage stage)
    {
        String req = "UPDATE Stage SET code = ?,nom = ?,commentaire = ?, nb_heure_pn_sem = ?, nb_heure_tl = ?,nb_semaine = ? WHERE code == ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,stage.getCode        () );
            ps.setString(2,stage.getNom         () );
            ps.setString(3,stage.getCommentaire () );
            ps.setInt   (4,stage.getNbHeureREH  () );
            ps.setInt   (5,stage.getNbHeureTut  () );
            ps.setInt   (6,stage.getNbHeure     () );
            ps.executeUpdate();
        }
        catch(SQLException e) {}
    }

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

    public ArrayList<Stage> getStages()
    {
        String req = "SELECT * FROM Stage";
        try
        {
            ps = co.prepareStatement(req);
            ArrayList<Stage> ensStage  = (ArrayList<Stage>) ps.executeQuery();
            return ensStage;
        }
        catch(SQLException e) {}
        return null;
    }
}
