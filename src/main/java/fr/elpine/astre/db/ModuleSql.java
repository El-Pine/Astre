package fr.elpine.astre.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.interfaces.ModuleInterface;
import fr.elpine.astre.metier.objet.Module;


import java.sql.*;
import java.util.List;

public class ModuleSql implements ModuleInterface {

    private DB db = Controleur.get().getDb();
    private Connection co;
    private PreparedStatement ps;

    public void ajouterModule(Module md)
    {
        String req = "INSERT INTO Module VALUES (?,?,?)";
        try
        {
            ps = co.prepareStatement( req );
            ps.setString(1,md.getNom()          );
            ps.setString(2,md.getCode()         );
            ps.setString(3,md.getCommentaire()  );
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }

    }


    @Override
    public void ajoutModule(Module module) {

    }

    @Override
    public void majModule(Module module) {

    }

    @Override
    public void supprModule(int moduleId) {

    }

    @Override
    public Module getModulebyNom(String moduleNom) {
        return null;
    }

    @Override
    public List<Module> getModules() {
        return null;
    }
}
