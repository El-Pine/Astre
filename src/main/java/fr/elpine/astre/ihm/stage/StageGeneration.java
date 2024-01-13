package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;


public class StageGeneration extends Stage implements Initializable
{
    private final ArrayList<Object> checkedObjects;

    private ObservableList<Object> ens;

    @FXML private TableView<Object>tabGeneration;
    @FXML private TableColumn<Object,String> g_1;
    @FXML private TableColumn<Object,Boolean>g_2;
    @FXML private TextField txtFieldRecherche;

    private String vue;


    public StageGeneration() // fxml -> "generation"
    {
        this.setTitle("Generation");
        this.setMinWidth(650);
        this.setMinHeight(700);

        this.checkedObjects = new ArrayList<>();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

        // Vérifier si le répertoire existe, sinon le créer
        File dossierSrc    = new File("Export"      );
        File dossierInter  = new File("Export/intervenant" );
        File dossierMod    = new File("Export/module"      );
        if (dossierSrc.exists()) {
            if (!dossierInter.exists() || !dossierMod.exists()) {
                if (!(dossierInter.mkdirs() && dossierMod.mkdirs()))
                    System.err.println("Impossible de créer le répertoire");
            }
        } else {
            if (!(dossierSrc.mkdirs() && dossierInter.mkdirs() && dossierMod.mkdirs()))
                System.err.println("Impossible de créer le répertoire");
        }
    }

    private void setCheckbox(boolean checked)
    {
        this.g_2.setCellValueFactory(cellData -> {
            Object modCheck = cellData.getValue();
            BooleanProperty stageProperty = new SimpleBooleanProperty(checked);

            stageProperty.addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    this.checkedObjects.add(modCheck);
                } else {
                    this.checkedObjects.remove(modCheck);
                }
            });
            return stageProperty;
        });

        g_2.setCellFactory(CheckBoxTableCell.forTableColumn(g_2));

        if(checked){
            this.checkedObjects.addAll(this.ens);
        }
        else
        {
            this.checkedObjects.clear();
        }
    }

    public void setVue(String vue) {
        if(vue.equals("intervenant"))
        {
            this.ens = FXCollections.observableArrayList(Controleur.get().getMetier().getIntervenants());
            this.vue = "intervenant";
        }
        else if(vue.equals("module"))
        {
            ArrayList<Module> lst = new ArrayList<>();

            Controleur.get().getMetier().getAnneeActuelle().getSemestres().forEach(s -> lst.addAll(s.getModules()));

            this.ens = FXCollections.observableArrayList(lst);
            this.vue = "module";
        }

        setCheckbox(false);

        if (this.vue.equals("intervenant"))
        {
            this.g_1.setCellValueFactory( cellData -> new SimpleStringProperty( String.format("%s %s", ((Intervenant)cellData.getValue()).getPrenom(), ((Intervenant)cellData.getValue()).getNom()) ) );
        }
        else if (this.vue.equals("module"))
        {
            this.g_1.setCellValueFactory( cellData -> new SimpleStringProperty( String.format("%s -%s", ((Module)cellData.getValue()).getCode(), ((Module)cellData.getValue()).getNom()) ) );
        }

        this.tabGeneration.setItems(this.ens);
        this.tabGeneration.setEditable(true);
    }

    public static void genrerIntervenant(Annee annee,Intervenant i)
    {
        HashMap<Semestre,HashMap<Module,HashMap<CategorieHeure,Double>>> semestres = new HashMap<>();

        for(Affectation a : i.getAffectations())
        {
            if (a.getModule().getSemestre().getAnnee() == annee) {
                semestres.computeIfAbsent(a.getModule().getSemestre(), k -> new HashMap<>());
                semestres.get(a.getModule().getSemestre()).computeIfAbsent(a.getModule(), k -> new HashMap<>());
                semestres.get(a.getModule().getSemestre()).get(a.getModule()).putIfAbsent(a.getTypeHeure(), 0.0);

                semestres.get(a.getModule().getSemestre()).get(a.getModule()).put(
                        a.getTypeHeure(),
                        semestres.get(a.getModule().getSemestre()).get(a.getModule()).get(a.getTypeHeure()) + a.getTotalEqtd(true)
                );
            }
        }

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html>\n");
        htmlContent.append("<html lang=\"en\">\n");
        htmlContent.append("<head>\n");
        htmlContent.append("    <meta charset=\"UTF-8\">\n");
        htmlContent.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        htmlContent.append("    <style>\n");
        htmlContent.append("        @import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@200;400;500;800&display=swap');\n");
        htmlContent.append("        * {\n");
        htmlContent.append("            margin: 0;\n");
        htmlContent.append("            padding: 0;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        body {\n");
        htmlContent.append("            margin: 25px;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        h1 {\n");
        htmlContent.append("            font-family: 'Montserrat', sans-serif;\n");
        htmlContent.append("            font-weight: 700;\n");
        htmlContent.append("            font-size: 3em;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        h2 {\n");
        htmlContent.append("            font-family: 'Montserrat', sans-serif;\n");
        htmlContent.append("            font-weight: 500;\n");
        htmlContent.append("            font-size: 2em;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        p {\n");
        htmlContent.append("            font-family: 'Montserrat', sans-serif;\n");
        htmlContent.append("            font-weight: 400;\n");
        htmlContent.append("            font-size: 1.5em;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        nav {\n");
        htmlContent.append("            display: flex;\n");
        htmlContent.append("            justify-content: space-between;\n");
        htmlContent.append("            width: 100%;\n");
        htmlContent.append("            margin-bottom: 5%;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        .box-semestres {\n");
        htmlContent.append("            display: grid;\n");
        htmlContent.append("            grid-template-columns: 1fr 1fr 1fr;\n");
        htmlContent.append("            grid-template-rows: 1fr 1fr 1fr;\n");
        htmlContent.append("            gap: 25px 25px;\n");
        htmlContent.append("            grid-template-areas: \". . .\" \". . .\";\n");
        htmlContent.append("        }\n");
        htmlContent.append("        .box-md {\n");
        htmlContent.append("            margin-top: 25px;\n");
        htmlContent.append("            border: 2px solid black;\n");
        htmlContent.append("            border-radius: 10px;\n");
        htmlContent.append("            width: 80%;\n");
        htmlContent.append("            padding: 15px;\n");
        htmlContent.append("        }\n");
        htmlContent.append("    </style>\n");
        htmlContent.append("    <title>").append(i.getPrenom()).append(" - ").append(i.getNom()).append(" </title>\n");
        htmlContent.append("</head>\n");
        htmlContent.append("<body>\n");
        htmlContent.append("    <nav>\n");
        htmlContent.append("        <div>\n");
        htmlContent.append("            <h1>").append(i.getPrenom()).append(" ").append(i.getNom()).append("</h1> <!--Nom prenom prof-->\n");
        htmlContent.append("            <h2>").append(i.getCategorie().getNom()).append("</h2> <!--Statut -->\n");
        htmlContent.append("        </div>\n");
        htmlContent.append("        <h1>").append(annee.getNom()).append("</h1> <!-- Années -->\n");
        htmlContent.append("    </nav>\n");
        // boucle semestres
        htmlContent.append("    <div class=\"box-semestres\"> <!--Boucle de semestres ici !!!!!!!!!!!!!!!!!!!!!!! -->\n");
        for (Semestre s : semestres.keySet())
        {
            htmlContent.append("        <div>\n");
            htmlContent.append("            <h2>Semestre ").append(s.getNumero()).append("</h2> <!--Numero semestre -->\n");
            // boucle categories heures
            for(Module m : semestres.get(s).keySet())
            {
                // nom du module
                htmlContent.append("            <div class=\"box-md\"> \n");
                htmlContent.append("               <p style=\"color:").append(m.getCouleurF()).append(";\">").append(m.getNom()).append("</p>");
                // boucle categories heures
                for(CategorieHeure ch : semestres.get(s).get(m).keySet())
                {
                    // nom de la categorie heure
                    htmlContent.append("                <p>").append(ch.getNom()).append(" : ").append(Fraction.simplifyDouble(semestres.get(s).get(m).get(ch), true)).append(" h</p>\n");
                }
                htmlContent.append("            </div>\n");
            }

            htmlContent.append("        </div>\n");

        }

        htmlContent.append("    </div> \n");
        htmlContent.append("    </div>\n");
        htmlContent.append("</body>\n");
        htmlContent.append("</html>");


        // Chemin vers le fichier HTML que vous souhaitez créer
        String filePath = "./Export/intervenant/"+i.getPrenom()+"-"+i.getNom()+".html";

        // Appel de la méthode pour créer le fichier HTML
        createHTMLFile(htmlContent.toString(), filePath);

        PopUp.information("Création fichier HTML", null , "Le fichier HTML a été crée avec succés");
    }

    public static void genererModules(Annee annee,Module mod){

        HashMap<Intervenant,HashMap<CategorieHeure,Double>> intervenants = new HashMap<>();

        // boucler toutes les affectations du module

        for( Affectation a : mod.getAffectations())
        {
            // si  la hashmap de la categorie heure est double dans intervenants existe pas pour l'intervenant de l'affectation alors on l'a créer
            if(!intervenants.containsKey(a.getIntervenant()))
            {
                intervenants.put(a.getIntervenant(), new HashMap<>());
            }
            // si la categorie heure de l'affectation n'existe pas dans la hashmap de l'intervenant alors on l'a créer
            if(!intervenants.get(a.getIntervenant()).containsKey(a.getTypeHeure()))
            {
                intervenants.get(a.getIntervenant()).put(a.getTypeHeure(),0.0);
            }


            Double d = intervenants.get(a.getIntervenant()).get(a.getTypeHeure());

            d += a.getTotalEqtd(true);

            intervenants.get(a.getIntervenant()).put(a.getTypeHeure(),d);

        }


        // Chaîne HTML que vous souhaitez écrire dans le fichier
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html>\n");
        htmlContent.append("<html lang=\"en\">\n");
        htmlContent.append("<head>\n");
        htmlContent.append("    <meta charset=\"UTF-8\">\n");
        htmlContent.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        htmlContent.append("    <style>\n");
        htmlContent.append("        @import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@200;400;500;800&display=swap');\n");
        htmlContent.append("        * {\n");
        htmlContent.append("            margin: 0;\n");
        htmlContent.append("            padding: 0;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        body {\n");
        htmlContent.append("            margin: 25px;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        h1 {\n");
        htmlContent.append("            font-family: 'Montserrat', sans-serif;\n");
        htmlContent.append("            font-weight: 700;\n");
        htmlContent.append("            font-size: 3em;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        h2 {\n");
        htmlContent.append("            font-family: 'Montserrat', sans-serif;\n");
        htmlContent.append("            font-weight: 500;\n");
        htmlContent.append("            font-size: 2em;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        p {\n");
        htmlContent.append("            font-family: 'Montserrat', sans-serif;\n");
        htmlContent.append("            font-weight: 400;\n");
        htmlContent.append("            font-size: 1.5em;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        nav {\n");
        htmlContent.append("            display: flex;\n");
        htmlContent.append("            justify-content: space-between;\n");
        htmlContent.append("            width: 100%;\n");
        htmlContent.append("            margin-bottom: 5%;\n");
        htmlContent.append("        }\n");
        htmlContent.append("        .box-semestres {\n");
        htmlContent.append("            display: grid;\n");
        htmlContent.append("            grid-template-columns: 1fr 1fr 1fr;\n");
        htmlContent.append("            grid-template-rows: 1fr 1fr 1fr;\n");
        htmlContent.append("            gap: 25px 25px;\n");
        htmlContent.append("            grid-template-areas: \". . .\" \". . .\";\n");
        htmlContent.append("        }\n");
        htmlContent.append("        .box-md {\n");
        htmlContent.append("            margin-top: 25px;\n");
        htmlContent.append("            border: 2px solid black;\n");
        htmlContent.append("            border-radius: 10px;\n");
        htmlContent.append("            width: 80%;\n");
        htmlContent.append("            padding: 15px;\n");
        htmlContent.append("        }\n");
        htmlContent.append("    </style>\n");
        htmlContent.append("    <title>").append(mod.getCode()).append(" - ").append(mod.getNom()).append("</title>\n");
        htmlContent.append("</head>\n");
        htmlContent.append("<body>\n");
        htmlContent.append("    <nav>\n");
        htmlContent.append("        <div>\n");
        htmlContent.append("            <h1>").append(mod.getCode()).append(" - ").append(mod.getNom()).append("</h1> \n");
        htmlContent.append("        </div>\n");
        htmlContent.append("        <h1>").append(annee.getNom()).append("</h1> <!-- Années -->\n");
        htmlContent.append("    </nav>\n");
        htmlContent.append("    <div class=\"box-semestres\"> <!--Boucle de semestres ici !!!!!!!!!!!!!!!!!!!!!!! -->\n");
        for(Intervenant i: intervenants.keySet())
        {
            htmlContent.append("        <div><!--Boucle profs iciiiiiiiiii !!!!!!!!!!!!!!!!!!!!! -->\n");
            htmlContent.append("            <div class=\"box-md\"> \n");
            htmlContent.append("                <p style=\"color:").append(mod.getCouleurF()).append(";\">").append(i.getNom()).append(" ").append(i.getPrenom()).append("</p> <!--On change la couleur ici-->\n");
            for(CategorieHeure ch : intervenants.get(i).keySet())
            {
                htmlContent.append("                <p>").append(ch.getNom()).append(" : ").append(Fraction.simplifyDouble(intervenants.get(i).get(ch), true)).append(" h</p>\n");
            }
            htmlContent.append("            </div>\n");
            htmlContent.append("        </div>\n");

        }

        htmlContent.append("    </div> \n");
        htmlContent.append("</body>\n");
        htmlContent.append("</html>");

        // Chemin vers le fichier HTML que vous souhaitez créer
        String filePath = "./Export/module/"+mod.getCode()+".html";

        // Appel de la méthode pour créer le fichier HTML
        createHTMLFile(htmlContent.toString(), filePath);

        PopUp.information("Création fichier HTML", null , "Le fichier HTML a été crée avec succés");
    }

    public static void createHTMLFile(String htmlContent, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            // Écriture de la chaîne HTML dans le fichier
            writer.flush();
            writer.write(htmlContent);
        } catch (IOException ignored) { }
    }

    @FXML private void onBtn() {
        if (this.vue.equals("module"))
        {
            this.checkedObjects.forEach(m -> genererModules(Controleur.get().getMetier().getAnneeActuelle(), (Module) m));
            try {
                File folder = new File("./Export/module");
                Desktop desktop = Desktop.getDesktop();
                desktop.open(folder);

            } catch (IOException ignored) { }

        }
        else if(this.vue.equals("intervenant"))
        {
            this.checkedObjects.forEach(i -> genrerIntervenant(Controleur.get().getMetier().getAnneeActuelle(), (Intervenant) i));
            try {
                File folder = new File("./Export/intervenant");
                Desktop desktop = Desktop.getDesktop();
                desktop.open(folder);

            } catch (IOException ignored) { }
        }
    }

    @FXML private void onCheckAll() {
        setCheckbox(true);
    }
    @FXML private void onUnCheckAll() {
        setCheckbox(false);
    }

    @FXML private void onAnnuler() {
        this.close();
    }

    @FXML private void onRecherche() {
        if (this.vue.equals("module"))
        {
            // creer une recherche de modules
            String recherche = txtFieldRecherche.getText();
            ArrayList<Module> ensMod = Controleur.get().getMetier().rechercheModuleByText(recherche);
            ObservableList<Object> ensResult =FXCollections.observableArrayList(ensMod);
            tabGeneration.setItems(ensResult);

        }
        else if(this.vue.equals("intervenant"))
        {
            String recherche = txtFieldRecherche.getText();
            ArrayList<Intervenant> ensInter = Controleur.get().getMetier().rechercheIntervenantByText(recherche);
            ObservableList<Object> ensResult =FXCollections.observableArrayList(ensInter);
            tabGeneration.setItems(ensResult);
        }
    }
}



