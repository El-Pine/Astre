package fr.elpine.astre.metier;

import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.objet.SAE;

import java.sql.*;
import java.util.ArrayList;

public class DB
{
    private Connection co;
    private PreparedStatement ps;

    public DB()
    {
        try {
            Class.forName("org.postgresql.Driver");
            co = DriverManager.getConnection("jdbc:postgresql://localhost/***REMOVED***", "***REMOVED***", "***REMOVED***");
            /*
             * Pour créer un tunnel SSH
             * -> ssh -L 5432:***REMOVED***:5432 -p 4660 ***REMOVED***@***REMOVED***
             *
             * Donc la BdD est accessible sur localhost:5432
             *
             * */
            System.out.println("connection ok");
        } catch (ClassNotFoundException | SQLException e){
            System.out.println(e);
        }
    }

    public void ajouterIntervenant(Intervenant inter)
    {
        String req = "INSERT INTO Intervenant VALUES(?,?,?,?,?)";
        try
        {
            ps = co.prepareStatement( req );
            ps.setString   (1,inter.getNom              ()  );
            ps.setString   (2,inter.getPrenom           ()  );
            ps.setString   (3,inter.getStatut().getCode ()  );
            ps.setInt      (5,inter.getService          ()  );
            ps.setDouble   (6,inter.getTotal            ()  );
            ps.executeUpdate();
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }

    public void ajouterRessource(Ressource res)
    {
        String req = "INSERT INTO Ressource VALUES (?,?,?,?,?,?,?)";
        try
        {
            ps = co.prepareStatement( req );
            ps.setString(1,res.getNom            ());
            ps.setString(2,res.getCode           ());
            ps.setString(3,res.getCommentaire    ());
            ps.setInt   (4,res.getNbHeurePn      ());
            ps.setInt   (5,res.getNbHeurePnCours ());
            //ps.setString(6,res.getHeureSemestre()); //TODO:A Compléter !!!!!
            //ps.setInt   (7,res.getNbHeuretl());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void ajouterStage(Stage stage)
    {
        String req = "INSERT INTO Stage VALUES (?,?,?,?,?,?)";
        try
        {
            ps = co.prepareStatement( req );
            ps.setString (1, stage.getNom        () );
            ps.setString (2, stage.getCode       () );
            ps.setString (3, stage.getCommentaire() );
            ps.setInt    (4,stage.getNbHeureREH  () );
            ps.setInt    (5,stage.getNbHeureTut  () );
            ps.setInt    (6,stage.getNbHeure     () );
            ps.executeUpdate();
        }
        catch (SQLException e)
        {

        }
    }

    public void ajouterCategorieHeure(CategorieHeure categorieHeure)
    {
        String req = "INSERT INTO CategorieHeure VALUES (?,?)";
        try
        {
            ps = co.prepareStatement( req );
            ps.setString (1, categorieHeure.getNom          () );
            ps.setDouble (2, categorieHeure.getEquivalentTD () );
            ps.executeUpdate();
        }
        catch (SQLException e)
        {

        }
    }
    public void ajouterCategorieIntervenant(CategorieIntervenant categorieIntervenant)
    {
        String req = "INSERT INTO CategorieIntervenant VALUES (?,?,?,?,?,?,?)";
        try
        {
            ps = co.prepareStatement( req );
            ps.setString (1, categorieIntervenant.getCode       () );
            ps.setString (2, categorieIntervenant.getNom        () );
            ps.setInt    (3, categorieIntervenant.getNbHeureMax () );
            ps.setInt    (4, categorieIntervenant.getService    () );
            ps.setDouble (5, categorieIntervenant.getRatioTd    () );
            ps.setBoolean(6, categorieIntervenant.estCompleter  () );
            ps.executeUpdate();
        }
        catch (SQLException e)
        {

        }
    }

    public ArrayList<CategorieIntervenant> getCategorieIntervenant()
    {
        ArrayList<CategorieIntervenant> resultats = new ArrayList<>();
        String req = "SELECT * FROM CategorieIntervenant";

        try (PreparedStatement ps = co.prepareStatement(req))
        {
            try (ResultSet rs = ps.executeQuery())
            {
                // Traiter les résultats du ResultSet
                while (rs.next()) {
                    CategorieIntervenant categorie = new CategorieIntervenant(
                            rs.getString ("code"         ),
                            rs.getString ("nom"          ),
                            rs.getInt    ("nbHeureMax"   ),
                            rs.getInt    ("service"      ),
                            rs.getFloat  ("ratioTP"      ),
                            rs.getBoolean("estCompleter" )
                    );
                    resultats.add(categorie);
                }
            }
        } catch (SQLException e) {
            // Gérer l'exception (journalisation, affichage, etc.)
            e.printStackTrace();
        }

        // Retourner l'ArrayList contenant les instances de CategorieIntervenant
        return resultats;
    }
    public void ajouterSAE(SAE sae)
    {
        String req = "INSERT INTO SAE VALUES (?,?,?,?,?,?)";
        try
        {
            ps = co.prepareStatement( req );
            ps.setString(1,sae.getNom()          );
            ps.setString(2,sae.getCode()         );
            ps.setString(3,sae.getCommentaire()  );
            ps.setInt   (4,sae.getNbHeurePnSem() );
            ps.setInt   (5,sae.getNbHeureTut()   );
            ps.setInt   (6,sae.getNbHeure()      );
            ps.executeUpdate();

        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
    }
}
