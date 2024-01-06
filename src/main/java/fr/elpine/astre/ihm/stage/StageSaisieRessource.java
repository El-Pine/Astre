package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import static java.lang.Integer.*;

//TODO:Faire les heures affectées (dans tableau Affectation nbHeuure * nbSemaine * eqtd)

public class StageSaisieRessource extends Stage implements Initializable
{
    @FXML
    public TableView<Affectation> tableau;
    @FXML
    private TableColumn<Intervenant,String> tc;
    @FXML
    public TableColumn<Affectation, String> tcIntervenant;
    @FXML
    public TableColumn<Affectation, String> tcType;
    public TableColumn<Affectation, Integer> tcSemaine;
    @FXML
    public TableColumn<Affectation, String> tcNbH;
    @FXML
    public TableColumn<Affectation, Integer> tcGrp;
    @FXML
    public TableColumn<Affectation, String> tcCommentaire;
    @FXML
    public TableColumn<Affectation, Integer> tcTotalEqtd;
    @FXML
    public ObservableList<Affectation> ensAff;
    @FXML
    public TextField txtTypeModule;
    @FXML
    public CheckBox cbValidation;
    @FXML
    public TextField txtnbGpTP;
    @FXML
    public TextField txtNbGpTD;
    @FXML
    public TextField txtNbEtd;
    @FXML
    public TextField txtLibelleLong;
    @FXML
    public TextField txtLibelleCourt;
    @FXML
    public TextField txtSemestre;

    public TextField txtTORepartion;
    public TextField txtTOPromo;
    public TextField txtTOAffecte;

    //private Stage stage;
    public ArrayList<Affectation> affAAjouter;
    public ArrayList<Affectation> affAEnlever;
    @FXML
    private TextField txtCode;
    private Module module;
    private int semestre;

    public GridPane gridPn;
    public GridPane gridPaneRepartition;
    public GridPane gridTot;

    public TabPane tabPaneSemaine;

    private HashMap<String, ArrayList<TextField>> hmTxtPn;
    private HashMap<String, ArrayList<TextField>> hmTxtSemaine;
    private HashMap<String, ArrayList<TextField>> hmTxtRepartion;


    public StageSaisieRessource() // fxml -> "saisieRessource"
    {
        this.setTitle("Affectation");
        this.setMinWidth(1550);
        this.setMinHeight(700);
    }


    //Méthode d'initialisation de la scène
    /*public static Stage creer(int semestre, Module mod) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(StageSaisieRessource.class.getResource("saisieRessource.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 700);

        Stage stage = new Stage();
        stage.setTitle("Affectation");
        stage.setScene(scene);

        AstreApplication.refreshIcon(stage);

        if(mod != null) StageSaisieRessource.module = mod;

        StageSaisieRessource stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) {
            stageCtrl.setStage(stage);
            stageCtrl.init(mod,"Ressource", semestre);
        }

        stage.setOnCloseRequest(e -> {
            try {
                StagePrevisionnel.creer().show();
            } catch (IOException ignored) {
            }
        });

        stage.show();

        return stage;
    }*/

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

        this.init("Ressource");
        initGridPn();
        for (CategorieHeure cat : Controleur.get().getMetier().getCategorieHeures())
        {
            if(cat.estRessource())
            {
                ajouterColonne(cat.getNom());
            }
        }
        ajouterColonne("TO");

        initTabPan();
        for (CategorieHeure cat : Controleur.get().getMetier().getCategorieHeures())
        {
            if(cat.estRessource())
            {
                ajouterOnglet(cat.getNom());
            }
        }

        this.gridPaneRepartition.getChildren().clear();
        for (CategorieHeure cat : Controleur.get().getMetier().getCategorieHeures())
        {
            if (cat.estRessource())
            {
                ajouterColonneRepartition(cat.getNom());
            }
        }


        this.hmTxtPn = new HashMap<String,ArrayList<TextField>>();
        this.module = new Module(txtLibelleLong.getText(), txtCode.getText(), txtLibelleCourt.getText(), txtTypeModule.getText(), Color.rgb(255,255,255), cbValidation.isSelected(), Controleur.get().getMetier().getSemestres().get(parseInt(txtSemestre.getText())));

        this.ensAff = FXCollections.observableArrayList(Controleur.get().getMetier().getAffectations());
        tcIntervenant.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIntervenant () .getNom()));
        tcType       .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeHeure   () .getNom()));
        tcSemaine    .setCellValueFactory(cellData -> {
            if (cellData.getValue().hasGrpAndNbSemaine())
	            return new SimpleIntegerProperty(cellData.getValue().getNbSemaine()).asObject();
            else
                return null;
        });
        tcGrp    .setCellValueFactory(cellData -> {
            if (cellData.getValue().hasGrpAndNbSemaine())
                return new SimpleIntegerProperty(cellData.getValue().getNbGroupe()).asObject();
            else
                return null;
        });
        tcNbH    .setCellValueFactory(cellData -> {
            if (cellData.getValue().hasNbHeure())
                return new SimpleStringProperty(cellData.getValue().getNbHeure    ().toString());
            else
                return null;
        });
        tcCommentaire.setCellValueFactory(cellData -> new SimpleStringProperty (cellData.getValue().getCommentaire()));


        //System.out.println(ensAff);

        tableau.setItems(ensAff);


        this.hmTxtPn = initHmPn(getAllTextFieldsPn(gridPn));

        this.hmTxtPn.forEach((key,value) -> {
            for (TextField txt: value)
            {
                if(txt.isEditable())
                {
                    ajouterListener(txt);
                    creerFormatter(key,txt);
                }
            }
        });

        this.hmTxtSemaine = initHmPn(extractTextFields(tabPaneSemaine));

        //System.out.println(hmTxtSemaine);

        this.hmTxtSemaine.forEach((key,value) -> {
            for (TextField txt: value)
            {
                ajouterListenerSemaine(txt);
                creerFormatter(key,txt);
            }
        });

        this.hmTxtRepartion = initHmPn(getAllTextFieldsPn(gridPaneRepartition));
        this.hmTxtRepartion.forEach((key,value) -> {
            for (TextField txt: value)
            {
                ajouterListenerTotal(txt);
                creerFormatter(key,txt);
            }
        });

        calculeAffecte();
    }

    private void calculeAffecte() {
        // Générer la liste de champs texte
        ArrayList<TextField> alAffecte = genererArrayList();

        // Calculer les valeurs en fonction des données du modèle
        HashMap<String, Double> hmAffecte = calculerValeursAffecte();

        // Mettre à jour les champs texte avec les valeurs calculées
        mettreAJourChamps(alAffecte, hmAffecte);
    }

    private HashMap<String, Double> calculerValeursAffecte() {
        HashMap<String, Double> hmAffecte = new HashMap<>();

        for (Affectation aff : tableau.getItems()) {
            String typeHeure = aff.getTypeHeure().getNom();
            double valeur;

            // TODO : pas bon, il faut aussi mettre les attributions
            if (aff.hasGrpAndNbSemaine())
                valeur = aff.getNbSemaine() * aff.getNbGroupe() * aff.getTypeHeure().getEquivalentTD().value();
            else
                valeur = aff.getNbHeure().value() * aff.getTypeHeure().getEquivalentTD().value();

            // Ajouter ou mettre à jour la valeur dans la HashMap
            hmAffecte.put(typeHeure, hmAffecte.getOrDefault(typeHeure, 0.0) + valeur);
        }

        return hmAffecte;
    }

    private void mettreAJourChamps(ArrayList<TextField> alAffecte, HashMap<String, Double> hmAffecte) {
        for (TextField txt : alAffecte) {
            String typeHeure = txt.getId().substring(3, 5);

            // Ajout de débogage
            System.out.println("Type d'heure extrait : " + typeHeure);

            if (hmAffecte.containsKey(typeHeure)) {
                // Ajout de débogage

                System.out.println("Valeur dans la HashMap : " + hmAffecte.get(typeHeure));
                System.out.println(String.valueOf(hmAffecte.get(typeHeure)));
                Integer valeur =  (int) Math.round(hmAffecte.get(typeHeure));
                System.out.println(valeur);
                txt.setText(valeur.toString());
            } else {
                txt.setText("Valeur non trouvée dans la HashMap");
            }
        }
    }

    private ArrayList<TextField> genererArrayList()
    {
        ArrayList<TextField> alAffecte = new ArrayList<>();
        for (Map.Entry<String, ArrayList<TextField>> entry : hmTxtRepartion.entrySet())
        {
            String key = entry.getKey();
            ArrayList<TextField> value = entry.getValue();

            for (TextField txt: value)
            {
                String jesaispascommentappelercettevariable = txt.getId().substring(5);
                if(jesaispascommentappelercettevariable.equals("Affecte"))
                    alAffecte.add(txt);
            }
        }

        return alAffecte;
    }


    /*-----------------------------*/
    /*   TextField dans tablePane  */
    /*-----------------------------*/

    public static ArrayList<TextField> extractTextFields(TabPane tabPane) {
        ArrayList<TextField> textFields = new ArrayList<>();
        tabPane.getTabs().forEach(tab -> extractTextFieldsFromTab(tab, textFields));
        return textFields;
    }

    private static void extractTextFieldsFromTab(Tab tab, ArrayList<TextField> textFields) {
        Node content = tab.getContent();
        if (content instanceof GridPane) {
            extractTextFieldsFromGridPane((GridPane) content, textFields);
        }
    }

    private static void extractTextFieldsFromGridPane(GridPane gridPane, ArrayList<TextField> textFields) {
        gridPane.getChildren().forEach(node -> {
            if (node instanceof FlowPane) {
                extractTextFieldsFromFlowPane((FlowPane) node, textFields);
            } else if (node instanceof TextField) {
                textFields.add((TextField) node);
            }
        });
    }

    private static void extractTextFieldsFromFlowPane(FlowPane flowPane, ArrayList<TextField> textFields) {
        flowPane.getChildren().forEach(node -> {
            if (node instanceof TextField) {
                textFields.add((TextField) node);
            }
        });
    }
    /*----------------------*/
    /*   TextField dans PN  */
    /*----------------------*/
    public static ArrayList<TextField> getAllTextFieldsPn(GridPane gridPane) {
        ArrayList<TextField> textFields = new ArrayList<>();

        for (Node node : gridPane.getChildren()) {
            if (node instanceof FlowPane flowPane) {
	            textFields.addAll(getTextFieldsFromFlowPane(flowPane));
            }
        }

        return textFields;
    }

    private static ArrayList<TextField> getTextFieldsFromFlowPane(FlowPane flowPane) {
        ArrayList<TextField> textFields = new ArrayList<>();

        for (Node node : flowPane.getChildren()) {
            if (node instanceof TextField) {
                textFields.add((TextField) node);
            }
        }
        return textFields;
    }

    /*-----------------------------*/
    /* Initialisation de la scène  */
    /*-----------------------------*/

    //private void setStage(Stage stage) { this.stage = stage; }

    private void init(String nomMod)
    {
        System.out.println("this.semestre : "+this.semestre);
        if (this.module != null) {
            // Initialisation pour la modification
            txtTypeModule.setText(this.module.getTypeModule());
            txtSemestre  .setText("" + this.semestre);
            txtSemestre  .setEditable(false);
            txtCode      .setText(this.module.getCode());

            initPn(this.module);

        } else {
            // Initialisation pour une nouvelle création
            txtTypeModule.setText(nomMod);
            txtTypeModule.setEditable(false);
            txtSemestre  .setText("" + this.semestre);
            txtSemestre  .setEditable(false);
        }

        Semestre sem = Controleur.get().getMetier().getAnneeActuelle().getSemestres().get(this.semestre); // .rechercheSemestreByNumero(this.semestre);

        txtCode.setText("R" + this.semestre + "." + String.format("%02d", sem.getModules().size() + 1));
        txtNbEtd.setText("" + sem.getNbEtd());
        txtNbGpTD.setText("" + sem.getNbGrpTD());
        txtnbGpTP.setText("" + sem.getNbGrpTP());
    }

    public void initPn(Module mod)
    {
        for (Attribution att : mod.getAttributions())
        {
            System.out.println("att.getNb"+att.getNbHeurePN().toString());
            this.hmTxtPn.get(att.getCatHr().getNom()).get(0).setText(att.getNbHeurePN().toString());
        }
    }

    @FXML
    protected void onBtnAjouter(ActionEvent e) throws IOException {
        //this.desactiver();
        //StageAjoutRessource.creer(StageSaisieRessource.module ,this ).show();
        StageAjoutRessource stage = Manager.creer("creationModules", this);

        stage.setModule(this.module);
        stage.showAndWait();
    }

    @FXML
    protected void onBtnSupprimer(ActionEvent e) throws IOException {
        Affectation affectation = tableau.getSelectionModel().getSelectedItem();

        if(affectation != null && PopUp.confirmationR("Suppression d'un module", null, "Êtes-vous sûr de supprimer ce module Ressource ?")) {
            tableau.getItems().remove(affectation);
            Controleur.get().getDb().supprimerAffectation(affectation);
            this.refresh();
        }
    }

    /*public void desactiver()
    {
        this.stage.getScene().lookup("#btnEnregistrer").setDisable(true);
        this.stage.getScene().lookup("#btnAnnuler").setDisable(true);
        this.stage.getScene().lookup("#btnAjouter").setDisable(true);
        this.stage.getScene().lookup("#btnSupprimer").setDisable(true);
    }

    public void activer() {
        this.stage.getScene().lookup("#btnEnregistrer").setDisable(false);
        this.stage.getScene().lookup("#btnAnnuler").setDisable(false);
        this.stage.getScene().lookup("#btnAjouter").setDisable(false);
        this.stage.getScene().lookup("#btnSupprimer").setDisable(false);
    }*/

    private void creerFormatter(String nom, TextField txtf) {
    txtf.setTextFormatter(new TextFormatter<>(change -> {
        if (change.getControlNewText().matches("^\\d+$") || change.getControlNewText().isEmpty()) {
            String newText = change.getControlNewText();

            if (isNumeric(newText)) {
                int newValue = Integer.parseInt(newText);

                List<TextField> repartitionList = this.hmTxtRepartion.get(nom);
                List<TextField> pnList = this.hmTxtPn.get(nom);

                if (repartitionList != null && pnList != null) {
                    int index = repartitionList.indexOf(txtf);

                    if (index != -1 && index < pnList.size()) {
                        int pnValue = Integer.parseInt(pnList.get(index).getText());

                        if (!repartitionList.contains(txtf) || newValue <= pnValue) {
                            txtf.setStyle("");
                        } else {
                            txtf.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
                        }
                    } else {
                        txtf.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
                    }
                } else {
                    // Handle the case when either repartitionList or pnList is null
                    txtf.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
                }

            } else {
                txtf.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
            }
            return change;
        } else {
            return null;
        }
    }));
}
    // Méthode utilitaire pour vérifier si une chaîne est un nombre
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    protected void onBtnEnregistrer() throws IOException, SQLException
    {
        Module mod;
        Fraction fractPn      = null;
        Fraction fractNbHeure = null;
        int nbSemaine         = 0;

        if(this.affAAjouter != null)
            for (Affectation aff : this.affAAjouter)
            {
                Controleur.get().getMetier().ajouterAffectation(aff);
            }

        mod = new Module(txtLibelleLong.getText(),txtCode.getText(),txtLibelleCourt.getText(),txtTypeModule.getText(), Color.BLACK, cbValidation.isSelected(), Controleur.get().getMetier().rechercheSemestreByNumero(Integer.parseInt(txtSemestre.getText())));
        for( CategorieHeure catHr : Controleur.get().getMetier().getCategorieHeures())
        {
            if(catHr.estRessource())
            {
                //Recuperation des paramètres du module
                fractPn      = Fraction.valueOf(getHeurePnByCatHr(catHr.getNom()));
                fractNbHeure = Fraction.valueOf(getNbHeureByCatHr(catHr.getNom(),false));
                nbSemaine    = Integer.parseInt(getNbHeureByCatHr(catHr.getNom(),true ));
            }

            Attribution att = new Attribution(fractPn, fractNbHeure,nbSemaine, mod, catHr);
            mod.ajouterAttribution(att);

            for (Affectation aff : tableau.getItems())
            {
                mod.ajouterAffectation(aff);
            }
        }
        Controleur.get().getMetier().enregistrer();
        this.close();
        Stage stage = Manager.creer("previsionnel", this);

        stage.showAndWait();
    }




    public String getNbHeureByCatHr(String nom,boolean nbSemaine)
    {
        for (Map.Entry<String,ArrayList<TextField>> entry : hmTxtSemaine.entrySet())
        {
            String key                 = entry.getKey  ();
            ArrayList<TextField> value = entry.getValue();

            System.out.println(key);
            System.out.println(value);

            if(key.equals(nom))
            {
                if(nbSemaine)
                {
                    return value.get(0).getText();
                }
                else
                {
                    return value.get(1).getText();
                }
            }
        }
        return "0";
    }


    public String getHeurePnByCatHr(String nom)
    {
        for (Map.Entry<String, ArrayList<TextField>> entry : hmTxtPn.entrySet())
        {
            String key                 = entry.getKey  ();
            ArrayList<TextField> value = entry.getValue();

            if(key.equals(nom))
            {
                return value.get(0).getText();
            }
        }
        return null;
    }


    @FXML
    protected void onBtnAnnuler() throws IOException {
        this.affAAjouter = this.affAEnlever = new ArrayList<>();
        this.close();
        //StagePrincipal.creer().show();
    }



    private void ajouterOnglet(String nom) {
        // Créer un GridPane pour l'onglet
        GridPane grid = new GridPane();

        // Ajouter des colonnes au GridPane
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        grid.getColumnConstraints().addAll(column1, column2);

        // Collection pour stocker les FlowPane
        FlowPane[] flowPanes = new FlowPane[4];

        // Créer et configurer les FlowPane dans une boucle
        for (int i = 0; i < flowPanes.length; i++) {
            FlowPane fp = new FlowPane();
            fp.setAlignment(Pos.CENTER);
            flowPanes[i] = fp;
        }

        // Ajouter du contenu aux FlowPane
        flowPanes[0].getChildren().add(new Label("Nombre Semaine"));

        TextField txtNbSemaine = creerTextField("txt" + nom + "NbSemaine");
        flowPanes[1].getChildren().add(txtNbSemaine);

        flowPanes[2].getChildren().add(new Label("Nombre heure / semaine"));

        TextField txtNbHrSem = creerTextField("txt" + nom + "NbHrSem");
        flowPanes[3].getChildren().add(txtNbHrSem);

        // Ajouter les FlowPane au GridPane
        grid.add(flowPanes[0], 0, 0);
        grid.add(flowPanes[1], 0, 1);
        grid.add(flowPanes[2], 1, 0);
        grid.add(flowPanes[3], 1, 1);

        // Créer un onglet
        Tab tab = new Tab(nom);
        tab.setContent(grid);

        // Ajouter l'onglet au TabPane
        this.tabPaneSemaine.getTabs().add(tab);
    }

    private TextField creerTextField(String id) {
        TextField textField = new TextField();
        textField.setId(id);
        textField.setPrefSize(50, 26);
        return textField;
    }

    private void ajouterColonne(String nom)
    {
        ArrayList<FlowPane> ensFp = new ArrayList<>();
        for (int i = 0; i <= 2; i++)
        {
            FlowPane fp = new FlowPane();
            fp.setAlignment(Pos.CENTER);

            ensFp.add(fp);
        }
        Label lbl = new Label(nom);
        ensFp.get(0).getChildren().add(lbl);

        TextField txt1 = new TextField();
        TextField txt2 = new TextField();

        if(this.module != null &&  nom != "TO")
        {
            CategorieHeure catHr = Astre.rechercherCatHr(Controleur.get().getMetier().getCategorieHeures(), nom);
            System.out.println("Nom CatHr :" +catHr.getNom());
            System.out.println("StageSaisieRessource.module.getAttribution(catHr).getNbHeurePN().toString() : " +this.module.getAttribution(catHr).getNbHeurePN().toString());


            //txt1.setText(StageSaisieRessource.module.getAttribution(catHr).getNbHeurePN().toString());
        }

        txt1.setId("txt"+nom+"Pn");

        if(nom.equals("TO"))
            txt1.setEditable(false);

        txt2.setId("txt"+nom+"PromoPn");
        txt2.setEditable(false);

        txt1.setPrefSize(50,26);
        txt2.setPrefSize(50,26);

        ensFp.get(1).getChildren().add(txt1);
        ensFp.get(2).getChildren().add(txt2);

        gridPn.getColumnConstraints().add(new ColumnConstraints());

        int cpt = 0;
        for (FlowPane fp : ensFp)
        {
            gridPn.add(fp,gridPn.getColumnConstraints().size() - 1, cpt++ );
        }
    }

    private void ajouterColonneRepartition(String nom) {
        ArrayList<FlowPane> ensFp = new ArrayList<>();

        for (int i = 0; i <= 3; i++) {
            FlowPane fp = creerFlowPane();
            ensFp.add(fp);
        }

        Label lbl = new Label(nom);
        ensFp.get(0).getChildren().add(lbl);

        String[] ids = { "Repartition", "Promo", "Affecte" };
        for (int i = 0; i < ids.length; i++) {
            TextField txt = creerTextField("txt" + nom + ids[i]);
            txt.setEditable(false);
            ensFp.get(i + 1).getChildren().add(txt);
        }

        gridPaneRepartition.getColumnConstraints().add(new ColumnConstraints());

        int cpt = 0;
        for (FlowPane fp : ensFp) {
            gridPaneRepartition.add(fp, gridPaneRepartition.getColumnConstraints().size() - 1, cpt++);
        }
    }

    private FlowPane creerFlowPane() {
        FlowPane fp = new FlowPane();
        fp.setAlignment(Pos.CENTER);
        return fp;
    }


    private void initTabPan()
    {
        this.tabPaneSemaine.getTabs().clear();
    }

    private void initGridPn()
    {
        this.gridPn.getChildren().clear();

        FlowPane fp = new FlowPane();
        fp.setAlignment(Pos.CENTER);

        Label lbl = new Label("Total promo(eqtd)");
        fp.getChildren().add(lbl);

        ColumnConstraints col = new ColumnConstraints();
        gridPn.getColumnConstraints().add(col);

        gridPn.add(fp, 0, 2);
    }



    /*-----------------------------*/
    /*    Ajout des Listenner      */
    /*-----------------------------*/

    private void ajouterListener(TextField txt)
    {
        txt.textProperty().addListener((observable, oldValue, newValue) -> valeurChangerPn(txt, newValue));
    }

    private void ajouterListenerSemaine(TextField txt)
    {
        txt.textProperty().addListener((observable, oldValue, newValue) -> valeurChangerSemaine(txt, newValue));
    }

    private void ajouterListenerTotal(TextField txt)
    {
        txt.textProperty().addListener((observable, oldValue, newValue) -> valeurChangerRepart(txt, newValue));
    }

    private void valeurChangerRepart(TextField txt, String newValue)
    {
        String txtTypeHeure = txt.getId().substring(3, 5);
        String txtNiveauCase = txt.getId().substring(6);

        int total = 0;
        int totalPromo = 0;
        int totalAffecte = 0;

        // Liste pour simplifier l'itération
        List<String> niveaux = Arrays.asList("Repartition", "Promo", "Affecte");

        for (Map.Entry<String, ArrayList<TextField>> entry : hmTxtRepartion.entrySet()) {
            String key = entry.getKey();
            ArrayList<TextField> value = entry.getValue();

            if (!key.equals("TO")) {
                for (TextField textField : value) {
                    String niveau = textField.getId().substring(5);
                    if (!textField.getText().isEmpty()) {
                        switch (niveau)
                        {
                            case "Repartition":
                                total += Integer.parseInt(textField.getText());
                                break;
                            case "Promo":
                                totalPromo += Integer.parseInt(textField.getText());
                                break;
                            case "Affecte":
                                totalAffecte += Integer.parseInt(textField.getText());
                                break;
                        }
                    }
                }
            }
            else
            {

            }
        }
        setValeurTxtTot2(this.txtTORepartion, total, this.txtTOPromo, totalPromo, this.txtTOAffecte, totalAffecte);
    }
    private void setValeurTxtTot2(TextField txt1, int total, TextField txt2, int totalPromo, TextField txt3, int totalAffecte) {
        if (txt1 != null && txt2 != null && txt3 != null)
        {
            txt1.setText(Integer.toString(total));
            txt2.setText(Integer.toString(totalPromo));
            txt3.setText(Integer.toString(totalAffecte));
        }
    }

    private void valeurChangerSemaine(TextField txt, String newValue)
    {
        String keyCatHr = txt.getId().substring(3,5);
        String keyCatHrMAJ = txt.getId().substring(3,5).toUpperCase();

        CategorieHeure catHr = Astre.rechercherCatHr(Controleur.get().getMetier().getCategorieHeures(), keyCatHrMAJ);

        int valeurSemaine = 0;
        int valeurFinal   = 0;

        System.out.println(!(this.hmTxtSemaine.get(keyCatHrMAJ).get(0).getText().isEmpty()) && !(this.hmTxtSemaine.get(keyCatHrMAJ).get(1).getText().isEmpty()));
        if(!(this.hmTxtSemaine.get(keyCatHrMAJ).get(0).getText().isEmpty()) && !(this.hmTxtSemaine.get(keyCatHrMAJ).get(1).getText().isEmpty()) )
        {
            valeurSemaine = Integer.parseInt(this.hmTxtSemaine.get(keyCatHrMAJ).get(0).getText()) * Integer.parseInt(this.hmTxtSemaine.get(keyCatHrMAJ).get(1).getText());
        }
        for (TextField txt1 : this.hmTxtRepartion.get(keyCatHrMAJ))
        {
            if(txt1.getId().equals("txt" + keyCatHr +"Repartition"))
            {
                txt1.setText(Integer.toString(valeurSemaine));
            }

            if(txt1.getId().equals("txt" + keyCatHr + "Promo"))
            {
                if(catHr.getNom().equals("TP") || catHr.getNom().equals("TD"))
                {
                    int valeurXgroupe = valeurSemaine * (catHr.getNom().equals("TP") ? Integer.parseInt(this.txtnbGpTP.getText()) : Integer.parseInt(this.txtNbGpTD.getText()));
                    valeurFinal   = (int) (valeurXgroupe * catHr.getEquivalentTD().value());
                }
                else
                {
                    valeurFinal = (int) (valeurSemaine * catHr.getEquivalentTD().value());
                }

                txt1.setText(Integer.toString(valeurFinal));
            }
        }

    }

    private void valeurChangerPn(TextField txt, String newValue)
    {
        String keyCatHr = txt.getId().substring(3,5).toUpperCase();
        for (TextField txt1 : this.hmTxtPn.get(keyCatHr))
        {
            if(!txt1.isEditable())
            {
                if(!txt.getText().isEmpty())
                {
                    int valeurInitial = Integer.parseInt(txt.getText());
                    txt1.setText(calculeNvValeur(valeurInitial, Astre.rechercherCatHr(Controleur.get().getMetier().getCategorieHeures(), keyCatHr)));
                }
            }
        }
        calculeTotaux();
    }

    private void calculeTotaux() {
        int total = 0;
        int totalPromo = 0;
        TextField txtTotPn = null;
        TextField txtTotPromoPn = null;

        for (Map.Entry<String, ArrayList<TextField>> entry : hmTxtPn.entrySet())
        {
            String key = entry.getKey();
            ArrayList<TextField> value = entry.getValue();

            if (!key.equals("TO")) {
                for (TextField txt : value) {
                    if (!txt.getText().isEmpty()) {
                        if (txt.isEditable()) {
                            total += Integer.parseInt(txt.getText());
                        } else {
                            totalPromo += Integer.parseInt(txt.getText());
                        }
                    }
                }
            } else {
                for (TextField txt : value) {
                    if (txt.getId().equals("txtTOPn"))
                        txtTotPn = txt;
                    else
                        txtTotPromoPn = txt;
                }
            }
        }
        setValeurTxtTot(txtTotPn, total, txtTotPromoPn, totalPromo);
    }

    private void setValeurTxtTot(TextField txt1, int total, TextField txt2, int totalPromo)
    {
        if (txt1 != null && txt2 != null)
        {
            txt1.setText(Integer.toString(total));
            txt2.setText(Integer.toString(totalPromo));
        }
    }
    private String calculeNvValeur(int valeurInitial, CategorieHeure catHr)
    {
        //TODO: Ajouter le regex de mathys
       if(catHr.getNom().equals("TP") || catHr.getNom().equals("TD"))
       {
           int valeurXgroupe = valeurInitial * (catHr.getNom().equals("TP") ? Integer.parseInt(this.txtnbGpTP.getText()) : Integer.parseInt(this.txtNbGpTD.getText()));
           int valeurFinal   = (int) (valeurXgroupe * catHr.getEquivalentTD().value());
           return Integer.toString(valeurFinal);
       }
       else
       {
           System.out.println();
           int valeurFinal = (int) (valeurInitial * catHr.getEquivalentTD().value());
           return Integer.toString(valeurFinal);
       }
    }

    private HashMap<String, ArrayList<TextField>> initHmPn(ArrayList<TextField> ensTxt)
    {
        HashMap<String, ArrayList<TextField>> hmTemp = new HashMap<>();
        for (TextField txt: ensTxt)
        {
            String key = txt.getId().substring(3,5).toUpperCase();
            if(hmTemp.containsKey(key))
            {
                hmTemp.get(key).add(txt);
            }
            else
            {
                hmTemp.put(key,new ArrayList<>());
                hmTemp.get(key).add(txt);
            }
        }
        return hmTemp;
    }

    /*
    private String getCellValue(Intervenant intervenant) {
        if (StageIntervenant.interAAjouter.contains(intervenant)) {
            return "➕";
        } else if (StageIntervenant.interAEnlever.contains(intervenant)) {
            return "❌";
        } else {
            return "";
        }
    }
    */


    public void refresh() {
        ensAff = FXCollections.observableArrayList(Controleur.get().getMetier().getAffectations());
        tableau.setItems(ensAff);
    }

    public void setSemestre(int semestre)
    {
        this.semestre = semestre;
        init(this.module.getNom());
    }

    public void setModule(Module mod) {
        this.module = mod;
    }
}