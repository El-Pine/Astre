package fr.elpine.astre.metier.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.objet.SAE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class SaeSql
{
    private DB db = Controleur.get().getDb();
    private Connection co;
    private PreparedStatement ps;


    public void ajoutSae(SAE sae)
    {
        String req = "INSERT INTO SAE VALUES (?,?,?,?,?,?)";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,sae.getCode          () );
            ps.setString(2,sae.getNom           () );
            ps.setString(3,sae.getCommentaire   () );
            ps.setInt   (4,sae.getNbHeurePnSem  () );
            ps.setInt   (5,sae.getNbHeureTut    () );
            ps.setInt   (6,sae.getNbHeure       () );
            ps.executeUpdate();
        }
        catch  (SQLException e)
        {}
    }

    public void majSae(SAE sae)
    {
        String req = "UPDATE SAE SET code = ?,nom = ?,commentaire = ?, nb_heure_pn_sem = ?, nb_heure_tut = ?, nb_heure = ? WHERE code == ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,sae.getCode          () );
            ps.setString(2,sae.getNom           () );
            ps.setString(3,sae.getCommentaire   () );
            ps.setInt   (4,sae.getNbHeurePnSem  () );
            ps.setInt   (5,sae.getNbHeureTut    () );
            ps.setInt   (6,sae.getNbHeure       () );
            ps.executeUpdate();
        }
        catch(SQLException e) {}
    }

    public void supprSae(String code)
    {
        String req = "DELETE FROM SAE WHERE code = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,code);
            ps.executeUpdate();
        }
        catch (SQLException e){}
    }

    public SAE getSaeByCode(String code)
    {
        String req = "SELECT * FROM SAE WHERE code = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,code);
            return (SAE) ps.executeQuery();
        }
        catch(SQLException e) {}

        return null;
    }

    public ArrayList<SAE> getSae()
    {
        String req = "SELECT * FROM SAE";
        try
        {
            ps = co.prepareStatement(req);
            ArrayList<SAE> ensSAE  = (ArrayList<SAE>) ps.executeQuery();
            return ensSAE;
        }
        catch(SQLException e) {}
        return null;
    }
}
