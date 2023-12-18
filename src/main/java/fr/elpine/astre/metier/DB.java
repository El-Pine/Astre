package fr.elpine.astre.metier;

import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.stage.StagePrincipal;
import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.objet.Module;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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
        // Verification de l'existance du fichier de connexion
        File fichier = new File("infoBd.txt");
        try { fichier.createNewFile(); } catch (IOException e) { throw new RuntimeException(e); }

        // Connexion
        AstreApplication.erreur = !this.reloadDB();

        /*
         * Pour créer un tunnel SSH
         * -> ssh -L 5432:***REMOVED***:5432 -p 4660 ***REMOVED***@***REMOVED***
         *
         * Donc la BdD est accessible sur localhost:5432
         *
         * */
    }

    /*-----------------*/
    /*    Connexion    */
    /*-----------------*/

    private boolean connexion(String ip, int port, String database, String identifiant, String password)
    {
        try {
            Class.forName("org.postgresql.Driver");
            co = DriverManager.getConnection(String.format("jdbc:postgresql://%s:%d/%s", ip, port, database), identifiant, password);

            try {
                if (!this.verify()) this.reset();
            }
            catch (Exception e) { e.printStackTrace(); }

            return true;
        } catch (Exception e) { return false; }
    }

    public boolean reloadDB()
    {
        String[] elements = this.getInformations();

        return this.connexion( elements[0], Integer.parseInt(elements[1]), elements[2], elements[3], elements[4] );
    }

    public boolean reloadDB( String ip, int port, String database, String identifiant, String password )
    {
        boolean valid = this.connexion( ip, port, database, identifiant, password );

        if (valid)
            try
            {
                FileWriter writer = new FileWriter("infoBd.txt", false);

                writer.write(String.format("%s\t%s\t%s\t%s\t%s", ip, port, database, identifiant, password));
                writer.close();
            }
            catch (IOException e) { e.printStackTrace(); }

        return valid;
    }

    public String[] getInformations()
    {
        String[] elements = null;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("infoBd.txt"));

            String line = reader.readLine();
            if ( line != null ) elements = line.split("\t");
        }
        catch (IOException e) { e.printStackTrace(); }

        if ( elements == null || elements.length != 5 ) { return null; }

        return elements;
    }

    /*-----------------*/
    /*  Verifications  */
    /*-----------------*/

    public boolean verify() throws SQLException
    {
        boolean valid = true;

        for (String table : Arrays.asList(
                "Affectation", "Attribution", "Intervenant", "CategorieIntervenant", "CategorieHeure", "Module", "Semestre", "Annee"
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
                    Module mod = new Module(rs.getString ("nom"           ),
                                            rs.getString ("code"          ),
                                            rs.getString ("abreviation"   ),
                                            rs.getString ("typeModule"    ),
                                            rs.getBoolean("validation"    ),
                            getSemestreById(rs.getInt    ("numeroSemestre"),rs.getString("annee")));
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
                    Module mod = new Module(rs.getString ("nom"           ),
                                            rs.getString ("code"          ),
                                            rs.getString ("abreviation"   ),
                                            rs.getString ("typeModule"    ),
                                            rs.getBoolean("validation"    ),
                            getSemestreById(rs.getInt    ("numeroSemestre"),rs.getString("annee"))
                    );
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
        String req = "INSERT INTO Intervenant (nom, prenom, mail, codeCategorie, heureService, heureMax, ratioTP) VALUES (?, ?, ?, ?,?, ?, ?)";
        try
        {
            ps = co.prepareStatement( req );
            ps.setString   (1,inter.getNom              ()  );
            ps.setString   (2,inter.getPrenom           ()  );
            ps.setString   (3, inter.getEmail           ()  );
            ps.setString   (4,inter.getStatut().getCode ()  );
            ps.setInt      (5,inter.getService          ()  );
            ps.setInt      (6,inter.getHeureMax         ()  );
            ps.setDouble   (7,inter.getRatioTP          ()  );
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
            ps.setString(6,Integer.toString(inter.getHeureMax  ())          );
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
                while (rs.next())
                {
                    Intervenant inter = Intervenant.creerIntervenant(
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        selectCatInterByCode(rs.getString(5)),
                        rs.getInt(6),
                        rs.getInt(7),
                        rs.getDouble(8));
                    System.out.println("jee suis la ");
                    resultats.add(inter);
                }
            }
        } catch (SQLException e) {
            // Gérer l'exception (journalisation, affichage, etc.)
            e.printStackTrace();
        }

        // Retourner l'ArrayList contenant les instances de CategorieIntervenant
        return resultats;
    }

    public Intervenant getIntervenantById(int code)
    {
        String req = "SELECT * FROM Intervenant WHERE idInter = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setInt(1,code);
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    Intervenant inter = Intervenant.creerIntervenant(
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            selectCatInterByCode(rs.getString(5)),
                            rs.getInt(6),
                            rs.getInt(7),
                            rs.getDouble(8));
                    return inter;
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
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

    public Semestre getSemestreById(int numeroSemestre, String annee)
    {
        String req = "SELECT * FROM Semestre WHERE numero = ? AND annee = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setInt(1,numeroSemestre);
            ps.setString(2,annee);
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    Semestre semestre = new Semestre(
                            rs.getInt                      ("numero"    ),
                            rs.getInt                      ("nbGrpTD"   ),
                            rs.getInt                      ("nbGrpTP"   ),
                            rs.getInt                      ("nbEtd"     ),
                            rs.getInt                      ("nbSemaine" ),
                            getAnneeByNumero((rs.getString ("annee"     ))
                            ));
                    return semestre;
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
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
            e.printStackTrace();
        }
    }

    //Méthode : SELECT * FROM CategorieIntervenant
    public ArrayList<CategorieIntervenant> getAllCategorieIntervenant()
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
                            rs.getString (1),
                            rs.getString (2),
                            rs.getInt    (3),
                            rs.getInt    (4),
                            rs.getFloat  (5)
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

    /*-----------------------*/
    /* Affectation Ressource */
    /*-----------------------*/

    //Méthode insert
    public void ajouterAff(Affectation affs)
    {
        String req = "INSERT INTO Affectation VALUES(?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1,affs.getCodeModule          ()         );
            ps.setInt   (2,affs.getNumeroSemestreModule()         );
            ps.setString(3,affs.getAnneeModule         ()         );
            ps.setString(5,affs.getTypeHeure           ().getNom());
            ps.setInt   (6,affs.getNbGroupe            ()         );
            ps.setInt   (7,affs.getNbSemaine           ()         );
            ps.setInt   (8,affs.getNbHeure             ()         );
            ps.setString(9,affs.getCommentaire         ()         );
            ps.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode d'update
    public void updateaff(Affectation aff)
    {
        String req = "UPDATE Affectation SET codeModule = ?, numeroSemesreModule = ?, anneeModule = ?, idInter = ?, typeHeure = ?, nbGroupe = ?, nbSemaine = ?, nbHeure = ?, commentaire = ? WHERE codeModule = ? AND numeroSemestreModule = ? AND anneeModule = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            //SET
            ps.setString (1,aff.getCodeModule          ());
            ps.setInt    (2,aff.getNumeroSemestreModule());
            ps.setString (3,aff.getAnneeModule         ());
            ps.setString (5,aff.getTypeHeure           ().getNom());
            ps.setInt    (6,aff.getNbGroupe            ());
            ps.setInt    (7,aff.getNbSemaine           ());
            ps.setInt    (8,aff.getNbHeure             ());
            ps.setString (9,aff.getCommentaire         ());

            // WHERE
            ps.setString (1,aff.getCodeModule          ());
            ps.setInt    (2,aff.getNumeroSemestreModule());
            ps.setString (3,aff.getAnneeModule         ());
        }
        catch (SQLException e )
        {
            e.printStackTrace();
        }
    }

    //Méthode delete
    public void supprimeraff(Affectation aff)
    {
        String req = "DELETE FROM Affectation WHERE codeModule = ? AND numeroSemestreModule = ? AND anneeModule = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString (1,aff.getCodeModule           ());
            ps.setInt    (2,aff.getNumeroSemestreModule ());
            ps.setString (3,aff.getAnneeModule          ());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode select *
    public ArrayList<Affectation> getAllaff()
    {
        ArrayList<Affectation> ensaff = new ArrayList<>();
        String req = "SELECT * FROM Attribution";
        try (PreparedStatement ps = co.prepareStatement(req))
        {
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next()) {
                    Affectation Affectation = new Affectation(
                            getModuleByNumero( rs.getString("codeModule"          )),
                                               rs.getInt   ("numeroSemestreModule"),
                                               rs.getString("anneeModule"         ),
                            getIntervenantById(rs.getInt   ("idInter")             ),
                            getCatHrByNom     (rs.getString("typeHeure"           )),
                                               rs.getInt   ("nbGroupe"            ),
                                               rs.getInt   ("nbSemaine"           ),
                                               rs.getInt   ("nbHeure"             ),
                                               rs.getString("commentaire"         )

                    );

                    ensaff.add(Affectation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ensaff;
    }
}
