package fr.elpine.astre.metier.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.interfaces.SAEInterface;
import fr.elpine.astre.metier.objet.SAE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SAESQL implements SAEInterface
{
    private DB db = Controleur.get().getDb();
    private Connection co;
    private PreparedStatement ps;
    public void ajoutSAE(SAE SAE)
    {
        String req = "INSERT INTO SAE VALUES (?,?,?,?,?,?)";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,SAE.getCode          () );
            ps.setString(2,SAE.getNom           () );
            ps.setString(3,SAE.getCommentaire   () );
            ps.setInt   (4,SAE.getNbHeurePnSem  () );
            ps.setInt   (5,SAE.getNbHeureTut    () );
            ps.setInt   (6,SAE.getNbHeure       () );
            ps.executeUpdate();
        }
        catch  (SQLException e)
        {}
    }

    @Override
    public void majSAE(SAE SAE)
    {
        String req = "UPDATE SAE SET code = ?,nom = ?,commentaire = ?, nb_heure_pn_sem = ?, nb_heure_tut = ?, nb_heure = ? WHERE code == ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,SAE.getCode          () );
            ps.setString(2,SAE.getNom           () );
            ps.setString(3,SAE.getCommentaire   () );
            ps.setInt   (4,SAE.getNbHeurePnSem  () );
            ps.setInt   (5,SAE.getNbHeureTut    () );
            ps.setInt   (6,SAE.getNbHeure       () );
            ps.executeUpdate();
        }
        catch(SQLException e) {}
    }

    @Override
    public void supprSAE(String code)
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

    @Override
    public SAE getSAEbyCode(String code)
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

    @Override
    public List<SAE> getSAE()
    {
        String req = "SELECT * FROM SAE";
        try
        {
            ps = co.prepareStatement(req);
            List<SAE> ensSAE  = (List<SAE>) ps.executeQuery();
            return ensSAE;
        }
        catch(SQLException e) {}
        return null;
    }
}
