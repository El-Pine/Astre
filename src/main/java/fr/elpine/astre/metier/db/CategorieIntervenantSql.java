package fr.elpine.astre.metier.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.objet.CategorieIntervenant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategorieIntervenantSql
{
    private DB db = Controleur.get().getDb();
    private Connection co;
    private PreparedStatement ps;


    public void ajoutCatInter(CategorieIntervenant catInter)
    {
        String req = "INSERT INTO CategorieIntervenant VALUES (?,?,?,?,?,?)";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString (1,catInter.getCode      () );
            ps.setString (2,catInter.getNom       () );
            ps.setInt    (3,catInter.getNbHeureMax() );
            ps.setInt    (4,catInter.getService   () );
            ps.setDouble (5,catInter.getRatioTd   () );
            ps.setBoolean(6,catInter.estCompleter () );
            ps.executeUpdate();
        }
        catch(SQLException e){}
    }

    public void majCatInter(CategorieIntervenant catInter)
    {
        String req = "UPDATE CategorieIntervenant SET code = ?, nom = ?, nbHeureMax = ?, service = ?, ratioTP = ?, estCompleter = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString (1,catInter.getCode      () );
            ps.setString (2,catInter.getNom       () );
            ps.setInt    (3,catInter.getNbHeureMax() );
            ps.setInt    (4,catInter.getService   () );
            ps.setDouble (5,catInter.getRatioTd   () );
            ps.setBoolean(6,catInter.estCompleter () );
            ps.executeUpdate();
        }
        catch(SQLException e){}
    }

    public void supprCatInter(String code)
    {
        String req = "DELETE FROM CategorieIntervenant WHERE code = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,code);
            ps.executeUpdate();
        }
        catch(SQLException e){}
    }

    public CategorieIntervenant getcatInterbyCode(String code) {
        String req = "SELECT * FROM CategorieIntervenant WHERE code = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,code);
            return (CategorieIntervenant) ps.executeQuery();
        }
        catch (SQLException e){}
        return null;
    }

    public ArrayList<CategorieIntervenant> getCatInters() {
        return null;
    }
}
