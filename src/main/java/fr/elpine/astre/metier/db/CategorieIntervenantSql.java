package fr.elpine.astre.metier.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.interfaces.ICategorieIntervenant;
import fr.elpine.astre.metier.interfaces.IIntervenant;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import fr.elpine.astre.metier.objet.Intervenant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CategorieIntervenantSql implements ICategorieIntervenant
{
    private DB db = Controleur.get().getDb();
    private Connection co;
    private PreparedStatement ps;


    @Override
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

    @Override
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

    @Override
    public void supprCatInter(String code)
    {
        String req = "DELETE FROM CategorieIntervenant WHERE code = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(code);
            ps.executeUpdate();
        }
        catch(SQLException e){}
    }

    @Override
    public CategorieIntervenant getcatInterbyCode(String code) {
        String req = "SELECT * FROM CategorieIntervenant WHERE code = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.
        }
    }

    @Override
    public List<CategorieIntervenant> getCatInter() {
        return null;
    }
}
