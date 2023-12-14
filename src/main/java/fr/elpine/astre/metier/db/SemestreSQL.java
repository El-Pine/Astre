package fr.elpine.astre.metier.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.objet.Semestre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class SemestreSQL
{
    private DB db = Controleur.get().getDb();
    private Connection co;
    private PreparedStatement ps;


    public void ajoutSemestre(Semestre semestre)
    {
        String req = "INSERT INTO Semestre VALUES (?,?)";
        try
        {
            ps = co.prepareStatement(req);
            ps.setInt( 1,semestre.getNumero          () );
            ps.setInt(2, semestre.getNbHeureTotPlacer() );
            ps.executeUpdate();
        }
        catch  (SQLException e)
        {}
    }

    public void majSemestre(Semestre semestre)
    {
        String req = "UPDATE Semestre SET numero = ?, nb_heure_tot_placer = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setInt(1,semestre.getNumero           () );
            ps.setInt(2,semestre.getNbHeureTotPlacer () );
            ps.executeUpdate();
        }
        catch(SQLException e) {}
    }

    public void supprSemestre(String code)
    {
        String req = "DELETE FROM Semestre WHERE numero = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setString(1,code);
            ps.executeUpdate();
        }
        catch (SQLException e){}
    }

    public Semestre getSemestreByNumero(int numero)
    {
        String req = "SELECT * FROM Semestre WHERE numero = ?";
        try
        {
            ps = co.prepareStatement(req);
            ps.setInt(1,numero);
            return (Semestre) ps.executeQuery();
        }
        catch(SQLException e) {}

        return null;
    }

    public ArrayList<Semestre> getSemestres()
    {
        String req = "SELECT * FROM Semestre";
        try
        {
            ps = co.prepareStatement(req);
            ArrayList<Semestre> ensSemestre  = (ArrayList<Semestre>) ps.executeQuery();
            return ensSemestre;
        }
        catch(SQLException e) {}
        return null;
    }
}
