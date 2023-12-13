package fr.elpine.astre.metier.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.interfaces.ModuleInterface;
import fr.elpine.astre.metier.objet.Module;


import java.sql.*;
import java.util.ArrayList;

public class ModuleSql implements ModuleInterface {

    private DB db = Controleur.get().getDb();
    private Connection co;
    private PreparedStatement ps;


    @Override
    public ArrayList<Module> getModules()
    {
        ArrayList<Module> ensModule = new ArrayList<>();
        String req = "SELECT * FROM Ressource";
        try
        {
            ps = co.prepareStatement(req);
            ensModule.add((Module)ps.executeQuery());

            req = "SELECT * FROM SAE";
            ps = co.prepareStatement(req);
            ensModule.add((Module)ps.executeQuery());

            req = "SELECT * FROM Stage";
            ps = co.prepareStatement(req);
            ensModule.add((Module)ps.executeQuery());

            req = "SELECT * FROM PPP";
            ps = co.prepareStatement(req);
            ensModule.add((Module)ps.executeQuery());
        }
        catch(SQLException e) {}

        return ensModule;
    }

}
