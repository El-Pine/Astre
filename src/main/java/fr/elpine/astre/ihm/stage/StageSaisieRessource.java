package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.outil.Creation;
import fr.elpine.astre.metier.outil.Fraction;
import fr.elpine.astre.metier.outil.Recuperation;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.*;


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
    public TableColumn<Affectation, String> tcTotalEqtd;
    @FXML
    public static ObservableList<Affectation> ensAff;

    public static ArrayList<CategorieHeure> ensCatHrPresent;

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

    /*-------------*/
    /*   GETTER    */
    /*-------------*/

    public String getNbHeureByCatHr(String nom,boolean nbSemaine)
    {
        for (Map.Entry<String,ArrayList<TextField>> entry : hmTxtSemaine.entrySet())
        {
            String key                 = entry.getKey  ();
            ArrayList<TextField> value = entry.getValue();

            if(key.equals(nom) && !key.equals("HP"))
                if(nbSemaine)
                    return value.get(0).getText();
                else
                    return value.get(1).getText();
            if (key.equals("HP")) return value.get(0).getText();
        }
        return "0";
    }

    public String getHeurePnByCatHr(String nom)
    {
        for (Map.Entry<String, ArrayList<TextField>> entry : hmTxtPn.entrySet())
        {
            String key                 = entry.getKey  ();
            ArrayList<TextField> value = entry.getValue();

            if(key.equals(nom)) return value.get(0).getText();
        }
        return null;
    }

    public Semestre getSemestre(){return Controleur.get().getMetier().rechercheSemestreByNumero(this.semestre);}

    private String getCellValue(Affectation affectation) {
        if (affectation.isSupprime()) {
            return "❌";
        } else if (affectation.isAjoute()) {
            return "➕";
        } else if (affectation.isModifie()) {
            return "🖉";
        } else {
            return "";
        }
    }

    public static ArrayList<CategorieHeure> getLstCatH() { return StageSaisieRessource.ensCatHrPresent; }

    /*-----------*/
    /*   SETTER  */
    /*-----------*/

    public void setSemestre( int    semestre ) { this.semestre    = semestre ;}
    public void setModule  ( Module mod      )
    {
        this.moduleModif = mod;
        if(mod != null) {
            this.typeModule = mod.getTypeModule();
            txtTypeModule.setText(this.typeModule);
        }

    }

    public void setTypeModule(String typeModule)
    {
        this.typeModule = typeModule;
        txtTypeModule.setText(this.typeModule);
    }


    /*--------------------------*/
    /* Méthode d'initialisation */
    /*--------------------------*/

    public void initialize(URL location, ResourceBundle resources)
    {
        this.setWidth ( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );
        tableau.setEditable(true);

        ensCatHrPresent = new ArrayList<>();

        initializeDetail();

        this.setOnCloseRequest(e ->
                Controleur.get().getMetier().rollback());
    }

    public void initializeDetail()
    {
        for (CategorieHeure catHr : Controleur.get().getMetier().getCategorieHeures())
        {
            if(estTypeModule(catHr))
                ensCatHrPresent.add(catHr);
        }


        initGridPn();
        initTabPan();
        initRepartitionColumns();
        initTableColumns();
        setupListenersAndFormatters();
        tableau.setItems(ensAff);

        calculeAffecte();
    }

    public boolean estTypeModule(CategorieHeure catHr)
    {
        boolean b = false;
        if(this.typeModule != null)
        {
            switch (this.typeModule.toUpperCase())
            {
                case "RESSOURCE" -> b = catHr.estRessource();
                case "STAGE"     -> b = catHr.estStage    ();
                case "SAE"       -> b = catHr.estSae      ();
                case "PPP"       -> b = catHr.estPpp      ();
            }
        }
        return b;
    }

    private void setupListenersAndFormatters()
    {
        this.hmTxtPn = initHmPn(Recuperation.getAllTextFieldsPn(gridPn));
        activationTextField(this.hmTxtPn,"Pn");

        this.hmTxtSemaine = initHmPn(Recuperation.extractTextFields(tabPaneSemaine));
        activationTextField(this.hmTxtSemaine,"Semaine");

        this.hmTxtRepartion = initHmPn(Recuperation.getAllTextFieldsPn(gridPaneRepartition));
        activationTextField(this.hmTxtRepartion,"R");
    }

    /*-------------------*/
    /* Méthode de calcul */
    /*-------------------*/

    private void calculeAffecte() {
        // Générer la liste de champs texte
        ArrayList<TextField> alAffecte = genererArrayListAffecte();

        // Calculer les valeurs en fonction des données du modèle
        HashMap<String, Double> hmAffecte = calculerValeursAffecte();

        // Mettre à jour les champs texte avec les valeurs calculées
        majChamps(alAffecte, hmAffecte);
    }

    private HashMap<String, Double> calculerValeursAffecte()
    {
        HashMap<String, Double> hmAffecte = new HashMap<>();

        if(tableau.getItems() != null)
            for (Affectation aff : tableau.getItems())
            {
                String typeHeure = aff.getTypeHeure().getNom().toUpperCase();
                double valeur;
                if (aff.hasGrpAndNbSemaine())
                    valeur = aff.getNbSemaine() * aff.getNbGroupe() * aff.getTypeHeure().getEquivalentTD().value();
                else
                    valeur = aff.getNbHeure().value() * aff.getTypeHeure().getEquivalentTD().value();

                // Ajouter ou mettre à jour la valeur dans la HashMap
                hmAffecte.put(typeHeure, hmAffecte.getOrDefault(typeHeure, 0.0) + valeur);
            }
        return hmAffecte;
    }

    private ArrayList<TextField> genererArrayListAffecte()
    {
        ArrayList<TextField> alAffecte = new ArrayList<>();
        for (Map.Entry<String, ArrayList<TextField>> entry : hmTxtRepartion.entrySet())
        {
            ArrayList<TextField> value = entry.getValue();

            for (TextField txt: value)
                if(txt.getId().contains("Affecte"))
                    alAffecte.add(txt);
        }
        return alAffecte;
    }

    private void valeurChangerSemaine(TextField txt)
    {
        int valeurSemaine = 0;
        int valeurFinal;

        CategorieHeure catHrTxtF = null;
        for (CategorieHeure catHr: ensCatHrPresent)
        {
            if(txt.getId().contains(catHr.getNom()))
            {
                catHrTxtF = catHr;
            }
        }

        assert catHrTxtF != null;
        if (this.hmTxtSemaine.containsKey(catHrTxtF.getNom().toUpperCase()) &&
                !this.hmTxtSemaine.get(catHrTxtF.getNom().toUpperCase()).isEmpty() &&
                !this.hmTxtSemaine.get(catHrTxtF.getNom().toUpperCase()).get(0).getText().isEmpty()) {

            int a;
            if (this.hmTxtSemaine.get(catHrTxtF.getNom().toUpperCase()).size() > 1 && !this.hmTxtSemaine.get(catHrTxtF.getNom()).get(1).getText().isEmpty()) {
                a = catHrTxtF.getNom().equalsIgnoreCase("HP") ? 1 : Integer.parseInt(this.hmTxtSemaine.get(catHrTxtF.getNom()).get(1).getText());
            } else {
                // Si le HashMap contient seulement un élément, utilisez une valeur par défaut ou une autre logique selon vos besoins.
                a = 1;  // Vous pouvez choisir une autre valeur par défaut si nécessaire.
            }
            valeurSemaine = Integer.parseInt(this.hmTxtSemaine.get(catHrTxtF.getNom().toUpperCase()).get(0).getText()) * a;
        }

        for (TextField txt1 : this.hmTxtRepartion.get(catHrTxtF.getNom().toUpperCase()))
        {
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

    private void valeurChangerPn(TextField txt)
    {
        CategorieHeure catHrTxtF = null;
        for (CategorieHeure catHr: ensCatHrPresent)
            if(txt.getId().contains(catHr.getNom())) catHrTxtF = catHr;


        assert catHrTxtF != null;
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

    public void majValeurSemaine(HashMap<String, ArrayList<TextField>> hm)
    {
        for(Map.Entry<String, ArrayList<TextField>> entry : hm.entrySet())
        {
            ArrayList<TextField> value = entry.getValue();
            for (TextField txt : value)
            {
                valeurChangerSemaine(txt);
            }
        }
    }



    private void valeurChangerRepart()
    {
        int total = 0;
        int totalPromo = 0;
        int totalAffecte = 0;

        for (Map.Entry<String, ArrayList<TextField>> entry : hmTxtRepartion.entrySet()) {
            String key = entry.getKey();
            ArrayList<TextField> value = entry.getValue();

            if (!key.equals("TO")) {
                for (TextField textField : value)
                {
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

    /*--------------------------*/
    /*  Activation des boutons  */
    /*--------------------------*/

    @FXML
    protected void onBtnAnnuler() throws IOException {
        ensAff.clear();

        if(this.moduleModif == null) this.futurModule.supprimer(false,true);

        Controleur.get().getMetier().rollback();
        this.close();
    }

    @FXML
    protected void onBtnAjouter() throws IOException
    {
        StageAjoutAffectation stage = Manager.creer("ajoutAffectation", this);

        if(stage != null)
        {
            stage.setModule(this.moduleModif != null ? this.moduleModif : this.futurModule);

            stage.showAndWait();
            this.refresh();
            this.calculeAffecte();
        }
    }

    @FXML
    protected void onBtnSupprimer()
    {
        Affectation affectation = tableau.getSelectionModel().getSelectedItem();
        if(affectation != null && PopUp.confirmationR("Suppression d'un module", null, "Êtes-vous sûr de supprimer ce module Ressource ?")) {
            affectation.supprimer();
            calculeAffecte();
        }
        tableau.refresh();
    }

    @FXML
    protected void onBtnEnregistrer()
    {
        Module mod = this.moduleModif != null ? this.moduleModif : this.futurModule;

        if(majInformation(mod))
        {
            if (this.moduleModif != null) majAttribution(mod);
            else creationAttribution(mod);

            Controleur.get().getMetier().enregistrer();
            this.close();
        }
    }

    /*-------------------*/
    /*    Activation     */
    /*-------------------*/

    private void activationTextField(HashMap<String, ArrayList<TextField>> hm,String type)
    {
        hm.forEach((key,value) -> {
            for (TextField txt: value)
            {
                if(txt.isEditable() || type.equals("R"))
                {
                    switch (type)
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

    /*----------------*/
    /* Méthode de maj */
    /*----------------*/

    public void majAttribution(Module mod) {
        for (CategorieHeure catHr : ensCatHrPresent)
        {
            // Récupération des paramètres du module
            Fraction fractPn      = Fraction.valueOf(textOrDefault(getHeurePnByCatHr(catHr.getNom().toUpperCase())));
            Fraction fractNbHeure = Fraction.valueOf(textOrDefault(getNbHeureByCatHr(catHr.getNom(), false)));
            int nbSemaine         = Integer.parseInt(textOrDefault(getNbHeureByCatHr(catHr.getNom(), true)));

            // Vérifie si une attribution existe déjà pour la catégorie d'heure
            boolean attributionExistante = false;
            for (Attribution att : mod.getAttributions())
            {
                if (att.getCatHr().equals(catHr))
                {
                    att.setNbHeurePN(fractPn);
                    att.setNbHeure(fractNbHeure);
                    att.setNbSemaine(nbSemaine);
                    attributionExistante = true;
                }
            }

            // Si aucune attribution n'a été trouvée, crée une nouvelle attribution
            if (!attributionExistante)
            {
                new Attribution(fractPn,fractNbHeure,nbSemaine,mod,catHr);
            }
        }
    }


    public boolean majInformation(Module mod)
    {
        txtLibelleCourt.setStyle("");
        txtLibelleLong .setStyle("");
        txtCode        .setStyle("");
        if (txtLibelleLong.getText().isEmpty() || txtLibelleCourt.getText().isEmpty() || txtCode.getText().isEmpty()) {
            String champVide = "";
            TextField txt = null;

            if (txtLibelleLong.getText().isEmpty())
            {
                champVide = "Libellé Long";
                txt = txtLibelleLong;

            } else if (txtLibelleCourt.getText().isEmpty())
            {
                champVide = "Libellé Court";
                txt = txtLibelleCourt;
            } else if (txtCode.getText().isEmpty())
            {
                champVide = "Code";
                txt = txtCode;
            }
            assert txt != null;
            txt.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");

            PopUp.error("Champ Vide", null, "Attention le champ " + champVide + " est vide.").showAndWait();
            return false;
        }
        if(Controleur.get().getMetier().existeModule(this.getSemestre(), txtCode.getText()))
        {
            PopUp.error("Code déja pris", null,"Le code doit être unique").showAndWait();
            txtCode.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
            return false;
        }

        mod.setNom        ( txtLibelleLong .getText   ());


        mod.setCode       ( txtCode        .getText   ());
        mod.setAbreviation( txtLibelleCourt.getText   ());
        mod.setCouleur    ( couleur        .getValue  ());
        mod.setValidation ( cbValidation   .isSelected());

        return true;
    }


    private void majChamps(ArrayList<TextField> alAffecte, HashMap<String, Double> hmAffecte)
    {
        for (TextField txt : alAffecte)
        {
            String typeHeure = "";
            for (CategorieHeure catHr: ensCatHrPresent)
                if(txt.getId().contains(catHr.getNom())) typeHeure = catHr.getNom().toUpperCase();

            if (hmAffecte.containsKey(typeHeure)) txt.setText( ""+(int) Math.round(hmAffecte.get(typeHeure)) );
            else                                  txt.setText( "Valeur non trouvée dans la HashMap"          );
        }
    }


    /*----------------------*/
    /*   Méthode d'ajouter  */
    /*----------------------*/

    private void ajouterOnglet(String nom)
    {
        GridPane grid = new GridPane();
        grid.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints());

        FlowPane[] flowPanes = new FlowPane[4];
        for (int i = 0; i < flowPanes.length; i++)
        {
            FlowPane fp = new FlowPane();
            fp.setAlignment(Pos.CENTER);
            flowPanes[i] = fp;
        }

        if (Objects.requireNonNull(Astre.rechercherCatHr(ensCatHrPresent, nom)).estHebdo())
        {
            flowPanes[0].getChildren().add(new Label("Nombre Semaine"));
            TextField txtNbSemaine = Creation.creerTextField("txt" + nom + "NbSemaine");
            flowPanes[1].getChildren().add(txtNbSemaine);
        }

        flowPanes[2].getChildren().add(new Label("Nb Heure"));
        TextField txtNbHrSem = Creation.creerTextField("txt" + nom + "NbHrSem");
        flowPanes[3].getChildren().add(txtNbHrSem);

        grid.add(flowPanes[0], 0, 0);
        grid.add(flowPanes[1], 0, 1);
        grid.add(flowPanes[2], 1, 0);
        grid.add(flowPanes[3], 1, 1);

        Tab tab = new Tab(nom);
        tab.setContent(grid);

        tabPaneSemaine.getTabs().add(tab);
    }

    private void ajouterColonne(String nom)
    {
        if(!nom.equals("HP")) //Astre.rechercherCategorie(nom).isPonctuelle()
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

            if (this.moduleModif != null && !nom.equals("TO"))
            {
                CategorieHeure catHr = Astre.rechercherCatHr(ensCatHrPresent, nom);
                txt1.setText(this.moduleModif.getAttribution(catHr) != null ? this.moduleModif.getAttribution(catHr).getNbHeurePN().toString() : "0");
            }

            txt1.setId("txt" + nom + "Pn");

            if (nom.equals("TO")) txt1.setEditable(false);

            txt2.setId("txt" + nom + "PromoPn");
            txt2.setEditable(false);
            txt2.setFocusTraversable(false);

            txt1.setPrefSize(50, 26);
            txt2.setPrefSize(50, 26);

            ensFp.get(1).getChildren().add(txt1);
            ensFp.get(2).getChildren().add(txt2);

            gridPn.getColumnConstraints().add(new ColumnConstraints());

            int cpt = 0;
            for (FlowPane fp : ensFp) gridPn.add(fp, gridPn.getColumnConstraints().size() - 1, cpt++);
        }
    }


    private void ajouterColonneRepartition(String nom) {
        ArrayList<FlowPane> ensFp = new ArrayList<>();

        for (int i = 0; i <= 3; i++) {
            FlowPane fp = Creation.creerFlowPane();
            ensFp.add(fp);
        }

        Label lbl = new Label(nom);
        ensFp.get(0).getChildren().add(lbl);

        String[] ids = { "Repartition", "Promo", "Affecte" };
        for (int i = 0; i < ids.length; i++) {
            TextField txt = Creation.creerTextField("txt" + nom + ids[i]);
            txt.setEditable(false);
            ensFp.get(i + 1).getChildren().add(txt);
        }

        gridPaneRepartition.getColumnConstraints().add(new ColumnConstraints());

        int cpt = 0;
        int columnIndex = gridPaneRepartition.getColumnConstraints().size();
        for (FlowPane fp : ensFp)
        {
            gridPaneRepartition.add(fp, columnIndex - 1, cpt++);
        }
    }

    /*----------------*/
    /*     Init       */
    /*----------------*/

    public void init()
    {
        Semestre sem = this.getSemestre();

        txtNbEtd .setText("" + sem.getNbEtd  ());
        txtNbGpTD.setText("" + sem.getNbGrpTD());
        txtnbGpTP.setText("" + sem.getNbGrpTP());

        if(this.typeModule != null)
        {
            txtCode.setText(definirCode(sem));
        }
        initializeDetail();

        if (this.moduleModif != null)
        {
            txtTypeModule  .setText(this.moduleModif.getTypeModule());
            txtTypeModule  .setEditable(false);
            txtTypeModule.setDisable(true);
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

            ensAff = FXCollections.observableArrayList(this.moduleModif.getAffectations());
            tableau.setItems(ensAff);
            tableau.requestFocus();
            calculeAffecte();

        } else
        {
            // Initialisation pour une nouvelle création
            txtTypeModule.setText(this.typeModule);
            txtTypeModule.setEditable(false);
            txtSemestre  .setText("" + this.semestre);
            txtSemestre  .setEditable(false);

            ensAff = FXCollections.observableArrayList(new ArrayList<>());

            this.futurModule = new Module(txtLibelleLong.getText(),"",txtLibelleCourt.getText(),txtTypeModule.getText(), couleur.getValue(), cbValidation.isSelected(), Controleur.get().getMetier().rechercheSemestreByNumero(Integer.parseInt(txtSemestre.getText())));
        }
    }

    private void initTableColumns() {

        ArrayList<CategorieHeure> ensCatH = ensCatHrPresent;
        ArrayList<String>      ensNomCatH = new ArrayList<>();

        for (CategorieHeure catHr : ensCatH)
            if ( estTypeModule(catHr) && !catHr.getNom().equals("HP"))
                ensNomCatH.add(catHr.getNom());

        for (CategorieHeure cat : ensCatH)
        {
            ajouterColonne(cat.getNom());
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
                } else if (item != null && item.equals("🖉")) {
                    setTextFill(Color.BLUE);
                } else {
                    setTextFill(Color.BLACK);
                    setText("");
                }
            }
        });

        tcIntervenant.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIntervenant().getNom() + " " + cellData.getValue().getIntervenant().getPrenom()));
        tcType       .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeHeure  ().getNom()));
        tcType.setCellFactory(column -> new TableCell<>() {
            final ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(ensNomCatH));
            {
                comboBox.setOnAction(event -> {
                    String newValue = comboBox.getValue();
                    String oldValue = getItem();

                    if ("HP".equals(oldValue)) {
                        event.consume(); // Annule l'édition si la valeur initiale est "HP"
                        tableau.refresh();
                        return;
                    }

                    Affectation afc = getTableView().getItems().get(getIndex());
                    if(newValue != null && !oldValue.equals(newValue))
                    {
                        if (!newValue.equals("CM")) {
                            afc.setTypeHeure(Astre.rechercherCatHr(ensCatH, newValue));
                        } else {
                            afc.setNbGroupe(1);
                            afc.setTypeHeure(Astre.rechercherCatHr(ensCatH, newValue));
                        }
                    }
                    calculeAffecte();
                    tableau.refresh();
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    comboBox.setValue(item);
                    setGraphic(comboBox);
                }
            }
        });

        tcSemaine    .setCellValueFactory(cellData -> cellData.getValue().hasGrpAndNbSemaine() ? new SimpleIntegerProperty(cellData.getValue().getNbSemaine()).asObject()  : null);
        tcSemaine.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tcSemaine.setCellFactory(column -> new TableCell<>() {
            final TextField textField = new TextField();
            {
                creerFormatter(textField);

                textField.setOnAction(event -> {
                    String newValue = textField.getText();
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Affectation afc = getTableView().getItems().get(index);
                        afc.setNbGroupe(Integer.parseInt(newValue));
                        calculeAffecte();
                        tableau.refresh();
                    }
                });

                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    Affectation afc = getTableView().getItems().get(getIndex());
                    boolean isHP = !afc.getTypeHeure().getNom().equals("HP");
                    setEditable(isHP);
                });
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(null); // Assurez-vous que le texte dans la cellule de la TableView est vide
                    textField.setText("" + item); // Définit le texte dans le TextField
                    setGraphic(textField);
                    Affectation afc = getTableView().getItems().get(getIndex());
                    boolean isHP = !afc.getTypeHeure().getNom().equals("HP");
                    setEditable(isHP);
                }
            }
        });

        tcGrp        .setCellValueFactory(cellData -> cellData.getValue().hasGrpAndNbSemaine() ? new SimpleIntegerProperty(cellData.getValue().getNbGroupe ()).asObject()  : null);
        tcGrp.setCellFactory(column -> new TableCell<>() {
            final TextField textField = new TextField();
            {
                creerFormatter(textField);

                textField.setOnAction(event -> {
                    String newValue = textField.getText();
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Affectation afc = getTableView().getItems().get(index);
                        afc.setNbGroupe(Integer.parseInt(newValue));
                        calculeAffecte();
                        tableau.refresh();
                    }
                });

                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    Affectation afc = getTableView().getItems().get(getIndex());
                    boolean isHP = !afc.getTypeHeure().getNom().equals("HP") && !afc.getTypeHeure().getNom().equals("CM");
                    setEditable(isHP);
                });
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(null); // Assurez-vous que le texte dans la cellule de la TableView est vide
                    textField.setText("" + item); // Définit le texte dans le TextField
                    setGraphic(textField);
                    Affectation afc = getTableView().getItems().get(getIndex());
                    boolean isHP = !afc.getTypeHeure().getNom().equals("HP") && !afc.getTypeHeure().getNom().equals("CM");
                    setEditable(isHP);
                }
            }
        });

        tcNbH        .setCellValueFactory(cellData -> cellData.getValue().hasNbHeure        () ? new SimpleStringProperty (cellData.getValue().getNbHeure  () .toString()) : null);
        tcNbH.setCellFactory(column -> new TableCell<>() {
            final TextField textField = new TextField();
            {
                creerFormatter(textField);

                textField.setOnAction(event -> {
                    String newValue = textField.getText();
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Affectation afc = getTableView().getItems().get(index);
                        afc.setNbHeure(Fraction.valueOf(newValue)); // Mettre à jour votre donnée
                        calculeAffecte();
                        tableau.refresh();
                    }
                });

                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    boolean isHP = getTableView().getItems().get(getIndex()).getTypeHeure().getNom().equals("HP");
                    setEditable(isHP);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(null); // Assurez-vous que le texte dans la cellule de la TableView est vide
                    textField.setText(item); // Définit le texte dans le TextField
                    setGraphic(textField);
                    boolean isHP = getTableView().getItems().get(getIndex()).getTypeHeure().getNom().equals("HP");
                    setEditable(isHP);
                }
            }
        });


        tcTotalEqtd  .setCellValueFactory(cellData -> new SimpleStringProperty (Fraction.simplifyDouble(cellData.getValue().getTotalEqtd(true),true)));
        tcCommentaire.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCommentaire()));
        tcCommentaire.setCellFactory(TextFieldTableCell.forTableColumn());
        tcCommentaire.setOnEditCommit(event -> {
            // Mettez à jour les données avec la nouvelle valeur
            if ( !event.getOldValue().equals(event.getNewValue() ))
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setCommentaire(event.getNewValue());
            tableau.refresh();
            // Vous pouvez ajouter ici le code pour sauvegarder les modifications
        });
    }

    public void initPn(Module mod)
    {
        for (Attribution att : mod.getAttributions())
            if(!att.getCatHr().getNom().equals("HP"))
            {
                this.hmTxtPn.get(att.getCatHr().getNom().toUpperCase()).get(0).setText(att.getNbHeurePN().toString());

                String a = calculeNvValeur(Integer.parseInt(textOrDefault(this.hmTxtPn.get(att.getCatHr().getNom().toUpperCase()).get(0).getText())), att.getCatHr());
                this.hmTxtPn.get(att.getCatHr().getNom().toUpperCase()).get(1).setText(a);
            }
    }

    private void initRepartitionColumns()
    {
        this.gridPaneRepartition.getChildren().clear();
        for (CategorieHeure cat : ensCatHrPresent)
        {
            ajouterColonneRepartition(cat.getNom());
        }
    }
    public void initSemaine(Module mod)
    {
        for (Attribution att : mod.getAttributions())
        {
            this.hmTxtSemaine.get(att.getCatHr().getNom().toUpperCase()).get(0).setText("" + att.getNbSemaine());

            if(!att.getCatHr().estHebdo()) this.hmTxtSemaine.get(att.getCatHr().getNom().toUpperCase()).get(0).setText(att.getNbHeure().toString());
        }
        majValeurSemaine(this.hmTxtSemaine);
    }

    private void initTabPan()
    {
        this.tabPaneSemaine.getTabs().clear();
        for (CategorieHeure cat : ensCatHrPresent) ajouterOnglet(cat.getNom());
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

    private HashMap<String, ArrayList<TextField>> initHmPn(ArrayList<TextField> ensTxt)
    {
        HashMap<String, ArrayList<TextField>> hmTemp = new HashMap<>();
        for (TextField txt: ensTxt)
        {
            String key = "TO";
            for (CategorieHeure catHr: ensCatHrPresent)
                if(txt.getId().contains(catHr.getNom()))
                    key = catHr.getNom().toUpperCase();

            if(hmTemp.containsKey(key)) hmTemp.get(key).add(txt);
            else
            {
                hmTemp.put(key,new ArrayList<>());
                hmTemp.get(key).add(txt);
            }
        }
        return hmTemp;
    }

    /*-----------------------------*/
    /*    Ajout des Listenner      */
    /*-----------------------------*/

    private void ajouterListener(TextField txt) {txt.textProperty().addListener((observable, oldValue, newValue) -> valeurChangerPn(txt));}
    private void ajouterListenerSemaine(TextField txt) {txt.textProperty().addListener((observable, oldValue, newValue) -> valeurChangerSemaine(txt));}
    private void ajouterListenerTotal(TextField txt) {txt.textProperty().addListener((observable, oldValue, newValue) -> valeurChangerRepart());}

    /*----------------*/
    /* Méthode outils */
    /*----------------*/

    private void creerFormatter(String nom, TextField txtf) {
        txtf.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("^\\d+$"))
            {
                String newText = change.getControlNewText();

                List<TextField> semaineList     = this.hmTxtSemaine  .get(nom);
                List<TextField> pnList          = this.hmTxtPn       .get(nom);
                List<TextField> repartitionList = this.hmTxtRepartion.get(nom);

                if (semaineList != null && pnList != null)
                {
                    int index = Creation.indexTextFied(semaineList,pnList,repartitionList,txtf);
                    if (index > -1 && index < pnList.size())
                    {
                        int pnValue = 0;
                        if(!pnList.get(index).getText().isEmpty())
                            pnValue = Integer.parseInt(pnList.get(index).getText());

                        if (!repartitionList.contains(txtf) || Integer.parseInt(newText) <= pnValue)
                        {
                            txtf.setStyle("");
                        }
                        else
                        {
                            txtf.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
                        }
                    }
                    else
                    {
                        if(index == 2) txtf.setStyle("");
                        else
                            txtf.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
                    }
                }
                else
                {
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

    private void creerFormatter(TextField txtf) {
        txtf.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("^\\d+$")) {
                txtf.setStyle("");
                return change;
            } else if (change.getText().isEmpty()) {
                txtf.setStyle("");
                return change;
            } else {
                return null;
            }
        }));
    }

    public void creationAttribution(Module mod)
    {
        Fraction fractPn;
        Fraction fractNbHeure;
        int nbSemaine;

        for( CategorieHeure catHr : ensCatHrPresent)
        {
            //Recuperation des paramètres du module
            fractPn      = Fraction.valueOf(textOrDefault(getHeurePnByCatHr(catHr.getNom())));
            fractNbHeure = Fraction.valueOf(textOrDefault(getNbHeureByCatHr(catHr.getNom(),false)));
            nbSemaine    = Integer.parseInt(textOrDefault(getNbHeureByCatHr(catHr.getNom(),true )));

            new Attribution(fractPn, fractNbHeure,nbSemaine, mod, catHr);
        }
    }

    public String definirCode(Semestre sem)
    {
        if(this.typeModule.equals("Stage"))
            return "ST" + this.semestre + "." + String.format("%02d", sem.getModules().size() + 1);
        if(this.typeModule.equals("Sae"))
            return "S" + this.semestre + "." + String.format("%02d", sem.getModules().size() + 1);
        if(this.typeModule.equals("Ressource") || this.typeModule.equals("PPP"))
            return "R" + this.semestre + "." + String.format("%02d", sem.getModules().size() + 1);

        return null;
    }

    public String textOrDefault(String s)
    {
        if(s != null)
            if(s.isEmpty())
                return "0";
            else
                return s;
        else
            return "0";
    }

    public void refresh()
    {
        tableau.setItems(ensAff);
    }
}