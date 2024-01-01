package fr.elpine.astre.metier;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.objet.Module;
import javafx.scene.paint.Color;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DB
{
    private static Logger logger = LoggerFactory.getLogger(DB.class);

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
            co = DriverManager.getConnection(String.format("jdbc:postgresql://%s:%d/%s", ip, port, database), identifiant, password);

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
    /* Gestion Commits */
    /*-----------------*/

    // TODO : à remplacer par les nouvelles méthodes dans le Astre

    public void enregistrer() throws SQLException { co.commit()  ; }
    public void annuler    () throws SQLException { co.rollback(); }

    /*-----------------*/
    /*     Module      */
    /*-----------------*/

    //Méthode d'insert
    public void ajouterModule(Module module)
    {
        String req = "INSERT INTO Module VALUES(?,?,?,?,?,?)";

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

                    ensModule.add(new Module(rs.getString("nom"           ),
                            rs.getString  ("code"          ),
                            rs.getString  ("abreviation"   ),
                            rs.getString  ("typeModule"    ),
                            Color.valueOf(rs.getString  ("couleur")),
                            rs.getBoolean ("validation"    ),
                            semestre));
                }
            }
        }
        catch(SQLException e) { logger.error("Erreur lors de la récupération des modules", e); }

        return ensModule;
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

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0)
            {
                ResultSet generatedKeys = ps.getGeneratedKeys();

                if (generatedKeys.next()) {
                    inter.setId(generatedKeys.getInt(1));
                }
            }
        }
        catch (SQLException e) { logger.error("Erreur lors de l'ajout d'un intervenant", e); }
    }

    //Méthode d'update
    public void majIntervenant(Intervenant inter)
    {
        String req = "UPDATE Intervenant SET nom = ?, prenom = ?, mail = ?, codeCategorie = ?, heureService = ?, heureTotal = ? WHERE id = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString(1,                 inter.getNom    ()           );
            ps.setString(2,                 inter.getPrenom ()           );
            ps.setString(3,                 inter.getMail()           );
            ps.setString(4,                 inter.getCategorie().getCode() );
            ps.setString(5,Integer.toString(inter.getHeureService())          );
            ps.setString(6,Integer.toString(inter.getHeureMax  ())          );
            ps.setInt(7,inter.getId());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de la mise à jour d'un intervenant", e); }
    }

    //Méthode delete
    public void supprimerIntervenant(Intervenant inter)
    {
        String req = "DELETE FROM Intervenant WHERE id = ?";
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
                    String codeInter = rs.getString(5);
                    CategorieIntervenant catInter =  Astre.rechercherCatInter(ensCatInter, codeInter);

                    Intervenant inter = new Intervenant(
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            catInter,
                            rs.getInt(6),
                            rs.getInt(7),
                            rs.getString(8)
                    );

                    inter.setId(rs.getInt(1));

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
        String req = "INSERT INTO Semestre VALUES (?,?,?,?,?)";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setInt(1,semestre.getNumero   ());
            ps.setInt(2,semestre.getNbGrpTD  ());
            ps.setInt(3,semestre.getNbGrpTP  ());
            ps.setInt(4,semestre.getNbEtd    ());
            ps.setInt(5,semestre.getNbSemaine());
            ps.executeUpdate();
        } catch (SQLException e) { logger.error("Erreur lors de l'ajout d'un semestre", e); }
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

                    ensSemestre.add(new Semestre(
                            rs.getInt                      ("numero"    ),
                            rs.getInt                      ("nbGrpTD"   ),
                            rs.getInt                      ("nbGrpTP"   ),
                            rs.getInt                      ("nbEtd"     ),
                            rs.getInt                      ("nbSemaine" ),
                            annee));
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
        String req = "UPDATE Annee SET nom = ?, debut = ?, fin = ? WHERE nom = ?";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setString(1, annee.getNom());
            ps.setDate(2, annee.getDateDeb());
            ps.setDate(3, annee.getDateFin());
            ps.setString(4, annee.getNom());
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
            ps.setInt    (3, categorieIntervenant.getNbHeureMaxDefault () );
            ps.setInt    (4, categorieIntervenant.getNbHeureServiceDefault    () );
            ps.setString (5, categorieIntervenant.getRatioTPDefault    () );
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de l'ajout d'une catégorie d'intervenant", e); }
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
            ps.setString  (5,      catInter.getRatioTPDefault    ());
            ps.setString (6,       catInter.getCode       ());
            ps.executeUpdate();

        }
        catch (SQLException e) { logger.error("Erreur lors de la mise à jour d'une catégorie d'intervenant", e); }
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
                            rs.getString (1),
                            rs.getString (2),
                            rs.getInt    (3),
                            rs.getInt    (4),
                            rs.getString (5)
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
        String req = "INSERT INTO CategorieHeure VALUES (?,?,?,?,?,?)";
        try(PreparedStatement  ps = co.prepareStatement( req ))
        {

            ps.setString (1, categorieHeure.getNom          () );
            ps.setString (2, categorieHeure.getEquivalentTD () );
            ps.setBoolean(3, categorieHeure.estRessource    () );
            ps.setBoolean(4, categorieHeure.estSae          () );
            ps.setBoolean(5, categorieHeure.estPpp          () );
            ps.setBoolean(6, categorieHeure.estStage        () );
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de l'ajout d'une catégorie d'heure", e); }
    }

    //Méthode update
    public void majCategorieHeure(CategorieHeure catHr)
    {
        String req = "UPDATE CategorieHeure SET nom = ?,eqtd = ?,ressource = ?, sae = ?, ppp = ?,stage = ? WHERE nom = ?";
        try(PreparedStatement ps = co.prepareStatement(req))
        {
            ps.setString  (1,catHr.getNom         ());
            ps.setString  (2,catHr.getEquivalentTD());
            ps.setBoolean (3,catHr.estRessource   ());
            ps.setBoolean (4,catHr.estSae         ());
            ps.setBoolean (5,catHr.estStage       ());
            ps.setBoolean (6,catHr.estPpp         ());
            ps.setString  (7,catHr.getNom         ());
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
                    CategorieHeure categorie = new CategorieHeure(
                            rs.getString  ("nom"       ),
                            rs.getString  ("eqtd"      ),
                            rs.getBoolean ("ressource" ),
                            rs.getBoolean ("sae"       ),
                            rs.getBoolean ("ppp"       ),
                            rs.getBoolean ("stage"     )
                    );
                    resultats.add(categorie);
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
        String req = "INSERT INTO Attribution VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setInt   (1, attribution.getNbHeure              ());
            ps.setInt   (2, attribution.getNbSemaine            ());
            ps.setString(3, attribution.getModule().getCode     ());
            ps.setString(4, attribution.getCatHr ().getNom      ());
            ps.executeUpdate();
        }
        catch (SQLException e) { logger.error("Erreur lors de l'ajout d'une attribution", e); }
    }

    // Méthode d'update pour la classe Attribution
    public void majAttribution(Attribution att) {
        String req = "UPDATE Attribution SET nbHeure = ?, nbSemaine = ? WHERE numeroSemestreModule = ? AND codeModule = ? AND anneeModule = ? AND nomCategorieHeure = ?";
        try (PreparedStatement ps = co.prepareStatement(req)) {
            ps.setInt   (1,att.getNbHeure  ());
            ps.setInt   (2,att.getNbSemaine());
            ps.setInt   (3,att.getModule   ().getSemestre().getNumero());
            ps.setString(4,att.getModule   ().getCode    ());
            ps.setString(5,att.getModule   ().getSemestre().getAnnee ().getNom());
            ps.setString(6,att.getCatHr    ().getNom     ());
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
                    String codeModule        = rs.getString(1);
                    int numSem               = rs.getInt(2);
                    String anneeSem          = rs.getString(3);
                    String nomCategorieHeure = rs.getString(4);

                    Module mod           = Astre.rechercherModule(ensModule, codeModule,numSem,anneeSem);
                    CategorieHeure catHr = Astre.rechercherCatHr(ensCatHr, nomCategorieHeure);

                    Attribution att = new Attribution(rs.getInt(5),rs.getInt(6),mod,catHr );
                    ensAttribution.add(att);
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
        catch (SQLException e) { logger.error("Erreur lors de l'ajout d'une affectation", e); }
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
        catch (SQLException e) { logger.error("Erreur lors de la mise à jour d'une affectation", e); }
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
        catch (SQLException e) { logger.error("Erreur lors de la suppression d'une affectation", e); }
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
                    int numSem               = rs.getInt   (2);
                    String anneeSem          = rs.getString(3);

                    String nomCategorieHeure = rs.getString(5);

                    Intervenant    inter = Astre.rechercherInter (ensInter,idInter);
                    Module         mod   = Astre.rechercherModule(ensModule,codeModule,numSem,anneeSem);
                    CategorieHeure catHr = Astre.rechercherCatHr(ensCatHr,nomCategorieHeure);

                    Affectation aff = new Affectation(mod, inter, catHr, rs.getInt(6),rs.getInt(7),rs.getString(9) );


                    ensaff.add(aff);
                }
            }
        }
        catch (SQLException e) { logger.error("Erreur lors de la récupération des affectations", e); }

        return ensaff;
    }
}