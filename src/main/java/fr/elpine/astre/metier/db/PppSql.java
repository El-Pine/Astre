package fr.elpine.astre.metier.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.objet.PPP;

import java.sql.*;
import java.util.ArrayList;

public class PppSql
{
    private DB db;
    private Connection co;
    private PreparedStatement ps;

    public PppSql(Controleur ctrl) { this.db = ctrl.getDb(); }

    public void ajoutPpp(PPP ppp)
    {
        String req = "INSERT INTO PPP VALUES (?,?,?,?,?,?)";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,ppp.getCode          () );
            ps.setString(2,ppp.getNom           () );
            ps.setString(3,ppp.getCommentaire   () );
            ps.setInt   (4,ppp.getNbHeureCM     () );
            ps.setInt   (5,ppp.getNbHeureTP     () );
            ps.setInt   (6,ppp.getNbHeureTD     () );
            ps.setInt   (7,ppp.getNbHeureTuto   () );
            ps.setInt   (8,ppp.getNbHeurePonct  () );
            ps.executeUpdate();
        }
        catch  (SQLException e)
        {}
    }

    public void majPpp(PPP ppp)
    {
        String req = "UPDATE PPP SET code = ?,nom = ?,commentaire = ?, nb_heure_sem = ?, semestre = ?, nb_heure_tl = ?, nb_grp = ?,nb_semaine = ? WHERE code == ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,ppp.getCode          () );
            ps.setString(2,ppp.getNom           () );
            ps.setString(3,ppp.getCommentaire   () );
            ps.setInt   (4,ppp.getNbHeureCM     () );
            ps.setInt   (5,ppp.getNbHeureTP     () );
            ps.setInt   (6,ppp.getNbHeureTD     () );
            ps.setInt   (7,ppp.getNbHeureTuto   () );
            ps.setInt   (8,ppp.getNbHeurePonct  () );
            ps.executeUpdate();
        }
        catch(SQLException e) {}
    }

    public void supprPpp(String code)
    {
        String req = "DELETE FROM PPP WHERE code = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,code);
            ps.executeUpdate();
        }
        catch (SQLException e){}
    }

    public PPP getPppByCode(String code)
    {
        String req = "SELECT * FROM PPP WHERE code = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,code);
            return (PPP) ps.executeQuery();
        }
        catch(SQLException e) {}

        return null;
    }

    public ArrayList<PPP> getPpp()
    {
        String req = "SELECT * FROM PPP";
        try
        {
            ps = co.prepareStatement(req);
            ArrayList<PPP> ensPPP  = (ArrayList<PPP>) ps.executeQuery();
            return ensPPP;
        }
        catch(SQLException e) {}
        return null;
    }
}