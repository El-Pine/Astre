package fr.elpine.astre.metier;

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

    public DB()
    {
        // Verification de l'existance du fichier de connexion
        File fichier = new File("infoBd.txt");
        try { fichier.createNewFile(); } catch (IOException e) { throw new RuntimeException(e); }

        // Connexion
        AstreApplication.erreur = !this.reloadDB();

        /*
         * Pour créer un tunnel SSH
         * -> ssh -L 5432:woody:5432 -p 4660 bt220243@corton.iut.univ-lehavre.fr
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
            co.setAutoCommit(false);
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

        if ( elements != null )
            return this.connexion( elements[0], Integer.parseInt(elements[1]), elements[2], elements[3], elements[4] );
        else
            return false;
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
    /* Gestion Commits */
    /*-----------------*/

    public void enregistrer() throws SQLException { co.commit()  ; }
    public void annuler    () throws SQLException { co.rollback(); }

    /*-----------------*/
    /*     Module      */
    /*-----------------*/

    //Méthode d'insert
    public void ajouterModule(Module module)
    {
        String req = "INSERT INTO Module VALUES(?,?,?,?,?)";

        try ( PreparedStatement ps = co.prepareStatement(req) )
        {
            ps.setString  (1, module.getCode        ());
            ps.setInt  (2, module.getSemestre().getNumero());
            ps.setString  (3, module.getSemestre().getAnnee().getNom());
            ps.setString  (4, module.getNom         ());
            ps.setString  (5, module.getAbreviation ());
            ps.setString  (6, module.getTypeModule  ());
            ps.setBoolean (7, module.estValide      ());
            ps.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }

    //Méthode d'update
    public void majModule(Module module)
    {
        String req = "UPDATE Module SET nom = ?, abreviation = ?, typeModule = ?, validation = ? WHERE code = ? AND numeroSemestre = ? AND annee = ?";

        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString  (1, module.getNom         ());
            ps.setString  (2, module.getAbreviation ());
            ps.setString  (3, module.getTypeModule  ());
            ps.setBoolean (4, module.estValide      ());
            ps.setString  (5, module.getCode        ());
            ps.setInt  (6, module.getSemestre().getNumero());
            ps.setString  (7, module.getSemestre().getAnnee().getNom());
            ps.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }

    //Méthode delete
    public void supprimerModule(Module module)
    {
        String req = "DELETE FROM Module WHERE code = ? AND numeroSemestre = ? AND annee = ?";

        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1, module.getCode());
            ps.setInt  (2, module.getSemestre().getNumero());
            ps.setString  (3, module.getSemestre().getAnnee().getNom());
            ps.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }

    //Méthode select all
    public ArrayList<Module> getAllModule()
    {
        ArrayList<Module> ensModules = new ArrayList<>();
        String            req        = "SELECT * FROM Module";

        try(PreparedStatement ps = co.prepareStatement(req))
        {
            try(ResultSet rs = ps.executeQuery())
            {
                while(rs.next())
                {
                    Module mod = new Module(rs.getString("nom"           ),
                                            rs.getString  ("code"          ),
                                            rs.getString  ("abreviation"   ),
                                            rs.getString  ("typeModule"    ),
                                            rs.getBoolean ("validation"    ),
                            getSemestreById(rs.getInt     ("numeroSemestre"),rs.getString("annee")));
                    ensModules.add(mod);
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
        String req = "SELECT * FROM Module WHERE code = ?"; //TODO:Tout changer
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
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString   (1,inter.getNom                 ()  );
            ps.setString   (2,inter.getPrenom              ()  );
            ps.setString   (3, inter.getMail               ()  );
            ps.setString   (4,inter.getCategorie().getCode ()  );
            ps.setInt      (5,inter.getHeureService        ()  );
            ps.setInt      (6,inter.getHeureMax            ()  );
            ps.setString   (7,inter.getRatioTP             ()  );
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
            ps.setString(3,                 inter.getMail()           );
            ps.setString(4,                 inter.getCategorie().getCode() );
            ps.setString(5,Integer.toString(inter.getHeureService())          );
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
                        rs.getString(8));
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
                            rs.getString(8));
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

    public ArrayList<Intervenant> getIntervenantByNom(String nomRch)
    {
        ArrayList<Intervenant> resultats = new ArrayList<>();
        String req = "SELECT * FROM Intervenant WHERE lower(nom) like lower(?) or lower(prenom) like lower(?) or lower(codecategorie) like lower(?)";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1,'%' + nomRch + '%');
            ps.setString(2,'%' + nomRch + '%');
            ps.setString(3,'%' + nomRch + '%');
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
                            rs.getString(8));
                    resultats.add(inter);

                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
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
            ps.setString(2,semestre.getAnnee ().getNom() );
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
            ps.setString(1, annee.getNom());
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
            ps.setString(1, annee.getNom());
            ps.setString(2, annee.getDateDeb());
            ps.setString(3, annee.getDateFin());
            ps.setString(4, annee.getNom());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode delete
    public void supprimerAnnee(Annee annee) {
        String req = "DELETE FROM Annee WHERE numero = ?";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setString(1, annee.getNom());
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
        String req = "SELECT * FROM Annee WHERE nom = ?";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setString(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                // Traiter les résultats du ResultSet
                while (rs.next()) {
                    Annee annee = new Annee(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3)
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
        String req = "INSERT INTO CategorieIntervenant VALUES (?,?,?,?,?)";

        try(PreparedStatement ps = co.prepareStatement( req );)
        {

            ps.setString (1, categorieIntervenant.getCode       () );
            ps.setString (2, categorieIntervenant.getNom        () );
            ps.setInt    (3, categorieIntervenant.getNbHeureMaxDefault () );
            ps.setInt    (4, categorieIntervenant.getNbHeureServiceDefault    () );
            ps.setDouble (5, categorieIntervenant.getRatioTPDefault    () );
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
            ps.setInt    (3,       catInter.getNbHeureMaxDefault ());
            ps.setInt    (4,       catInter.getNbHeureServiceDefault   ());
            ps.setFloat  (5,(float)catInter.getRatioTPDefault    ());
            ps.setString (6,       catInter.getCode       ());
            ps.executeUpdate();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode de delete
    public boolean supprimerCatIntervenant(CategorieIntervenant catInter)
    {
        String req = "DELETE FROM CategorieIntervenant WHERE code = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1,catInter.getCode());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.printf("je suis la aussi ");
            e.printStackTrace();
            return false;
        }
        return true;
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
        try(PreparedStatement  ps = co.prepareStatement( req ))
        {

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
    public boolean supprimerCategorieHeure(CategorieHeure catHr)
    {
        String req = "DELETE FROM CategorieHeure WHERE nom = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1,catHr.getNom());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("je suis la mais dans categorie heure");
            e.printStackTrace();
            return false;
        }
        return true;
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
                            rs.getString  (1),
                            rs.getDouble  (2),
                            rs.getBoolean (3),
                            rs.getBoolean (4),
                            rs.getBoolean (5),
                            rs.getBoolean (6)
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
            ps.setInt   (1, attribution.getNbHeure              ());
            ps.setInt   (2, attribution.getNbSemaine            ());
            ps.setString(3, attribution.getModule().getCode     ());
            ps.setString(4, attribution.getCatHr ().getNom      ());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode d'update pour la classe Attribution
    public void majAttribution(Attribution att) {
        String req = "UPDATE Attribution SET codeModule = ?, nomCategorieHeure = ?, nbHeure = ?, nbSemaine = ? WHERE numeroSemestreModule = ? AND codeModule = ?, anneeModule = ?, nomCategorieHeure = ?";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setString(1,att.getModule   ().getCode    ());
            ps.setString(2,att.getCatHr    ().getNom     ());
            ps.setInt   (3,att.getNbHeure  ());
            ps.setInt   (4,att.getNbHeure  ());
            ps.setInt   (5,att.getNbSemaine()                                  );
            ps.setInt   (6,att.getModule   ().getSemestre().getNumero());
            ps.setString(7,att.getModule   ().getCode    ());
            ps.setString(8,att.getModule   ().getSemestre().getAnnee ().getNom());
            ps.setString(9,att.getCatHr    ().getNom     ());
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
            ps.setString (1,att.getModule().getCode    ());
            ps.setInt    (2,att.getModule().getSemestre().getNumero());
            ps.setString (3,att.getModule().getSemestre().getAnnee().getNom());
            ps.setString (4,att.getCatHr ().getNom     ());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode select * pour la classe Attribution
    public ArrayList<Attribution> getAllAttribution(ArrayList<Module> ensModule, ArrayList<CategorieHeure> ensCatHr) {
        ArrayList<Attribution> ensAttribution = new ArrayList<>();
        String req = "SELECT * FROM Attribution";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            try (ResultSet rs = ps.executeQuery()) {
                // Traiter les résultats du ResultSet
                while (rs.next())
                {
                    String codeModule        = rs.getString(1);
                    int numSem               = rs.getInt(2);
                    String anneeSem          = rs.getString(3);
                    String nomCategorieHeure = rs.getString(4);

                    Module mod = rechercherModule(ensModule, codeModule,numSem,anneeSem);
                    CategorieHeure catHr = rechercherCatHr(ensCatHr, nomCategorieHeure);

                    Attribution att = new Attribution(rs.getInt(5),rs.getInt(6),mod,catHr );
                    ensAttribution.add(att);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ensAttribution;
    }

    public Module rechercherModule(ArrayList<Module> ensModule, String codeModule,int numSem, String anneeSem)
    {
        for (Module mod : ensModule)
        {
            if(mod.getCode    ().equals(codeModule)    &&
               mod.getSemestre().getNumero() == numSem &&
               mod.getSemestre().getAnnee().getNom().equals(anneeSem))
            { return mod;}
        }
        return null;
    }
    public CategorieHeure rechercherCatHr(ArrayList<CategorieHeure> ensCategorie, String nomCatHr)
    {
        for (CategorieHeure catHrs : ensCategorie)
        {
            if(catHrs.getNom().equals(nomCatHr)){ return catHrs;}
        }
        return null;
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
            ps.setString   (1,affs.getModule     ().getCode    ());
            ps.setInt      (2,affs.getModule     ().getSemestre().getNumero());
            ps.setString   (3,affs.getModule     ().getSemestre().getAnnee().getNom());
            ps.setInt      (4,affs.getIntervenant().getId      ());
            ps.setString   (5,affs.getTypeHeure  ().getNom     ());
            ps.setInt      (6,affs.getNbGroupe   ());
            ps.setInt      (7,affs.getNbSemaine  ());
            ps.setInt      (8,affs.getNbHeure    ());
            ps.setString   (9,affs.getCommentaire());
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
        String req = "UPDATE Affectation SET codeModule = ?, numeroSemesreModule = ?, anneeModule = ?, idinter = ?, typeHeure = ?, nbGroupe = ?, nbSemaine = ?, nbHeure = ?, commentaire = ? WHERE codeModule = ? AND numeroSemestreModule = ? AND anneeModule = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            //SET
            ps.setString (1,aff.getModule().getCode());
            ps.setInt    (2,aff.getModule().getSemestre().getNumero());
            ps.setString (3,aff.getModule().getSemestre().getAnnee().getNom());
            ps.setInt    (4,aff.getIntervenant().getId());
            ps.setString (5,aff.getTypeHeure           ().getNom());
            ps.setInt    (6,aff.getNbGroupe            ());
            ps.setInt    (7,aff.getNbSemaine           ());
            ps.setInt    (8,aff.getNbHeure             ());
            ps.setString (9,aff.getCommentaire         ());

            // WHERE
            ps.setString (10,aff.getModule().getCode());
            ps.setInt    (11,aff.getModule().getSemestre().getNumero());
            ps.setString (12,aff.getModule().getSemestre().getAnnee().getNom());
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
            ps.setString (1,aff.getModule().getCode());
            ps.setInt    (2,aff.getModule().getSemestre().getNumero());
            ps.setString (3,aff.getModule().getSemestre().getAnnee().getNom());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Méthode select *
    public ArrayList<Affectation> getAllaff(ArrayList<Intervenant> ensInter, ArrayList<Module> ensModule, ArrayList<CategorieHeure> ensCatHr)
    {
        ArrayList<Affectation> ensaff = new ArrayList<>();
        String req = "SELECT * FROM Affectation";
        try (PreparedStatement ps = co.prepareStatement(req))
        {
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    int idInter = rs.getInt(4);

                    String codeModule        = rs.getString(1);
                    int numSem               = rs.getInt(2);
                    String anneeSem          = rs.getString(3);

                    String nomCategorieHeure = rs.getString(5);

                    Intervenant    inter = rechercherInter (ensInter,idInter);
                    Module         mod   = rechercherModule(ensModule,codeModule,numSem,anneeSem);
                    CategorieHeure catHr = rechercherCatHr(ensCatHr,nomCategorieHeure);

                    Affectation aff = new Affectation(mod)


                    ensaff.add(affectation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ensaff;
    }

    public Affectation getAffectationByModule(Module module)
    {
        String req = "SELECT * FROM Affectation WHERE codeModule = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1, module.getCode());
            try(ResultSet rs = ps.executeQuery())
            {
                Affectation affectation = new Affectation(
                        getModuleByNumero( rs.getString(1)),
                        rs.getInt   (2),
                        rs.getString(3),
                        getIntervenantById(rs.getInt   (4)),
                        getCatHrByNom     (rs.getString(5)),
                        rs.getInt   (6),
                        rs.getInt   (7),
                        rs.getInt   (8),
                        rs.getString(9));

                return affectation;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Module> getPrevisionsbySemestre(int semestre) {
        ArrayList<Module> ensaff = new ArrayList<>();
        String            req    = "SELECT * FROM module WHERE numerosemestre = ?";

        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setInt(1, semestre);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Module mod = new Module(
                            rs.getString("nom"),
                            rs.getString("code"),
                            rs.getString("abreviation"),
                            rs.getString("typeModule"),
                            rs.getBoolean("validation"),
                            getSemestreById(rs.getInt("numeroSemestre"), rs.getString("annee"))
                    );
                    ensaff.add(mod);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ensaff;
    }






}
