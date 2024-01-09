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
    public ColorPicker couleur;
    @FXML
    private TableColumn<Affectation,String> tc;
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
    public static ObservableList<Affectation> ensAff;
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

    private Module moduleModif;

    private Module futurModule;
    private int semestre;
    private String typeModule;

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

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.setWidth ( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

        if(this.typeModule != null)
        {
            if (this.typeModule.equals("Ressource"))
                initializeRessource();
            else
                initializeStage();
        }
        this.setOnCloseRequest(e ->
        {
            //this.futurModule.supprimer(false,true);
            Controleur.get().getMetier().rollback();
        });
    }

    public void initializeStage()
    {
        initGridPn();
        initTabPan();
        initRepartitionColumns();
        initTableColumns();
        setupListenersAndFormatters();
        setupTableAndBindings();

        initializeAffectations();
        calculateAffecte();

    }

    public void initializeRessource()
    {
        initGridPn();
        initTabPan();
        initRepartitionColumns();
        initTableColumns();
        setupListenersAndFormatters();
        setupTableAndBindings();

        initializeAffectations();
        calculateAffecte();
    }

    private void initTableColumns() {
        for (CategorieHeure cat : Controleur.get().getMetier().getCategorieHeures())
        {
            boolean typeModule = this.typeModule.equals("Ressource") ? cat.estRessource() : cat.estStage();
            if (typeModule)
            {
                ajouterColonne(cat.getNom());
            }
        }
        ajouterColonne("TO");

        tc.setCellValueFactory(cellData -> new SimpleStringProperty(getCellValue(cellData.getValue())));
        tc.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);

                if (item != null && item.equals("❌")) {
                    setTextFill(Color.RED);
                } else if (item != null && item.equals("➕")) {
                    setTextFill(Color.LIGHTGREEN);
                } else {
                    setTextFill(Color.BLACK);
                    setText("");
                }
            }
        });

        tcIntervenant.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIntervenant().getNom()));
        tcType       .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeHeure  ().getNom()));
        tcSemaine    .setCellValueFactory(cellData -> cellData.getValue().hasGrpAndNbSemaine() ? new SimpleIntegerProperty(cellData.getValue().getNbSemaine()).asObject()  : null);
        tcGrp        .setCellValueFactory(cellData -> cellData.getValue().hasGrpAndNbSemaine() ? new SimpleIntegerProperty(cellData.getValue().getNbGroupe ()).asObject()  : null);
        tcNbH        .setCellValueFactory(cellData -> cellData.getValue().hasNbHeure        () ? new SimpleStringProperty (cellData.getValue().getNbHeure  () .toString()) : null);
        tcCommentaire.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCommentaire()));
    }

    private void initRepartitionColumns() {
        this.gridPaneRepartition.getChildren().clear();
        for (CategorieHeure cat : Controleur.get().getMetier().getCategorieHeures())
        {
            boolean typeModule = this.typeModule.equals("Ressource") ? cat.estRessource() : cat.estStage();
            if (typeModule) {
                System.out.println(cat.getNom());
                ajouterColonneRepartition(cat.getNom());
            }
        }
        //ajouterColonneRepartition("HP");
    }

    private void initializeAffectations()
    {
        if(this.moduleModif != null)
            StageSaisieRessource.ensAff = FXCollections.observableArrayList(this.moduleModif.getAffectations());
        else
            StageSaisieRessource.ensAff = FXCollections.observableArrayList(new ArrayList<>());
    }

    private void setupTableAndBindings() {
        tableau.setItems(ensAff);
    }

    private void setupListenersAndFormatters() {
        this.hmTxtPn = initHmPn(getAllTextFieldsPn(gridPn));
        activationTextField(this.hmTxtPn,"Pn");


        this.hmTxtSemaine = initHmPn(extractTextFields(tabPaneSemaine));
        activationTextField(this.hmTxtSemaine,"Semaine");

        this.hmTxtRepartion = initHmPn(getAllTextFieldsPn(gridPaneRepartition));
        activationTextField(this.hmTxtRepartion,"R");
    }

    private void activationTextField(HashMap<String, ArrayList<TextField>> hm,String prout)
    {
        hm.forEach((key,value) -> {
            for (TextField txt: value)
            {
                if(txt.isEditable() || prout.equals("R"))
                {
                    switch (prout)
                    {
                        case "Pn"          -> ajouterListener(txt);
                        case "Semaine"     -> ajouterListenerSemaine(txt);
                        case "R"           -> ajouterListenerTotal(txt);
                    }
                    creerFormatter(key,txt);
                }
            }
        });
    }

    private void calculateAffecte() {
        calculeAffecte();
    }
    private void calculeAffecte() {
        // Générer la liste de champs texte
        ArrayList<TextField> alAffecte = genererArrayListAffecte();

        // Calculer les valeurs en fonction des données du modèle
        HashMap<String, Double> hmAffecte = calculerValeursAffecte();

        // Mettre à jour les champs texte avec les valeurs calculées
        mettreAJourChamps(alAffecte, hmAffecte);
    }

    private HashMap<String, Double> calculerValeursAffecte()
    {
        System.out.println("jusque ici tous va bien ");
        HashMap<String, Double> hmAffecte = new HashMap<>();

        if(tableau.getItems() != null)
        {
            for (Affectation aff : tableau.getItems()) {
                String typeHeure = aff.getTypeHeure().getNom().toUpperCase();
                double valeur;
                if (aff.hasGrpAndNbSemaine())
                    valeur = aff.getNbSemaine() * aff.getNbGroupe() * aff.getTypeHeure().getEquivalentTD().value();
                else
                    valeur = aff.getNbHeure().value() * aff.getTypeHeure().getEquivalentTD().value();

                // Ajouter ou mettre à jour la valeur dans la HashMap
                hmAffecte.put(typeHeure, hmAffecte.getOrDefault(typeHeure, 0.0) + valeur);
            }
        }

        System.out.println("aaa " + hmAffecte);
        return hmAffecte;
    }

    private void mettreAJourChamps(ArrayList<TextField> alAffecte, HashMap<String, Double> hmAffecte)
    {
        System.out.println("bbb : " + alAffecte);
        for (TextField txt : alAffecte)
        {
            String typeHeure = "";
            for (CategorieHeure catHr: Controleur.get().getMetier().getCategorieHeures())
            {
                if(txt.getId().contains(catHr.getNom()))
                {
                    typeHeure = catHr.getNom().toUpperCase();
                }
            }

            if (hmAffecte.containsKey(typeHeure)) {
                // Ajout de débogage
                Integer valeur =  (int) Math.round(hmAffecte.get(typeHeure));
                txt.setText(valeur.toString());
            } else {
                txt.setText("Valeur non trouvée dans la HashMap");
            }
        }
    }

    private ArrayList<TextField> genererArrayListAffecte()
    {
        ArrayList<TextField> alAffecte = new ArrayList<>();
        for (Map.Entry<String, ArrayList<TextField>> entry : hmTxtRepartion.entrySet())
        {
            String key = entry.getKey();
            ArrayList<TextField> value = entry.getValue();

            for (TextField txt: value)
                if(txt.getId().contains("Affecte"))
                    alAffecte.add(txt);
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

    public void init()
    {
        Semestre sem = Controleur.get().getMetier().getAnneeActuelle().getSemestres().get(this.semestre); // .rechercheSemestreByNumero(this.semestre);


        txtNbEtd.setText("" + sem.getNbEtd());
        txtNbGpTD.setText("" + sem.getNbGrpTD());
        txtnbGpTP.setText("" + sem.getNbGrpTP());

        if(this.typeModule != null)
            if(this.typeModule.equals("Ressource")) {
                initializeRessource();
                txtCode.setText("R" + this.semestre + "." + String.format("%02d", sem.getModules().size() + 1));
            }
            else {
                initializeStage();
                if(this.typeModule.equals("Stage"))
                    txtCode.setText("ST" + this.semestre + "." + String.format("%02d", sem.getModules().size() + 1));
                if(this.typeModule.equals("SAE"))
                    txtCode.setText("S" + this.semestre + "." + String.format("%02d", sem.getModules().size() + 1));
            }

        if (this.moduleModif != null)
        {
            txtTypeModule  .setText(this.moduleModif.getTypeModule());
            txtSemestre    .setText("" + this.semestre);
            txtSemestre    .setEditable(false);
            txtCode        .setText(this.moduleModif.getCode());
            txtLibelleLong .setText(this.moduleModif.getNom());
            txtLibelleCourt.setText(this.moduleModif.getAbreviation());
            couleur        .setValue(this.moduleModif.getCouleur());
            cbValidation   .setSelected(this.moduleModif.estValide());

            initPn     (this.moduleModif);
            initSemaine(this.moduleModif);

            calculeTotaux();

            ObservableList<Affectation> a = FXCollections.observableArrayList(this.moduleModif.getAffectations());
            tableau.setItems(a);
            calculeAffecte();

        } else
        {
            // Initialisation pour une nouvelle création
            txtTypeModule.setText(this.typeModule);
            txtTypeModule.setEditable(false);
            txtSemestre  .setText("" + this.semestre);
            txtSemestre  .setEditable(false);

            this.futurModule = new Module(txtLibelleLong.getText(),txtCode.getText(),txtLibelleCourt.getText(),txtTypeModule.getText(), couleur.getValue(), cbValidation.isSelected(), Controleur.get().getMetier().rechercheSemestreByNumero(Integer.parseInt(txtSemestre.getText())));
        }
    }

    public void initPn(Module mod)
    {
        for (Attribution att : mod.getAttributions())
        {
            if(!att.getCatHr().getNom().equals("HP")) {
                this.hmTxtPn.get(att.getCatHr().getNom()).get(0).setText(att.getNbHeurePN().toString());
                String a = calculeNvValeur(Integer.parseInt(this.hmTxtPn.get(att.getCatHr().getNom()).get(0).getText()), att.getCatHr());
                this.hmTxtPn.get(att.getCatHr().getNom()).get(1).setText(a);
            }
        }

    }

    public void initSemaine(Module mod)
    {
        for (Attribution att : mod.getAttributions())
        {
            this.hmTxtSemaine.get(att.getCatHr().getNom()).get(0).setText("" + att.getNbSemaine());
            if(!att.getCatHr().getNom().equals("HP"))
                this.hmTxtSemaine.get(att.getCatHr().getNom()).get(1).setText("" + att.getNbHeure  ().toString());
        }

        majValeurSemaine(this.hmTxtSemaine);
    }

    public void majValeurSemaine(HashMap<String, ArrayList<TextField>> hm)
    {
        for(Map.Entry<String, ArrayList<TextField>> entry : hm.entrySet())
        {
            ArrayList<TextField> value = entry.getValue();
            for (TextField txt : value)
            {
                valeurChangerSemaine(txt, txt.getText());
            }
        }
    }

    @FXML
    protected void onBtnAjouter(ActionEvent e) throws IOException
    {
        StageAjoutAffectation stage = Manager.creer("ajoutAffectation", this);

        if (this.moduleModif != null) {
            stage.setModule(this.moduleModif);
        } else {
            stage.setModule(this.futurModule);
        }

        stage.showAndWait();
        this.refresh();
        this.calculeAffecte();
    }

    @FXML
    protected void onBtnSupprimer(ActionEvent e) throws IOException {
        Affectation affectation = tableau.getSelectionModel().getSelectedItem();
        if(affectation != null && PopUp.confirmationR("Suppression d'un module", null, "Êtes-vous sûr de supprimer ce module Ressource ?")) {
            affectation.supprimer();
            calculeAffecte();
        }
        tableau.refresh();
    }

    private void creerFormatter(String nom, TextField txtf) {
        txtf.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("^\\d+$"))
            {
                String newText = change.getControlNewText();

                System.out.println("aaaaaaaaaaaaa" + nom);
                List<TextField> semaineList     = this.hmTxtSemaine  .get(nom);
                List<TextField> pnList          = this.hmTxtPn       .get(nom);
                List<TextField> repartitionList = this.hmTxtRepartion.get(nom);



                if (semaineList != null && pnList != null)
                {
                    System.out.println("cccccccccccccc");
                    int index = indexTextFied(semaineList,pnList,repartitionList,txtf);
                    System.out.println(index);
                    System.out.println(pnList.size());
                    if (index > -1 && index < pnList.size())
                    {
                        System.out.println("dddddddddddddddd");
                        int pnValue = 0;
                        if(!pnList.get(index).getText().equals(""))
                            pnValue = Integer.parseInt(pnList.get(index).getText());

                        if (!repartitionList.contains(txtf) || Integer.parseInt(newText) <= pnValue)
                        {
                            System.out.println("eeeeeeeeeeeeeeeeee");
                            txtf.setStyle("");
                        }
                        else
                        {
                            txtf.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
                        }
                    }
                    else
                    {
                        System.out.println("je suis la aaaaaaa");
                        if(index == 2) txtf.setStyle("");
                        else
                            txtf.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
                    }
                }
                else
                {
                    System.out.println("nom " + nom);

                    if(nom.equals("HP")) txtf.setStyle("");
                    else
                        txtf.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
                }
                return change;
            }
            else if (change.getText().isEmpty())
            {
                txtf.setStyle("");
                return change;
            }
            else
            {
                return null;
            }
        }));
    }

    public int indexTextFied(List<TextField> listTxt1, List<TextField> listTxt2, List<TextField> listTxt3, TextField txtf)
    {
        if(listTxt1.contains(txtf)) return listTxt1.indexOf(txtf);
        if(listTxt2.contains(txtf)) return listTxt2.indexOf(txtf);
        if(listTxt3.contains(txtf)) return listTxt3.indexOf(txtf);
        return -1;
    }

    @FXML
    protected void onBtnEnregistrer()
    {
        if(this.moduleModif != null)
        {
            majAttribution(this.moduleModif);
            majInformation(this.moduleModif);
        }
        else
        {
            majInformation(this.futurModule);
            creationAttribution(this.futurModule);
        }

        Controleur.get().getMetier().enregistrer();

        this.close();
    }

    public void creationAttribution(Module mod)
    {
        Fraction fractPn      = null;
        Fraction fractNbHeure = null;
        int nbSemaine         = 0;

        for( CategorieHeure catHr : Controleur.get().getMetier().getCategorieHeures())
        {
            boolean typeModule = this.typeModule.equals("Ressource") ? catHr.estRessource() : catHr.estStage();
            if(typeModule)
            {
                //Recuperation des paramètres du module
                fractPn      = Fraction.valueOf(getHeurePnByCatHr(catHr.getNom()));
                fractNbHeure = Fraction.valueOf(getNbHeureByCatHr(catHr.getNom(),false));
                nbSemaine    = Integer.parseInt(getNbHeureByCatHr(catHr.getNom(),true ));

                Attribution att = new Attribution(fractPn, fractNbHeure,nbSemaine, mod, catHr);
            }
        }
    }

    public void majAttribution(Module mod)
    {
        Fraction fractPn      = null;
        Fraction fractNbHeure = null;
        int nbSemaine         = 0;

        for( CategorieHeure catHr : Controleur.get().getMetier().getCategorieHeures())
        {
            boolean typeModule = this.typeModule.equals("Ressource") ? catHr.estRessource() : catHr.estStage();
            if(typeModule)
            {
                //Recuperation des paramètres du module
                fractPn      = Fraction.valueOf(getHeurePnByCatHr(catHr.getNom()));
                fractNbHeure = Fraction.valueOf(getNbHeureByCatHr(catHr.getNom(),false));
                nbSemaine    = Integer.parseInt(getNbHeureByCatHr(catHr.getNom(),true ));

                for (Attribution att : mod.getAttributions())
                {
                    att.setNbHeurePN( fractPn      );
                    att.setNbHeure  ( fractNbHeure );
                    att.setNbSemaine( nbSemaine    );
                }
            }
        }
    }

    public void majInformation(Module mod)
    {
        mod.setNom(txtLibelleLong.getText());
        mod.setCode(txtCode.getText());
        mod.setAbreviation(txtLibelleCourt.getText());
        mod.setCouleur(couleur.getValue());
        mod.setValidation(cbValidation.isSelected());
    }



    public String getNbHeureByCatHr(String nom,boolean nbSemaine)
    {
        for (Map.Entry<String,ArrayList<TextField>> entry : hmTxtSemaine.entrySet())
        {
            String key                 = entry.getKey  ();
            ArrayList<TextField> value = entry.getValue();
            if(key.equals(nom) && !key.equals("HP"))
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
            if (key.equals("HP"))
            {
                return value.get(0).getText();
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
        ensAff.clear();

        if(this.moduleModif == null) this.futurModule.supprimer(false,true);
        Controleur.get().getMetier().rollback();
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
        System.out.println("nom " + nom);

        if(nom.equals("HP") || nom.equals("H tut") || nom.equals("H Saé"))
        {
            flowPanes[0].getChildren().add(new Label("Nombre d'heure"));

            TextField txtNbSemaine = creerTextField("txt" + nom + "NbSemaine");
            flowPanes[1].getChildren().add(txtNbSemaine);
        }
        else
        {
            // Ajouter du contenu aux FlowPane
            flowPanes[0].getChildren().add(new Label("Nombre Semaine"));

            TextField txtNbSemaine = creerTextField("txt" + nom + "NbSemaine");
            flowPanes[1].getChildren().add(txtNbSemaine);

            flowPanes[2].getChildren().add(new Label("Nombre heure / semaine"));

            TextField txtNbHrSem = creerTextField("txt" + nom + "NbHrSem");
            flowPanes[3].getChildren().add(txtNbHrSem);
        }



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
        if(!nom.equals("HP")) {

            ArrayList<FlowPane> ensFp = new ArrayList<>();
            for (int i = 0; i <= 2; i++) {
                FlowPane fp = new FlowPane();
                fp.setAlignment(Pos.CENTER);

                ensFp.add(fp);
            }
            Label lbl = new Label(nom);
            ensFp.get(0).getChildren().add(lbl);

            TextField txt1 = new TextField();
            TextField txt2 = new TextField();

            if (this.moduleModif != null && !nom.equals("TO"))
            {
                System.out.println(nom);
                CategorieHeure catHr = Astre.rechercherCatHr(Controleur.get().getMetier().getCategorieHeures(), nom);

                txt1.setText(this.moduleModif.getAttribution(catHr).getNbHeurePN() != null ? this.moduleModif.getAttribution(catHr).getNbHeurePN().toString() : "0");
            }

            txt1.setId("txt" + nom + "Pn");

            if (nom.equals("TO"))
                txt1.setEditable(false);

            txt2.setId("txt" + nom + "PromoPn");
            txt2.setEditable(false);

            txt1.setPrefSize(50, 26);
            txt2.setPrefSize(50, 26);

            ensFp.get(1).getChildren().add(txt1);
            ensFp.get(2).getChildren().add(txt2);

            gridPn.getColumnConstraints().add(new ColumnConstraints());

            int cpt = 0;
            for (FlowPane fp : ensFp) {
                gridPn.add(fp, gridPn.getColumnConstraints().size() - 1, cpt++);
            }
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
        for (CategorieHeure cat : Controleur.get().getMetier().getCategorieHeures())
        {
            boolean typeModule = this.typeModule.equals("Ressource") ? cat.estRessource() : cat.estStage();
            if(typeModule)
            {
                ajouterOnglet(cat.getNom());
            }
        }
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

        CategorieHeure catHrTxtF = null;
        for (CategorieHeure catHr: Controleur.get().getMetier().getCategorieHeures())
        {
            if(txt.getId().contains(catHr.getNom()))
            {
                catHrTxtF = catHr;
            }
        }

        int valeurSemaine = 0;
        int valeurFinal   = 0;

        System.out.println(catHrTxtF.getNom().toUpperCase());
        if (this.hmTxtSemaine.containsKey(catHrTxtF.getNom().toUpperCase()) &&
                !this.hmTxtSemaine.get(catHrTxtF.getNom().toUpperCase()).isEmpty() &&
                !this.hmTxtSemaine.get(catHrTxtF.getNom().toUpperCase()).get(0).getText().isEmpty()) {

            int a;
            if (this.hmTxtSemaine.get(catHrTxtF.getNom().toUpperCase()).size() > 1 && !this.hmTxtSemaine.get(catHrTxtF.getNom()).get(1).getText().equals("")) {
                a = catHrTxtF.getNom().toUpperCase().equals("HP") ? 1 : Integer.parseInt(this.hmTxtSemaine.get(catHrTxtF.getNom()).get(1).getText());
            } else {
                // Si le HashMap contient seulement un élément, utilisez une valeur par défaut ou une autre logique selon vos besoins.
                a = 1;  // Vous pouvez choisir une autre valeur par défaut si nécessaire.
            }
            valeurSemaine = Integer.parseInt(this.hmTxtSemaine.get(catHrTxtF.getNom().toUpperCase()).get(0).getText()) * a;
        }

        for (TextField txt1 : this.hmTxtRepartion.get(catHrTxtF.getNom().toUpperCase()))
        {
            System.out.println(this.hmTxtRepartion);
            if(txt1.getId().equals("txt" + catHrTxtF.getNom() +"Repartition"))
            {
                txt1.setText(Integer.toString(valeurSemaine));
            }

            if(txt1.getId().equals("txt" + catHrTxtF.getNom() + "Promo"))
            {
                if(catHrTxtF.getNom().equals("TP") || catHrTxtF.getNom().equals("TD"))
                {
                    int valeurXgroupe = valeurSemaine * (catHrTxtF.getNom().equals("TP") ? Integer.parseInt(this.txtnbGpTP.getText()) : Integer.parseInt(this.txtNbGpTD.getText()));
                    valeurFinal   = (int) (valeurXgroupe * catHrTxtF.getEquivalentTD().value());
                }
                else
                {
                    valeurFinal = (int) (valeurSemaine * catHrTxtF.getEquivalentTD().value());
                }

                txt1.setText(Integer.toString(valeurFinal));
            }
        }

    }

    private void valeurChangerPn(TextField txt, String newValue)
    {
        CategorieHeure catHrTxtF = null;
        for (CategorieHeure catHr: Controleur.get().getMetier().getCategorieHeures())
        {
            if(txt.getId().contains(catHr.getNom()))
            {
                catHrTxtF = catHr;
            }
        }


        if(!catHrTxtF.getNom().equals("TO"))
        {
            for (TextField txt1 : this.hmTxtPn.get(catHrTxtF.getNom().toUpperCase())) {
                if (!txt1.isEditable()) {
                    if (!txt.getText().isEmpty()) {
                        int valeurInitial = Integer.parseInt(txt.getText());
                        txt1.setText(calculeNvValeur(valeurInitial, catHrTxtF));
                    }
                }
            }
            calculeTotaux();
        }
    }

    private void calculeTotaux() {
        int total               = 0;
        int totalPromo          = 0;
        TextField txtTotPn      = null;
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
        if(catHr.getNom().equals("TP") || catHr.getNom().equals("TD"))
        {
            int valeurXgroupe = valeurInitial * (catHr.getNom().equals("TP") ? Integer.parseInt(this.txtnbGpTP.getText()) : Integer.parseInt(this.txtNbGpTD.getText()));
            int valeurFinal   = (int) (valeurXgroupe * catHr.getEquivalentTD().value());
            return Integer.toString(valeurFinal);
        }
        else
        {
            int valeurFinal = (int) (valeurInitial * catHr.getEquivalentTD().value());
            return Integer.toString(valeurFinal);
        }
    }

    private HashMap<String, ArrayList<TextField>> initHmPn(ArrayList<TextField> ensTxt)
    {
        HashMap<String, ArrayList<TextField>> hmTemp = new HashMap<>();
        for (TextField txt: ensTxt)
        {
            String key = "TO";
            for (CategorieHeure catHr: Controleur.get().getMetier().getCategorieHeures())
            {
                if(txt.getId().contains(catHr.getNom()))
                {
                    key = catHr.getNom().toUpperCase();
                }
            }

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
        //System.out.println(hmTemp);
        return hmTemp;
    }

    private String getCellValue(Affectation affectation) {
        if (affectation.isSupprime()) {
            return "❌";
        } else if (affectation.isAjoute()) {
            return "➕";
        } else {
            return "";
        }
    }

    public void refresh()
    {
        tableau.setItems(ensAff);
    }

    public void setSemestre(int semestre)
    {
        this.semestre = semestre;
    }

    public Semestre getSemestre(){return Controleur.get().getMetier().rechercheSemestreByNumero(this.semestre);}

    public void setModule(Module mod) {
        this.moduleModif = mod;
    }

    public void setTypeModule(String typeModule)
    {
        System.out.println("je rentre ici typeModule");
        this.typeModule = typeModule;
        txtTypeModule.setText(this.typeModule);
    }
}