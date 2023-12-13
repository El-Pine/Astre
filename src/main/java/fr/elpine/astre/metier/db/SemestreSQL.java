package fr.elpine.astre.metier.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.interfaces.SemestreInterface;
import fr.elpine.astre.metier.objet.Semestre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SemestreSQL implements SemestreInterface
{
    private DB db = Controleur.get().getDb();
    private Connection co;
    private PreparedStatement ps;
    public void ajoutSemestre(Semestre Semestre)
    {
        String req = "INSERT INTO Semestre VALUES (?,?,?,?,?,?)";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,Semestre.getCode          () );
            ps.setString(2,Semestre.getNom           () );
            ps.setString(3,Semestre.getCommentaire   () );
            ps.setInt   (4,Semestre.getNbHeurePnSem  () );
            ps.setInt   (5,Semestre.getNbHeureTut    () );
            ps.setInt   (6,Semestre.getNbHeure       () );
            ps.executeUpdate();
        }
        catch  (SQLException e)
        {}
    }

    @Override
    public void majSemestre(Semestre Semestre)
    {
        String req = "UPDATE Semestre SET code = ?,nom = ?,commentaire = ?, nb_heure_pn_sem = ?, nb_heure_tut = ?, nb_heure = ? WHERE code == ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,Semestre.getCode          () );
            ps.setString(2,Semestre.getNom           () );
            ps.setString(3,Semestre.getCommentaire   () );
            ps.setInt   (4,Semestre.getNbHeurePnSem  () );
            ps.setInt   (5,Semestre.getNbHeureTut    () );
            ps.setInt   (6,Semestre.getNbHeure       () );
            ps.executeUpdate();
        }
        catch(SQLException e) {}
    }

    @Override
    public void supprSemestre(String code)
    {
        String req = "DELETE FROM Semestre WHERE code = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,code);
            ps.executeUpdate();
        }
        catch (SQLException e){}
    }

    @Override
    public Semestre getSemestrebyCode(String code)
    {
        String req = "SELECT * FROM Semestre WHERE code = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,code);
            return (Semestre) ps.executeQuery();
        }
        catch(SQLException e) {}

        return null;
    }

    @Override
    public List<Semestre> getSemestre()
    {
        String req = "SELECT * FROM Semestre";
        try
        {
            ps = co.prepareStatement(req);
            List<Semestre> ensSemestre  = (List<Semestre>) ps.executeQuery();
            return ensSemestre;
        }
        catch(SQLException e) {}
        return null;
    }
}
