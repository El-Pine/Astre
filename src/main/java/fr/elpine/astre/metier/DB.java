package fr.elpine.astre.metier;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.objet.Module;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DB
{
    private Connection co;
    private PreparedStatement ps;

    public DB()
    {
        AstreApplication.erreur = !connectionDb();
    }

    /*-----------------*/
    /*  Verifications  */
    /*-----------------*/

    public boolean verify() throws SQLException
    {
        boolean valid = true;

        for (String table : Arrays.asList(
                "AffectationPPP",
                "AffectationStage",
                "AffectationSAE",
                "AffectationRessource",
                "Attribution",
                "Intervenant",
                "CategorieIntervenant",
                "CategorieHeure",
                "Module",
                "Semestre",
                "Annee"
        ))
        {
            ResultSet set = co.getMetaData().getTables(null, null, table.toLowerCase(), null);
            if (!set.next())
            {
                valid = false; System.out.printf("Table %s introuvable !\n", table);
            }
        }

        return valid;
    }

    public void reset() throws SQLException
    {
        co.createStatement().executeUpdate(loadSQL("scriptCreation.sql"));
    }

    public boolean reloadDb()
    {
        String filePath = "DB.java"; // Chemin vers votre fichier

        try {
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder content = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.contains("DriverManager.getConnection")) {
                    // Remplacer la ligne par "bonjour"
                    content.append("bonjour").append(System.lineSeparator());
                } else {
                    content.append(line).append(System.lineSeparator());
                }
            }

            reader.close();

            // Écrire le contenu modifié dans le fichier
            FileWriter writer = new FileWriter(file);
            writer.write(content.toString());
            writer.close();

            System.out.println("Remplacement effectué avec succès.");
        } catch (IOException e) { return false; }
        return true;
    }

    public boolean connectionDb()
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

            boolean valid = verify();

            if (valid) System.out.println("VALID !");
            else {
                System.out.println("reset . . .");
                reset();
                System.out.println("reset ok !");
            }
        } catch (ClassNotFoundException | SQLException e){ return false; }
        return true;
    }

    private static String loadSQL(String file)
    {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(DB.class.getResourceAsStream(file)))))
        {
            reader.lines().forEach(line -> content.append(line).append('\n'));
        }
        catch (Exception e) { e.printStackTrace(); }

        return content.toString();
    }


    /*-----------------*/
    /*     Module      */
    /*-----------------*/

    //Méthode d'insert
    public void ajouterModule(Module module)
    {
        String req = "INSERT INTO Module VALUES(?,?,?,?,?)";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString  (1,module.getCode        ());
            ps.setString  (2,module.getNom         ());
            ps.setString  (3,module.getAbreviation ());
            ps.setString  (4,module.getTypeModule  ());
            ps.setBoolean (5,module.estValide      ());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode d'update
    public void majModule(Module module)
    {
        String req = "UPDATE Module SET code = ?, nom = ?, abreviation = ?, typeModule = ?, validation = ? WHERE code = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString  (1,module.getCode        ());
            ps.setString  (2,module.getNom         ());
            ps.setString  (3,module.getAbreviation ());
            ps.setString  (4,module.getTypeModule  ());
            ps.setBoolean (5,module.estValide      ());
            ps.setString  (6,module.getCode        ());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode delete
    public void supprimerModule(Module module)
    {
        String req = "DELETE FORM Module WHERE code = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1,module.getCode());
            ps.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode select all
    public ArrayList<Module> getAllModule()
    {
        ArrayList<Module> ensModule = new ArrayList<>();
        String req = "SELECT * FROM Module";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            try(ResultSet rs = ps.executeQuery())
            {
                while(rs.next())
                {
                    Module mod = new Module(rs.getString ("nom"        ),
                                            rs.getString ("code"       ),
                                            rs.getString ("abreviation"),
                                            rs.getString ("typeModule" ),
                                            rs.getBoolean("validation" ));
                    ensModule.add(mod);
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public Module getModuleByNumero(String numero)
    {
        String req = "SELECT * FROM Module WHERE code = ?";
        try (PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1,numero);
            try (ResultSet rs = ps.executeQuery())
            {
                // Traiter les résultats du ResultSet
                while (rs.next()) {
                    Module mod = new Module(rs.getString ("nom"        ),
                                            rs.getString ("code"       ),
                                            rs.getString ("abreviation"),
                                            rs.getString ("typeModule" ),
                                            rs.getBoolean("validation" ));
                    return mod;
                }
            }
        } catch (SQLException e) {
            // Gérer l'exception (journalisation, affichage, etc.)
            e.printStackTrace();
        }
        return null;
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

    /*--------------*/
    /*   SEMESTRE   */
    /*--------------*/

    //Méthode insert
    public void ajouterSemestre(Semestre semestre)
    {
        String req = "INSERT INTO Semestre VALUES (?,?,?,?,?)";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setInt(1,semestre.getNumero   ());
            ps.setInt(2,semestre.getNbGrpTD  ());
            ps.setInt(3,semestre.getNbGrpTP  ());
            ps.setInt(4,semestre.getNbEtd    ());
            ps.setInt(5,semestre.getNbSemaine());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode d'update
    public void majSemestre(Semestre semestre)
    {
        String req = "UPDATE Semestre SET numero = ?, nbGrpTD = ?, nbGrpTP = ?, nbEtd = ?, nbSemaine = ? WHERE numero = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setInt(1,semestre.getNumero   () );
            ps.setInt(2,semestre.getNbGrpTD  () );
            ps.setInt(3,semestre.getNbGrpTP  () );
            ps.setInt(4,semestre.getNbEtd    () );
            ps.setInt(5,semestre.getNbSemaine() );
            ps.setInt(6,semestre.getNbSemaine() );
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode delete
    public void supprimerSemestre(Semestre semestre)
    {
        String req = "DELETE FROM Semestre WHERE numero = ? AND annee = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setInt   (1,semestre.getNumero()          );
            ps.setString(2,semestre.getAnnee ().getNumero() );
            ps.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode select *
    public ArrayList<Semestre> getAllSemestre()
    {
        ArrayList<Semestre> ensSemestre = new ArrayList<>();
        String req                      = "SELECT * FROM Semestre";
        try (PreparedStatement ps = co.prepareStatement(req))
        {
            try (ResultSet rs = ps.executeQuery())
            {
                // Traiter les résultats du ResultSet
                while (rs.next()) {
                    Semestre semestre = new Semestre(
                            rs.getInt                      ("numero"    ),
                            rs.getInt                      ("nbGrpTD"   ),
                            rs.getInt                      ("nbGrpTP"   ),
                            rs.getInt                      ("nbEtd"     ),
                            rs.getInt                      ("nbSemaine" ),
                            getAnneeByNumero((rs.getString ("annee"     ))
                            ));
                    ensSemestre.add(semestre);
                }
            }
        } catch (SQLException e) {
            // Gérer l'exception (journalisation, affichage, etc.)
            e.printStackTrace();
        }
        return ensSemestre;
    }

    /*-----------*/
    /*   Annee   */
    /*-----------*/

    // Méthode insert
    public void ajouterAnnee(Annee annee) {
        String req = "INSERT INTO Annee VALUES (?,?,?)";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setString(1, annee.getNumero());
            ps.setString(2, annee.getDateDeb());
            ps.setString(3, annee.getDateFin());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode d'update
    public void majAnnee(Annee annee) {
        String req = "UPDATE Annee SET numero = ?, dateDeb = ?, dateFin = ? WHERE numero = ?";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setString(1, annee.getNumero());
            ps.setString(2, annee.getDateDeb());
            ps.setString(3, annee.getDateFin());
            ps.setString(4, annee.getNumero());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode delete
    public void supprimerAnnee(Annee annee) {
        String req = "DELETE FROM Annee WHERE numero = ?";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setString(1, annee.getNumero());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Annee> getAllAnnee() {
        ArrayList<Annee> ensAnnee = new ArrayList<>();
        String req = "SELECT * FROM Annee";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            try (ResultSet rs = ps.executeQuery()) {
                // Traiter les résultats du ResultSet
                while (rs.next()) {
                    Annee annee = new Annee(
                            rs.getString("numero"),
                            rs.getString("dateDeb"),
                            rs.getString("dateFin")
                    );
                    ensAnnee.add(annee);
                }
            }
        } catch (SQLException e) {
            // Gérer l'exception (journalisation, affichage, etc.)
            e.printStackTrace();
        }
        return ensAnnee;
    }

    public Annee getAnneeByNumero(String numero)
    {
        String req = "SELECT * FROM Annee WHERE numero = ?";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setString(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                // Traiter les résultats du ResultSet
                while (rs.next()) {
                    Annee annee = new Annee(
                            rs.getString("numero"),
                            rs.getString("dateDeb"),
                            rs.getString("dateFin")
                    );
                    return annee;
                }
            }
        } catch (SQLException e) {
            // Gérer l'exception (journalisation, affichage, etc.)
            e.printStackTrace();
        }
        return null;
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

    //Méthode insert
    public void ajouterCategorieHeure(CategorieHeure categorieHeure)
    {
        String req = "INSERT INTO CategorieHeure VALUES (?,?,?,?,?,?)";
        try
        {
            ps = co.prepareStatement( req );
            ps.setString (1, categorieHeure.getNom          () );
            ps.setDouble (2, categorieHeure.getEquivalentTD () );
            ps.setBoolean(3, categorieHeure.estRessource    () );
            ps.setBoolean(4, categorieHeure.estSae          () );
            ps.setBoolean(5, categorieHeure.estPpp          () );
            ps.setBoolean(6, categorieHeure.estStage        () );
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode update
    public void majCategorieHeure(CategorieHeure catHr)
    {
        String req = "UPDATE CategorieHeure SET nom = ?,eqtd = ?,ressource = ?, sae = ?, ppp = ?,stage = ? WHERE nom = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString  (1,catHr.getNom         ());
            ps.setDouble  (2,catHr.getEquivalentTD());
            ps.setBoolean (3,catHr.estRessource   ());
            ps.setBoolean (4,catHr.estSae         ());
            ps.setBoolean (5,catHr.estStage       ());
            ps.setBoolean (6,catHr.estPpp         ());
            ps.setString  (7,catHr.getNom         ());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode delete
    public void supprimerCategorieHeure(CategorieHeure catHr)
    {
        String req = "DELETE FROM CategorieHeure WHERE nom = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1,catHr.getNom());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode select *
    public ArrayList<CategorieHeure> getAllCategorieHeure()
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
                            rs.getString  ("nom"       ),
                            rs.getDouble  ("eqtd"      ),
                            rs.getBoolean ("ressource" ),
                            rs.getBoolean ("sae"       ),
                            rs.getBoolean ("ppp"       ),
                            rs.getBoolean ("stage"     )
                    );
                    resultats.add(categorie);
                }
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return resultats;
    }

    public CategorieHeure getCatHrByNom(String nom)
    {
        String req = "SELECT * FROM CategorieHeure WHERE nom = ?";
        try (PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1,nom);
            try (ResultSet rs = ps.executeQuery())
            {
                // Traiter les résultats du ResultSet
                while (rs.next()) {
                    CategorieHeure categorie = new CategorieHeure(
                            rs.getString  ("nom"       ),
                            rs.getDouble  ("eqtd"      ),
                            rs.getBoolean ("ressource" ),
                            rs.getBoolean ("sae"       ),
                            rs.getBoolean ("ppp"       ),
                            rs.getBoolean ("stage"     )
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


    /*---------------------------*/
    /*        ASSOCIATION        */
    /*---------------------------*/

    // Méthode insert pour la classe Attribution
    public void ajouterAttribution(Attribution attribution) {
        String req = "INSERT INTO Attribution VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setString(1, attribution.getCodeModule           ());
            ps.setInt   (2, attribution.getNumeroSemestreModule ());
            ps.setString(3, attribution.getAnneeModule          ());
            ps.setString(4, attribution.getNomCategorieHeure    ());
            ps.setInt   (5, attribution.getNbHeure              ());
            ps.setInt   (6, attribution.getNbSemaine            ());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode d'update pour la classe Attribution
    public void majAttribution(Attribution att) {
        String req = "UPDATE Attribution SET codeModule = ?, numeroSemestreModule = ?, anneeModule = ?, nomCategorieHeure = ?, nbHeure = ?, nbSemaine = ? WHERE numeroSemestreModule = ? AND codeModule = ?, anneeModule = ?, nomCategorieHeure = ?";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setString (1 ,att.getCodeModule           ());
            ps.setInt    (2 ,att.getNumeroSemestreModule ());
            ps.setString (3 ,att.getAnneeModule          ());
            ps.setString (4 ,att.getNomCategorieHeure    ());
            ps.setInt    (5 ,att.getNbHeure              ());
            ps.setInt    (6 ,att.getNbSemaine            ());
            ps.setString (7 ,att.getCodeModule           ());
            ps.setInt    (8 ,att.getNumeroSemestreModule ());
            ps.setString (9 ,att.getAnneeModule          ());
            ps.setString (10,att.getNomCategorieHeure    ());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Méthode delete pour la classe Attribution
    public void supprimerAttribution(Attribution att) {
        String req = "DELETE FROM Attribution WHERE codeModule = ? AND numeroSemestreModule = ? AND anneeModule = ? AND nomCategorieHeure = ?";
        try (PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString (1,att.getCodeModule           ());
            ps.setInt    (2,att.getNumeroSemestreModule ());
            ps.setString (3,att.getAnneeModule          ());
            ps.setString (4,att.getNomCategorieHeure    ());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode select * pour la classe Attribution
    public ArrayList<Attribution> getAllAttribution() {
        ArrayList<Attribution> ensAttribution = new ArrayList<>();
        String req = "SELECT * FROM Attribution";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            try (ResultSet rs = ps.executeQuery()) {
                // Traiter les résultats du ResultSet
                while (rs.next()) {
                    Attribution attribution = new Attribution(
                                              rs.getInt   ("numeroSemestreModule"),
                                              rs.getInt   ("nbHeure"             ),
                                              rs.getInt   ("nbSemaine"           ),
                                              rs.getString("anneeModule"         ),
                                              rs.getString("nomCategorieHeure"   ),
                                              rs.getString("codeModule"          ),
                            getModuleByNumero(rs.getString("module"              )),
                            getCatHrByNom    (rs.getString("catHr"               ))
                    );
                    ensAttribution.add(attribution);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ensAttribution;
    }
}
