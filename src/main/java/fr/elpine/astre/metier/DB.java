package fr.elpine.astre.metier;

import fr.elpine.astre.metier.objet.*;

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


    /*-----------------*/
    /*   Intervenant   */
    /*-----------------*/

    //Méthode d'insert
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

    //Méthode d'update
    public void majIntervenant(Intervenant inter)
    {
        String req = "UPDATE Intervenant SET nom = ?, prenom = ?, mail = ?, statut = ?, service = ?, total = ? WHERE nom = ? AND prenom = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1,                 inter.getNom    ()           );
            ps.setString(2,                 inter.getPrenom ()           );
            ps.setString(3,                 inter.getEmail  ()           );
            ps.setString(4,                 inter.getStatut ().getCode() );
            ps.setString(5,Integer.toString(inter.getService())          );
            ps.setString(6,Double .toString(inter.getTotal  ())          );
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode delete
    public void supprimerIntervenant(Intervenant inter)
    {
        String req = "DELETE FROM Intervenant WHERE nom = ? AND prenom = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1, inter.getNom   () );
            ps.setString(2, inter.getPrenom() );
            ps.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode select *
    public ArrayList<Intervenant> getAllIntervenant()
    {
        ArrayList<Intervenant> resultats = new ArrayList<>();
        String req = "SELECT * FROM Intervenant";

        try (PreparedStatement ps = co.prepareStatement(req))
        {
            try (ResultSet rs = ps.executeQuery())
            {
                // Traiter les résultats du ResultSet
                while (rs.next()) {
                    Intervenant categorie = new Intervenant(
                            rs.getString                     ("nom"     ),
                            rs.getString                     ("prenom"  ),
                            rs.getString                     ("mail"    ),
                            selectCatInterByCode(rs.getString("statut") ),
                            rs.getInt                        ("service" ),
                            rs.getFloat                      ("total"   )
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


    /*-------------------------*/
    /*  Catégorie Intervenant  */
    /*-------------------------*/


    //Méthode d'insert
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
            ps.executeUpdate();
        }
        catch (SQLException e)
        {

        }
    }

    //Méthode : SELECT * FROM CategorieIntervenant
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
                            rs.getFloat  ("ratioTP"      )
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

    //Méthode d'update
    public void majCategorieIntervenant(CategorieIntervenant catInter)
    {
        String req = "UPDATE CategorieIntervenant SET code = ?, nom = ?, nbHeureMax = ?, service = ?, ratioTP = ? WHERE code = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString (1,       catInter.getCode       ());
            ps.setString (2,       catInter.getNom        ());
            ps.setInt    (3,       catInter.getNbHeureMax ());
            ps.setInt    (4,       catInter.getService    ());
            ps.setFloat  (5,(float)catInter.getRatioTd    ());
            ps.setString (6,       catInter.getCode       ());
            ps.executeUpdate();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode de delete
    public void supprimerCatIntervenant(CategorieIntervenant catInter)
    {
        String req = "DELETE FROM CategorieIntervenant WHERE code = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1,catInter.getCode());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public CategorieIntervenant selectCatInterByCode(String code)
    {
        String req = "SELECT * FROM CategorieIntervenant WHERE code = ?";
        try (PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1,code);
            try (ResultSet rs = ps.executeQuery())
            {
                // Traiter les résultats du ResultSet
                while (rs.next()) {
                    CategorieIntervenant categorie = new CategorieIntervenant(
                            rs.getString ("code"                ),
                            rs.getString ("nom"                 ),
                            rs.getInt    ("NbHeureMaxDefaut"    ),
                            rs.getInt    ("nbHeureServiceDefaut"),
                            rs.getDouble ("ratioTPDefaut"       )
                    );
                    return categorie;
                }
            }
        } catch (SQLException e) {
            // Gérer l'exception (journalisation, affichage, etc.)
            e.printStackTrace();
        }
        return null;
    }

    /*-------------------*/
    /*  Categorie Heure  */
    /*-------------------*/

    //TODO:A faire après la mise a jour de la BADO
    /*
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

    public ArrayList<CategorieHeure> getCategorieHeure()
    {
        ArrayList<CategorieHeure> resultats = new ArrayList<>();
        String req = "SELECT * FROM CategorieHeure";

        try (PreparedStatement ps = co.prepareStatement(req))
        {
            try (ResultSet rs = ps.executeQuery())
            {
                // Traiter les résultats du ResultSet
                while (rs.next()) {
                    CategorieHeure categorie = new CategorieHeure(
                            rs.getString()
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
    */
}
