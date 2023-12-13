package fr.elpine.astre.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.interfaces.RessourceInterface;
import fr.elpine.astre.metier.objet.Ressource;

import java.sql.*;
import java.util.ArrayList;
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
            ps.setString(1,ressource.getCode         ());
            ps.setString(2,ressource.getNom          ());
            ps.setString(3,ressource.getCommentaire  ());
            ps.setInt   (4,ressource.get);


        }
        catch  (SQLException e)
        {}




    }

    @Override
    public void majRessource(Ressource ressource) {

    }

    @Override
    public void supprRessource(String code) {

    }

    @Override
    public Ressource getRessourcebyCode(String code) {
        return null;
    }

    @Override
    public List<Ressource> getRessource() {
        return null;
    }
}
