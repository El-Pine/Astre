package fr.elpine.astre.metier;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DB
{
    private static final Logger logger = LoggerFactory.getLogger(DB.class);

    private Connection co;

    public DB()
    {
        logger.info("Lancement de l'application");

        // Verification de l'existance du fichier de connexion
        File fichier = new File("infoBd.txt");
        try { fichier.createNewFile(); } catch (IOException e) { throw new RuntimeException(e); }

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
            co = DriverManager.getConnection(String.format("jdbc:postgresql://%s:%d/%s?user=%s&password=%s&sslmode=disable", ip, port, database, identifiant, password));

            try {
                if (!this.verify()) this.reset();
            }
            catch (Exception e) { logger.error("Erreur lors de la réinitialisation de la base de données", e); }

            // Mise en place de la partie métier à chaque connexion établie
            Controleur.get().startAstre();

            logger.info("Connexion à la base de données établie avec succès !");

            return true;
        }
        catch (Exception e)
        {
            logger.error("Erreur lors de la connexion à la base de données", e);
            return false;
        }
    }

    public boolean reloadDB()
    {
        String[] elements = DB.getInformations();

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
            catch (IOException e) { logger.error("Erreur lors du la sauvegarde des informations de connexion", e); }

        return valid;
    }

    public static String[] getInformations()
    {
        String[] elements = null;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("infoBd.txt"));

            String line = reader.readLine();
            if ( line != null ) elements = line.split("\t");
        }
        catch (IOException e) { logger.error("Erreur lors de la récupération des informations", e); }

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
                valid = false;
                logger.warn("Table {} introuvable !\n", table);
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
        catch (Exception e) { logger.error(String.format("Erreur lors du chargement du fichier SQL : %s", file), e); }

        return content.toString();
    }

    /*-----------------*/
    /*     Module      */
    /*-----------------*/

    //Méthode d'insert
    public void ajouterModule(Module module)
    {
        String req = "INSERT INTO Module VALUES(?,?,?,?,?,?,?,?)";

        try ( PreparedStatement ps = co.prepareStatement(req) )
        {
            Color c = module.getCouleur();
            String couleur = String.format("rgb(%d,%d,%d)", (int) (c.getRed()*255), (int) (c.getGreen()*255), (int) (c.getBlue()*255));

            ps.setString  (1, module.getCode        ());
            ps.setInt     (2, module.getSemestre().getNumero());
            ps.setString  (3, module.getSemestre().getAnnee().getNom());
            ps.setString  (4, module.getNom         ());
            ps.setString  (5, module.getAbreviation ());
            ps.setString  (6, module.getTypeModule  ());
            ps.setString  (7, couleur);
            ps.setBoolean (8, module.estValide      ());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de l'ajout d'un module", e); }
    }

    //Méthode d'update
    public void majModule(Module module)
    {
        String req = "UPDATE Module SET nom = ?, abreviation = ?, typeModule = ?, couleur = ?, validation = ? WHERE code = ? AND numeroSemestre = ? AND annee = ?";

        try(PreparedStatement ps = co.prepareStatement(req))
        {
            Color c = module.getCouleur();
            String couleur = String.format("rgb(%d,%d,%d)", (int) (c.getRed()*255), (int) (c.getGreen()*255), (int) (c.getBlue()*255));

            ps.setString  (1, module.getNom         ());
            ps.setString  (2, module.getAbreviation ());
            ps.setString  (3, module.getTypeModule  ());
            ps.setString  (4, couleur);
            ps.setBoolean (5, module.estValide      ());
            ps.setString  (6, module.getCode        ());
            ps.setInt     (7, module.getSemestre().getNumero());
            ps.setString  (8, module.getSemestre().getAnnee().getNom());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de la mise à jour d'un module", e); }
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
        catch (SQLException e) { logger.error("Erreur lors de la suppression d'un module", e); }
    }

    //Méthode select all
    public ArrayList<Module> getAllModule( ArrayList<Semestre> list)
    {
        ArrayList<Module> ensModule = new ArrayList<>();
        String            req        = "SELECT * FROM Module";

        try(PreparedStatement ps = co.prepareStatement(req))
        {
            try(ResultSet rs = ps.executeQuery())
            {
                while(rs.next())
                {
                    Semestre semestre = null;
                    for ( Semestre sem : list)
                    {
                        if ( sem.getNumero() == rs.getInt("numeroSemestre") && sem.getAnnee().getNom().equals(rs.getString("annee")) )
                            semestre = sem;
                    }

                    if (semestre != null) {
                        ensModule.add(new Module(rs.getString("nom"),
                                rs.getString("code"),
                                rs.getString("abreviation"),
                                rs.getString("typeModule"),
                                Color.valueOf(rs.getString("couleur")),
                                rs.getBoolean("validation"),
                                semestre));
                    }
                }
            }
        }
        catch(SQLException e) { logger.error("Erreur lors de la récupération des modules", e); }

        return ensModule;
    }
    
    public void majCodeModule(Module module)
    {
        String lastCode = module.getLastCode();

        try
        {
            // Ajout module
            this.ajouterModule( module );

            // Modification des affectations et des attributions liées au module

            // Affectations
            PreparedStatement ps = co.prepareStatement("UPDATE Affectation SET codeModule = ? WHERE codeModule = ? AND numeroSemestreModule = ? AND anneeModule = ?");

            ps.setString (1, module.getCode());
            ps.setString (2, lastCode);
            ps.setInt    (3, module.getSemestre().getNumero());
            ps.setString (4, module.getSemestre().getAnnee().getNom());
            ps.executeUpdate();

            // Attributions
            ps = co.prepareStatement("UPDATE Attribution SET codeModule = ? WHERE codeModule = ? AND numeroSemestreModule = ? AND anneeModule = ?");

            ps.setString (1, module.getCode());
            ps.setString (2, lastCode);
            ps.setInt    (3, module.getSemestre().getNumero());
            ps.setString (4, module.getSemestre().getAnnee().getNom());
            ps.executeUpdate();

            // Suppression
            ps = co.prepareStatement("DELETE FROM Module WHERE code = ? AND numeroSemestre = ? AND annee = ?");

            ps.setString(1, module.getLastCode());
            ps.setInt  (2, module.getSemestre().getNumero());
            ps.setString  (3, module.getSemestre().getAnnee().getNom());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors d'un changement de code de module'", e); }
    }


    /*-----------------*/
    /*   Intervenant   */
    /*-----------------*/

    //Méthode d'insert
    public void ajouterIntervenant(Intervenant inter)
    {
        String req = "INSERT INTO Intervenant (nom, prenom, mail, codeCategorie, heureService, heureMax, ratioTP) VALUES (?, ?, ?, ?,?, ?, ?)";
        try(PreparedStatement ps = co.prepareStatement(req, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setString   (1,inter.getNom                 ()  );
            ps.setString   (2,inter.getPrenom              ()  );
            ps.setString   (3, inter.getMail               ()  );
            ps.setString   (4,inter.getCategorie().getCode ()  );
            ps.setString   (5,inter.getHeureService().toString()  );
            ps.setString   (6,inter.getHeureMax().toString()  );
            ps.setString   (7,inter.getRatioTP().toString()  );

            if (ps.executeUpdate() > 0)
            {
                ResultSet generatedKeys = ps.getGeneratedKeys();

                if (generatedKeys.next()) inter.setId(generatedKeys.getInt("idInter"));
            }
        }
        catch (SQLException e) { logger.error("Erreur lors de l'ajout d'un intervenant", e); }
    }

    //Méthode d'update
    public void majIntervenant(Intervenant inter)
    {
        String req = "UPDATE Intervenant SET nom = ?, prenom = ?, mail = ?, codeCategorie = ?, heureService = ?, heureMax = ?, ratioTP = ? WHERE idInter = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1,                 inter.getNom    ()           );
            ps.setString(2,                 inter.getPrenom ()           );
            ps.setString(3,                 inter.getMail()           );
            ps.setString(4,                 inter.getCategorie().getCode() );
            ps.setString(5, inter.getHeureService().toString()          );
            ps.setString(6, inter.getHeureMax  ().toString()          );
            ps.setString(7, inter.getRatioTP().toString());
            ps.setInt(8,inter.getId());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de la mise à jour d'un intervenant", e); }
    }

    //Méthode delete
    public void supprimerIntervenant(Intervenant inter)
    {
        String req = "DELETE FROM Intervenant WHERE idInter = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setInt(1, inter.getId   () );
            ps.executeUpdate();
        }
        catch(SQLException e) { logger.error("Erreur lors de la suppression d'un intervenant", e); }
    }

    //Méthode select *
    public ArrayList<Intervenant> getAllIntervenant(ArrayList<CategorieIntervenant> ensCatInter)
    {
        ArrayList<Intervenant> resultats = new ArrayList<>();
        String req = "SELECT * FROM Intervenant";

        try (PreparedStatement ps = co.prepareStatement(req))
        {
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    String codeInter = rs.getString("codeCategorie");
                    CategorieIntervenant catInter =  Astre.rechercherCatInter(ensCatInter, codeInter);

                    Intervenant inter = new Intervenant(
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("mail"),
                            catInter,
                            Fraction.valueOf( rs.getString("heureService") ),
                            Fraction.valueOf( rs.getString("heureMax")     ),
                            Fraction.valueOf( rs.getString("ratioTP")      )
                    );

                    inter.setId(rs.getInt("idInter"));

                    resultats.add(inter);
                }
            }
        } catch (SQLException e) { logger.error("Erreur lors de la récupération des intervenants", e); }

        return resultats;
    }


    /*--------------*/
    /*   SEMESTRE   */
    /*--------------*/

    //Méthode insert
    public void ajouterSemestre(Semestre semestre)
    {
        String req = "INSERT INTO Semestre VALUES (?,?,?,?,?,?)";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setInt(1,semestre.getNumero   ());
            ps.setString(2,semestre.getAnnee().getNom());
            ps.setInt(3,semestre.getNbGrpTD  ());
            ps.setInt(4,semestre.getNbGrpTP  ());
            ps.setInt(5,semestre.getNbEtd    ());
            ps.setInt(6,semestre.getNbSemaine());
            ps.executeUpdate();
        } catch (SQLException e) { logger.error("Erreur lors de l'ajout d'un semestre", e); }
    }

    //Méthode d'update
    public void majSemestre(Semestre semestre)
    {
        String req = "UPDATE Semestre SET nbGrpTD = ?, nbGrpTP = ?, nbEtd = ?, nbSemaine = ? WHERE numero = ? AND annee = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setInt(2,semestre.getNbGrpTD  () );
            ps.setInt(3,semestre.getNbGrpTP  () );
            ps.setInt(4,semestre.getNbEtd    () );
            ps.setInt(5,semestre.getNbSemaine() );
            ps.setInt(1,semestre.getNumero   () );
            ps.setString(6,semestre.getAnnee().getNom() );
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de la mise à jour d'un semestre", e); }
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
        catch (SQLException e) { logger.error("Erreur lors de la suppression d'un semestre", e); }
    }

    //Méthode select *
    public ArrayList<Semestre> getAllSemestre( ArrayList<Annee> list)
    {
        ArrayList<Semestre> ensSemestre = new ArrayList<>();
        String req                      = "SELECT * FROM Semestre";
        try (PreparedStatement ps = co.prepareStatement(req))
        {
            try (ResultSet rs = ps.executeQuery())
            {
                // Traiter les résultats du ResultSet
                while (rs.next()) {

                    Annee annee = null;
                    for ( Annee anTemp : list)
                    {
                        if ( rs.getString("annee").equals(anTemp.getNom()) )
                            annee = anTemp;
                    }

                    if (annee != null) {
                        ensSemestre.add(new Semestre(
                                rs.getInt("numero"),
                                rs.getInt("nbGrpTD"),
                                rs.getInt("nbGrpTP"),
                                rs.getInt("nbEtd"),
                                rs.getInt("nbSemaine"),
                                annee));
                    }
                }
            }
        }
        catch (SQLException e) { logger.error("Erreur lors de la récupération des semestres", e); }

        return ensSemestre;
    }


    /*-----------*/
    /*   Annee   */
    /*-----------*/

    // Méthode insert
    public void ajouterAnnee(Annee annee) {
        String req = "INSERT INTO Annee VALUES (?,?,?)";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setString(1, annee.getNom());
            ps.setDate(2, annee.getDateDeb());
            ps.setDate(3, annee.getDateFin());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de l'ajout d'une année", e); }
    }

    // Méthode d'update
    public void majAnnee(Annee annee) {
        String req = "UPDATE Annee SET debut = ?, fin = ? WHERE nom = ?";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setDate(1, annee.getDateDeb());
            ps.setDate(2, annee.getDateFin());
            ps.setString(3, annee.getNom());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de la mise à jour d'une année", e); }
    }

    // Méthode delete
    public void supprimerAnnee(Annee annee) {
        String req = "DELETE FROM Annee WHERE nom = ?";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setString(1, annee.getNom());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de la suppression d'une année", e); }
    }

    public ArrayList<Annee> getAllAnnee() {
        ArrayList<Annee> ensAnnee = new ArrayList<>();
        String req = "SELECT * FROM Annee";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            try (ResultSet rs = ps.executeQuery()) {
                // Traiter les résultats du ResultSet
                while (rs.next()) {
                    Annee annee = new Annee(
                            rs.getString("nom"),
                            rs.getDate("debut"),
                            rs.getDate("fin")
                    );
                    ensAnnee.add(annee);
                }
            }
        }
        catch (SQLException e) { logger.error("Erreur lors de la récupération des années", e); }

        return ensAnnee;
    }


    /*-------------------------*/
    /*  Catégorie Intervenant  */
    /*-------------------------*/

    //Méthode d'insert
    public void ajouterCategorieIntervenant(CategorieIntervenant categorieIntervenant)
    {
        String req = "INSERT INTO CategorieIntervenant VALUES (?,?,?,?,?)";

        try(PreparedStatement ps = co.prepareStatement( req ))
        {

            ps.setString (1, categorieIntervenant.getCode       () );
            ps.setString (2, categorieIntervenant.getNom        () );
            ps.setString    (3, categorieIntervenant.getNbHeureMaxDefault ().toString() );
            ps.setString    (4, categorieIntervenant.getNbHeureServiceDefault    ().toString() );
            ps.setString (5, categorieIntervenant.getRatioTPDefault    ().toString() );
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de l'ajout d'une catégorie d'intervenant", e); }
    }

    //Méthode d'update
    public void majCategorieIntervenant(CategorieIntervenant catInter)
    {
        String req = "UPDATE CategorieIntervenant SET nom = ?, nbHeureMaxDefaut = ?, nbHeureServiceDefaut = ?, ratioTPDefaut = ? WHERE code = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString (1,       catInter.getNom        ());
            ps.setString    (2,       catInter.getNbHeureMaxDefault ().toString());
            ps.setString    (3,       catInter.getNbHeureServiceDefault   ().toString());
            ps.setString  (4,      catInter.getRatioTPDefault    ().toString());
            ps.setString (5,       catInter.getCode       ());
            ps.executeUpdate();

        }
        catch (SQLException e) { logger.error("Erreur lors de la mise à jour d'une catégorie d'intervenant", e); }
    }

    //Méthode de delete
    public void supprimerCategorieIntervenant(CategorieIntervenant catInter)
    {
        String req = "DELETE FROM CategorieIntervenant WHERE code = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1,catInter.getCode());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de la suppression d'une catégorie d'intervenant", e); }
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
                            rs.getString ("code"),
                            rs.getString ("nom"),
                            Fraction.valueOf( rs.getString("nbHeureMaxDefaut") ),
                            Fraction.valueOf( rs.getString("nbHeureServiceDefaut") ),
                            Fraction.valueOf( rs.getString("ratioTPDefaut") )
                    );
                    resultats.add(categorie);
                }
            }
        }
        catch (SQLException e) { logger.error("Erreur lors de la récupération des catégories d'intervenant", e); }

        return resultats;
    }


    /*-------------------*/
    /*  Categorie Heure  */
    /*-------------------*/

    //Méthode insert
    public void ajouterCategorieHeure(CategorieHeure categorieHeure)
    {
        String req = "INSERT INTO CategorieHeure VALUES (?,?,?,?,?,?,?,?)";
        try(PreparedStatement  ps = co.prepareStatement( req ))
        {

            ps.setString (1, categorieHeure.getNom          () );
            ps.setString (2, categorieHeure.getEquivalentTD ().toString() );
            ps.setBoolean(3, categorieHeure.estRessource    () );
            ps.setBoolean(4, categorieHeure.estSae          () );
            ps.setBoolean(5, categorieHeure.estPpp          () );
            ps.setBoolean(6, categorieHeure.estStage        () );
            ps.setBoolean(7, categorieHeure.estHebdo        () );
            ps.setString(8,  categorieHeure.getTypeGroupe   ());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de l'ajout d'une catégorie d'heure", e); }
    }

    //Méthode update
    public void majCategorieHeure(CategorieHeure catHr)
    {
        String req = "UPDATE CategorieHeure SET eqtd = ?,ressource = ?, sae = ?, ppp = ?,stage = ?, hebdo=?,typeGroupe=?  WHERE nom = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString  (1,catHr.getEquivalentTD().toString());
            ps.setBoolean (2,catHr.estRessource   ());
            ps.setBoolean (3,catHr.estSae         ());
            ps.setBoolean (4,catHr.estStage       ());
            ps.setBoolean (5,catHr.estPpp         ());
            ps.setBoolean (6,catHr.estHebdo       ());
            ps.setString  (7,catHr.getTypeGroupe  ());
            ps.setString  (8,catHr.getNom         ());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de la mise à jour d'une catégorie d'heure", e); }
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
        catch (SQLException e) { logger.error("Erreur lors de la suppression d'une catégorie d'heure", e); }
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
                    resultats.add(new CategorieHeure(
                            rs.getString  (1       ),
                            Fraction.valueOf( rs.getString(2) ),
                            rs.getBoolean (3 ),
                            rs.getBoolean (4 ),
                            rs.getBoolean (5 ),
                            rs.getBoolean (6 ),
                            rs.getBoolean (7 ),
                            rs.getString  (8)
                    ));
                }
            }
        }
        catch (SQLException e) { logger.error("Erreur lors de la récupération des catégories d'heure", e); }

        return resultats;
    }


    /*---------------------------*/
    /*        ASSOCIATION        */
    /*---------------------------*/

    // Méthode insert pour la classe Attribution
    public void ajouterAttribution(Attribution attribution) {
        String req = "INSERT INTO Attribution VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setString(1, attribution.getModule().getCode     ());
            ps.setInt   (2, attribution.getModule().getSemestre().getNumero());
            ps.setString(3, attribution.getModule().getSemestre().getAnnee().getNom());
            ps.setString(4, attribution.getCatHr().getNom());
            ps.setString(5, attribution.getNbHeure().toString());
            if (attribution.hasNbSemaine()) ps.setInt(6, attribution.getNbSemaine());
            else                            ps.setNull(6, Types.INTEGER);
            ps.setString(7, attribution.getNbHeurePN().toString());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de l'ajout d'une attribution", e); }
    }

    // Méthode d'update pour la classe Attribution
    public void majAttribution(Attribution att) {
        String req = "UPDATE Attribution SET nbHeure = ?, nbSemaine = ?, nbHeurePN = ? WHERE numeroSemestreModule = ? AND codeModule = ? AND anneeModule = ? AND nomCategorieHeure = ?";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setString   (1,att.getNbHeure  ().toString());
            if (att.hasNbSemaine()) ps.setInt   (2,att.getNbSemaine());
            else                    ps.setNull(2, Types.INTEGER);
            ps.setString   (3,att.getNbHeurePN().toString());

            ps.setInt   (4,att.getModule   ().getSemestre().getNumero());
            ps.setString(5,att.getModule   ().getCode    ());
            ps.setString(6,att.getModule   ().getSemestre().getAnnee ().getNom());
            ps.setString(7,att.getCatHr    ().getNom     ());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de la mise à jour d'une attribution", e); }
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
        }
        catch (SQLException e) { logger.error("Erreur lors de la suppression d'une attribution", e); }
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
                    String codeModule        = rs.getString("codeModule");
                    int numSem               = rs.getInt("numeroSemestreModule");
                    String anneeSem          = rs.getString("anneeModule");
                    String nomCategorieHeure = rs.getString("nomCategorieHeure");

                    Module mod           = Astre.rechercherModule(ensModule, codeModule,numSem,anneeSem);
                    CategorieHeure catHr = Astre.rechercherCatHr(ensCatHr, nomCategorieHeure);

                    int nbSemaine = rs.getInt("nbSemaine");

                    Fraction f   = Fraction.valueOf( rs.getString("nbHeure") );
                    Fraction fPN = Fraction.valueOf( rs.getString("nbHeurePN") );

                    if (rs.wasNull()) {
                        ensAttribution.add( new Attribution(
                                fPN,
                                f,
                                mod,
                                catHr
                        ));
                    } else {
                        assert catHr != null;
                        ensAttribution.add( new Attribution(
                                fPN,
                                f,
                                nbSemaine,
                                mod,
                                catHr
                        ));
                    }
                }
            }
        }
        catch (SQLException e) { logger.error("Erreur lors de la récupération des attributions", e); }

        return ensAttribution;
    }


    /*-----------------------*/
    /*  Affectation Modules  */
    /*-----------------------*/

    //Méthode insert
    public void ajouterAffectation(Affectation affs)
    {
        String req = "INSERT INTO Affectation (codeModule, numeroSemestreModule, anneeModule, idInter, typeHeure, nbGroupe, nbSemaine, nbHeure, commentaire) VALUES(?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement ps = co.prepareStatement(req, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setString   (1,affs.getModule     ().getCode    ());
            ps.setInt      (2,affs.getModule     ().getSemestre().getNumero());
            ps.setString   (3,affs.getModule     ().getSemestre().getAnnee().getNom());
            ps.setInt      (4,affs.getIntervenant().getId      ());

            ps.setString   (5,affs.getTypeHeure  ().getNom     ());

            if (affs.hasGrpAndNbSemaine()) {
                ps.setInt(6, affs.getNbGroupe());
                ps.setInt(7, affs.getNbSemaine());
            } else {
                ps.setNull(6, Types.INTEGER);
                ps.setNull(7, Types.INTEGER);
            }

            if (affs.hasNbHeure()) ps.setString(8, affs.getNbHeure().toString());
            else                   ps.setNull  (8, Types.FLOAT);

            ps.setString   (9,affs.getCommentaire());

            if (ps.executeUpdate() > 0)
            {
                ResultSet generatedKeys = ps.getGeneratedKeys();

                if (generatedKeys.next()) affs.setId(generatedKeys.getInt("idAffectation"));
            }

        }
        catch (SQLException e) { logger.error("Erreur lors de l'ajout d'une affectation", e); }
    }

    //Méthode d'update
    public void majAffectation(Affectation aff)
    {
        String req = "UPDATE Affectation SET typeHeure = ?, nbGroupe = ?, nbSemaine = ?, nbHeure = ?, commentaire = ? WHERE codeModule = ? AND numeroSemestreModule = ? AND anneeModule = ? AND idInter = ? AND idAffectation = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString (1,aff.getTypeHeure           ().getNom());

            if (aff.hasGrpAndNbSemaine()) {
                ps.setInt(2, aff.getNbGroupe());
                ps.setInt(3, aff.getNbSemaine());
            } else {
                ps.setNull(2, Types.INTEGER);
                ps.setNull(3, Types.INTEGER);
            }

            if (aff.hasNbHeure()) ps.setString(4, aff.getNbHeure().toString());
            else                   ps.setNull  (4, Types.FLOAT);

            ps.setString (5,aff.getCommentaire         ());

            ps.setString (6,aff.getModule().getCode());
            ps.setInt    (7,aff.getModule().getSemestre().getNumero());
            ps.setString (8,aff.getModule().getSemestre().getAnnee().getNom());
            ps.setInt    (9,aff.getIntervenant().getId());
            ps.setInt    (10,aff.getId());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de la mise à jour d'une affectation", e); }
    }

    //Méthode delete
    public void supprimerAffectation(Affectation aff)
    {
        String req = "DELETE FROM Affectation WHERE codeModule = ? AND numeroSemestreModule = ? AND anneeModule = ? AND idInter = ? AND idAffectation = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString (1,aff.getModule().getCode());
            ps.setInt    (2,aff.getModule().getSemestre().getNumero());
            ps.setString (3,aff.getModule().getSemestre().getAnnee().getNom());
            ps.setInt    (4,aff.getIntervenant().getId());
            ps.setInt    (5,aff.getId());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de la suppression d'une affectation", e); }
    }

    //Méthode select *
    public ArrayList<Affectation> getAllAffectation(ArrayList<Intervenant> ensInter, ArrayList<Module> ensModule, ArrayList<CategorieHeure> ensCatHr)
    {
        ArrayList<Affectation> ensaff = new ArrayList<>();
        String req = "SELECT * FROM Affectation";
        try (PreparedStatement ps = co.prepareStatement(req))
        {
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    String codeModule        = rs.getString("codeModule");
                    int numSem               = rs.getInt   ("numeroSemestreModule");
                    String anneeSem          = rs.getString("anneeModule");

                    Intervenant    inter = Astre.rechercherInter (ensInter,rs.getInt("idInter"));
                    Module         mod   = Astre.rechercherModule(ensModule,codeModule,numSem,anneeSem);
                    CategorieHeure catHr = Astre.rechercherCatHr(ensCatHr,rs.getString("typeHeure"));

                    Fraction nbHeure = Fraction.valueOf( rs.getString("nbHeure") );

                    if (rs.wasNull()) {
                        Affectation aff = new Affectation(
                                mod,
                                inter,
                                catHr,
                                rs.getInt("nbGroupe"),
                                rs.getInt("nbSemaine"),
                                rs.getString("commentaire")
                        );

                        aff.setId( rs.getInt("idAffectation") );

                        ensaff.add(aff);
                    } else {
                        Affectation aff = new Affectation(
                                mod,
                                inter,
                                catHr,
                                nbHeure,
                                rs.getString("commentaire")
                        );

                        aff.setId( rs.getInt("idAffectation") );

                        ensaff.add(aff);
                    }
                }
            }
        }
        catch (SQLException e) { logger.error("Erreur lors de la récupération des affectations", e); }

        return ensaff;
    }

    public boolean getStatus() {
	    try {
		    return this.co.isValid(0);
	    } catch (SQLException e) {
		    throw new RuntimeException(e);
	    }
    }
}