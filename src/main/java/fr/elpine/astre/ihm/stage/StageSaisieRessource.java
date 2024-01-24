package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.ihm.outil.*;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.outil.Creation;
import fr.elpine.astre.metier.outil.Fraction;
import fr.elpine.astre.metier.outil.ModuleType;
import fr.elpine.astre.metier.outil.Recuperation;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class StageSaisieRessource extends Stage implements Initializable
{
    @FXML private ColorPicker couleur;
    @FXML private CheckBox    cbValidation;
    @FXML private MenuButton  btnTypeHeure;
    @FXML private TabPane     tabPaneSemaine; // partie semaine

    @FXML private TableView<Affectation> tableau;

    @FXML private TableColumn<Affectation, String>  tc;
    @FXML private TableColumn<Affectation, String>  tcIntervenant;
    @FXML private TableColumn<Affectation, CategorieHeure>  tcType;
    @FXML private TableColumn<Affectation, String>  tcSemaine;
    @FXML private TableColumn<Affectation, String>  tcNbH;
    @FXML private TableColumn<Affectation, String>  tcGrp;
    @FXML private TableColumn<Affectation, String>  tcCommentaire;
    @FXML private TableColumn<Affectation, String>  tcTotalEqtd;


    @FXML private TextField txtTypeModule;
    @FXML private TextField txtnbGpTP;
    @FXML private TextField txtNbGpTD;
    @FXML private TextField txtNbEtd;
    @FXML private TextField txtLibelleLong;
    @FXML private TextField txtLibelleCourt;
    @FXML private TextField txtSemestre;
    @FXML private TextField txtTORepartion;
    @FXML private TextField txtTOPromo;
    @FXML private TextField txtTOAffecte;
    @FXML private TextField txtTORepartionPN;
    @FXML private TextField txtTOPromoPN;
    @FXML private TextField txtCode;

    @FXML private GridPane gridPn; // partie pn
    @FXML private GridPane gridPaneRepartition; // partie repartition
    @FXML private GridPane gridTot; // partie repartition (somme)

    private HashMap<String, ArrayList<TextField>> hmTxtPn;
    private HashMap<String, ArrayList<TextField>> hmTxtSemaine;
    private HashMap<String, ArrayList<TextField>> hmTxtRepartion;

    public ArrayList<CategorieHeure> ensCatHrPresent;

    private ModuleType typeModule;
    private Module module;
    private Semestre                semestre;
    private boolean warn = false;

    private HashMap<Attribution, ArrayList<TextField>> mapTxt;

    private HashMap<TextField, Boolean> validMap;
    private HashMap<TextField, Boolean> validMapDetail;


    public StageSaisieRessource() // fxml -> "saisieRessource"
    {
        this.setTitle("Affectation");
        this.setMinWidth(1460);
        this.setMinHeight(700);
        this.setOnCloseRequest(e -> Controleur.get().getMetier().rollback());
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

            if (key.equals(nom) && !Astre.rechercherCatHr(ensCatHrPresent,nom).estHebdo())
                if(nbSemaine)
                    return value.get(0).getText();
                else
                    return value.get(1).getText();
            else return value.get(0).getText();
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

    private String getCellValue(Affectation affectation) {
        if (affectation.isSupprime()) {
            return "S";
        } else if (affectation.isAjoute()) {
            return "A";
        } else if (affectation.isModifie()) {
            return "M";
        } else {
            return "";
        }
    }

    public ArrayList<CategorieHeure> getLstCatH() { return this.ensCatHrPresent; }

    /*-----------*/
    /*   SETTER  */
    /*-----------*/

    public void setSemestre( Semestre semestre ) { this.semestre = semestre ;}
    public void setModule  ( Module mod        ) { this.module = mod        ;}
    public void setTypeModule(ModuleType typeModule)
    {
        // Arrays.asList("Ressource", "SAÉ", "Stage/Suivi", "PPP");

        txtTypeModule.setText( (this.typeModule = typeModule).getLabel() );
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

        //initializeDetail();

        this.validMap = new HashMap<>();

        Regex.activerRegex(Regex.REGEX_ALL_NOT_EMPTY, Regex.REGEX_ALL, this.txtCode,         validMap, false);
        Regex.activerRegex(Regex.REGEX_ALL_NOT_EMPTY, Regex.REGEX_ALL, this.txtLibelleLong,  validMap, false);
        Regex.activerRegex(Regex.REGEX_ALL_NOT_EMPTY, Regex.REGEX_ALL, this.txtLibelleCourt, validMap, false);
    }

    public void initializeDetail()
    {
        initGridPn();
        initTabPan();
        initRepartitionColumns();
        initTableColumns();
        setupListenersAndFormatters();
        tableau.setItems(FXCollections.observableList(this.module.getAffectations()));

        calculeAffecte();
    }

    public boolean estTypeModule(CategorieHeure catHr)
    {
        boolean b = false;
        if(this.typeModule != null)
        {
            switch (this.typeModule)
            {
                case RESSOURCE -> b = catHr.estRessource();
                case STAGE     -> b = catHr.estStage    ();
                case SAE       -> b = catHr.estSae      ();
                case PPP       -> b = catHr.estPpp      ();
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
            if (this.hmTxtSemaine.get(catHrTxtF.getNom().toUpperCase()).size() > 1 && !this.hmTxtSemaine.get(catHrTxtF.getNom()).get(1).getText().isEmpty())
            {
                a = catHrTxtF.estHebdo() ? Integer.parseInt(this.hmTxtSemaine.get(catHrTxtF.getNom()).get(1).getText()) : 1;
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
                if(catHrTxtF.getTypeGroupe().equals("TD") || catHrTxtF.getTypeGroupe().equals("TP"))
                {
                    int valeurXgroupe = valeurSemaine * (catHrTxtF.getTypeGroupe().equals("TD") ? Integer.parseInt(this.txtNbGpTD.getText()) : Integer.parseInt(this.txtnbGpTP.getText()));
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

    private void calculeTotaux()
    {
        int total               = 0;
        int totalPromo          = 0;
        TextField txtTotPn      = null;
        TextField txtTotPromoPn = null;

        for (Map.Entry<String, ArrayList<TextField>> entry : hmTxtPn.entrySet())
        {
            String key = entry.getKey();
            ArrayList<TextField> value = entry.getValue();

            if (!key.equals("TO")) {
                for (TextField txt : value)
                {
                    if (!txt.getText().isEmpty())
                    {
                        if (txt.isEditable())
                        {
                            total += Integer.parseInt(txt.getText());
                        } else
                        {
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
        Controleur.get().getMetier().rollback();
        this.close();
    }

    @FXML
    protected void onBtnAjouter()
    {
        StageAjoutAffectation stage = Manager.creer("ajoutAffectation", this);

        if (stage != null)
        {
            //stage.setParent(this);
            stage.setModule(this.module);
            stage.init();

            stage.showAndWait();
            tableau.setItems( FXCollections.observableArrayList( this.module.getAffectations() ) );
            //this.calculeAffecte();
        }
    }

    @FXML
    protected void onBtnSupprimer()
    {
        Affectation affectation = tableau.getSelectionModel().getSelectedItem();
        if (affectation != null && PopUp.confirmationR("Suppression d'une affectation", null, "Êtes-vous sûr de supprimer cette affectation ?")) {
            affectation.supprimer();
            //calculeAffecte();
        }
        tableau.refresh();
    }

    @FXML
    protected void onBtnEnregistrer()
    {
        /*if (majInformation(this.module))
        {
            // todo : faire attention ici
            if (this.module != null) majAttribution(this.module);
            else creationAttribution(this.module);

            Controleur.get().getMetier().enregistrer();
            this.close();
        }*/

        if (Regex.estValide(validMap))
        {
            String code = this.module.getCode();
            boolean b = !txtCode.getText().equals(code);

            if (Controleur.get().getMetier().existeModule(this.semestre, txtCode.getText()) && b)
                PopUp.error("Code invalide", null,"Le code du module doit être unique").showAndWait();
            else
            {
                this.module.setNom        ( txtLibelleLong .getText   ());
                this.module.setCode       ( txtCode        .getText   ());
                this.module.setAbreviation( txtLibelleCourt.getText   ());
                this.module.setCouleur    ( couleur        .getValue  ());
                this.module.setValidation ( cbValidation   .isSelected());

                // todo : gérer le détails lors de la sauvegarde

                Controleur.get().getMetier().enregistrer();
                this.close();
            }
        }
        else
            PopUp.warning("Informations incorrectes", null, "Vous devez remplir tous les champs !").showAndWait();
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

        String s = this.module.getCode();

        boolean b = !txtCode.getText().equals(s);

        if(Controleur.get().getMetier().existeModule(this.semestre, txtCode.getText()) && b)
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

    private void ajouterOnglet(CategorieHeure cat)
    {
        GridPane grid = new GridPane();
        grid.getColumnConstraints().addAll(new ColumnConstraints(175), new ColumnConstraints(175));
        grid.getRowConstraints().addAll(new RowConstraints(50), new RowConstraints(50));

        FlowPane[] flowPanes = new FlowPane[4];
        for (int i = 0; i < flowPanes.length; i++)
        {
            FlowPane fp = new FlowPane();
            fp.setAlignment(Pos.CENTER);
            flowPanes[i] = fp;
        }

        flowPanes[0].getChildren().add(new Label("Nombre Semaine"));
        TextField txtNbSemaine = new TextField();
        txtNbSemaine.setPrefWidth(50);
        Regex.activerRegex(Regex.REGEX_INT_NOT_EMPTY, Regex.REGEX_INT, txtNbSemaine, validMapDetail, false);
        flowPanes[1].getChildren().add(txtNbSemaine);

        flowPanes[2].getChildren().add(new Label("Nb Heure"));
        TextField txtNbHrSem = new TextField();
        txtNbHrSem.setPrefWidth(50);
        Regex.activerRegex(Fraction.REGEX, Fraction.REGEX_CARAC, txtNbHrSem, validMapDetail, false);
        flowPanes[3].getChildren().add(txtNbHrSem);

        Attribution a = this.module.getAttribution(cat);
        this.mapTxt.get(a).add(txtNbSemaine);
        this.mapTxt.get(a).add(txtNbHrSem);

        txtNbSemaine.setOnAction(e -> {
            if (this.validMapDetail.get(txtNbSemaine) && this.mapTxt.get(a).indexOf(txtNbSemaine) == 5) {
                a.setNbSemaine(Integer.parseInt(txtNbSemaine.getText()));
                this.chargerCalculs();
            }
        });

        txtNbHrSem.setOnAction(e -> {
            if (this.validMapDetail.get(txtNbHrSem) && this.mapTxt.get(a).indexOf(txtNbHrSem) == 6) {
                a.setNbHeure(Fraction.valueOf(txtNbHrSem.getText()));
                this.chargerCalculs();
            }
        });

        grid.add(flowPanes[0], 0, 0);
        grid.add(flowPanes[1], 0, 1);
        grid.add(flowPanes[2], 1, 0);
        grid.add(flowPanes[3], 1, 1);

        Tab tab = new Tab(cat.getNom());
        tab.setContent(grid);

        tabPaneSemaine.getTabs().add(tab);
    }

    private void ajouterColonne(CategorieHeure cat)
    {
        if ( !cat.getNom().equals("HP"))
        {
            ArrayList<FlowPane> ensFp = new ArrayList<>();
            for (int i = 0; i <= 2; i++)
            {
                FlowPane fp = new FlowPane();
                fp.setAlignment(Pos.CENTER);
                ensFp.add(fp);
            }
            Label lbl = new Label(cat.getNom());
            ensFp.get(0).getChildren().add(lbl);

            TextField txt1 = new TextField();
            TextField txt2 = new TextField();

            txt2.setEditable(false);
            txt2.setFocusTraversable(false);

            txt1.setPrefWidth(50);
            txt2.setPrefWidth(50);

            Regex.activerRegex(Fraction.REGEX, Fraction.REGEX_CARAC, txt1, validMapDetail, false);

            Attribution a = this.module.getAttribution(cat);
            this.mapTxt.get(a).add(txt1);
            this.mapTxt.get(a).add(txt2);

            txt1.setOnAction(e -> {
                if (this.validMapDetail.get(txt1) && this.mapTxt.get(a).indexOf(txt1) == 3) {
                    a.setNbHeurePN(Fraction.valueOf(txt1.getText()));
                    this.chargerCalculs();
                }
            });

            ensFp.get(1).getChildren().add(txt1);
            ensFp.get(2).getChildren().add(txt2);

            gridPn.getColumnConstraints().add(new ColumnConstraints(60));

            int cpt = 0;
            for (FlowPane fp : ensFp) gridPn.add(fp, gridPn.getColumnConstraints().size() - 1, cpt++);
        }
    }


    private void ajouterColonneRepartition(CategorieHeure cat) {
        ArrayList<FlowPane> ensFp = new ArrayList<>();

        for (int i = 0; i <= 3; i++) {
            FlowPane fp = new FlowPane();
            fp.setAlignment(Pos.CENTER);
            ensFp.add(fp);
        }

        Label lbl = new Label(cat.getNom());
        ensFp.get(0).getChildren().add(lbl);

        for (int i = 0; i < 3; i++) {
            TextField txt = new TextField();
            txt.setPrefWidth(50);

            if (i != 0 || cat.estHebdo())
                txt.setEditable(false);
            else
                Regex.activerRegex(Fraction.REGEX, Fraction.REGEX_CARAC, txt, validMapDetail, false);

            ensFp.get(i + 1).getChildren().add(txt);

            Attribution a = this.module.getAttribution(cat);
            this.mapTxt.get(a).add(txt);
            txt.setOnAction(e -> {
                if (this.validMapDetail.get(txt) && this.mapTxt.get(a).indexOf(txt) == 0) {
                    a.setNbHeure(Fraction.valueOf(txt.getText()));
                    this.chargerCalculs();
                }
            });
        }

        gridPaneRepartition.getColumnConstraints().add(new ColumnConstraints(50));

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

        txtNbEtd .setText(String.valueOf(this.semestre.getNbEtd  ()));
        txtNbGpTD.setText(String.valueOf(this.semestre.getNbGrpTD()));
        txtnbGpTP.setText(String.valueOf(this.semestre.getNbGrpTP()));

        txtSemestre.setText(String.valueOf(this.semestre.getNumero()));

        if ( this.module == null ) {
            this.module = new Module(
                    "",
                    this.definirCode(),
                    "",
                    this.typeModule,
                    couleur.getValue(),
                    cbValidation.isSelected(),
                    this.semestre
            );

            for (CategorieHeure c : Controleur.get().getMetier().getCategorieHeures())
            {
                if ( estTypeModule(c) )
                {
                    if (c.estHebdo())
                        new Attribution(Fraction.valueOf(""), Fraction.valueOf(""), 0, this.module, c);
                    else
                        new Attribution(Fraction.valueOf(""), Fraction.valueOf(""), this.module, c);
                }
            }
        }

        txtCode        .setText(this.module.getCode());
        txtLibelleLong .setText(this.module.getNom());
        txtLibelleCourt.setText(this.module.getAbreviation());

        couleur.setValue(this.module.getCouleur());

        cbValidation.setSelected(this.module.estValide());

        this.initTableColumns();

        tableau.setItems( FXCollections.observableArrayList( this.module.getAffectations() ) );
        tableau.requestFocus();

        // setup attributions (pn, semaine, repartition)

        this.btnTypeHeure.setText(" ... ");

        // todo : générer le détails
        this.gestionDetails();

        this.initAttribution(); // le btn des attributions

        // initializeDetail(); initPn(); initSemaine(); calculeAffecte();
    }

    private void gestionDetails()
    {
        // refaire la structure du détails en fonction des attributions (textField, données)
        this.gridPn.getChildren().clear();
        this.gridPn.getColumnConstraints().clear();
        this.gridPaneRepartition.getChildren().clear();
        this.gridPaneRepartition.getColumnConstraints().clear();
        this.tabPaneSemaine.getTabs().clear();
        this.tabPaneSemaine.setStyle( this.estModuleHebdo() ? "-fx-border-color: -color-border-default" : "-fx-border-color: transparent" );

        this.validMapDetail = new HashMap<>();
        this.mapTxt         = new HashMap<>();

        for (Attribution a : this.module.getAttributions())
            if (!a.isSupprime()) this.mapTxt.put(a, new ArrayList<>());

        for (CategorieHeure cat : this.module.getEnsCatHr()) {
            this.ajouterColonneRepartition(cat);
            this.ajouterColonne(cat);
            if (cat.estHebdo())
                this.ajouterOnglet(cat);
        }

        /*
         * HP        -> (c1Rep), c2Rep, c3Rep
         * pas hebdo -> (c1Rep), c2Rep, c3Rep, (c1PN), c2PN
         * hebdo     ->  c1Rep , c2Rep, c3Rep, (c1PN), c2PN, (nbSem), (nbHr)
         * */

        // charger les données des attributions dans la nouvelle structure
        this.chargerDonnees();
        this.chargerCalculs();
    }

    private void chargerDonnees()
    {
        for (Map.Entry<Attribution, ArrayList<TextField>> e : this.mapTxt.entrySet())
        {
            Attribution          a          = e.getKey();
            ArrayList<TextField> textFields = e.getValue();

            if (a.getCatHr().getNom().equalsIgnoreCase("HP"))
                textFields.get(0).setText(a.getNbHeure().toString());
            else if (a.getCatHr().estHebdo()) {
                textFields.get(3).setText(a.getNbHeurePN().toString());
                textFields.get(5).setText(String.valueOf(a.getNbSemaine()));
                textFields.get(6).setText(a.getNbHeure().toString());
            } else {
                textFields.get(0).setText(a.getNbHeure().toString());
                textFields.get(3).setText(a.getNbHeurePN().toString());
            }
        }
    }

    private void chargerCalculs()
    {
        for (Map.Entry<Attribution, ArrayList<TextField>> e : this.mapTxt.entrySet())
        {
            Attribution          a          = e.getKey();
            ArrayList<TextField> textFields = e.getValue();

            if (a.getCatHr().getNom().equalsIgnoreCase("HP")) {
                textFields.get(1).setText(Fraction.simplifyDouble(a.getNbHeurePromo(), false));
                textFields.get(2).setText(Fraction.simplifyDouble(a.getNbHeureAffecte(), false));
            } else  {
                if (a.getCatHr().estHebdo()) textFields.get(0).setText(Fraction.simplifyDouble(a.getNbHeureEtd(), false));

                textFields.get(1).setText(Fraction.simplifyDouble(a.getNbHeurePromo(), false));
                textFields.get(2).setText(Fraction.simplifyDouble(a.getNbHeureAffecte(), false));
                textFields.get(4).setText(Fraction.simplifyDouble(a.getNbHeurePNPromo(), false));
            }
        }

        this.txtTORepartionPN.setText(Fraction.simplifyDouble(this.module.getSommePN(), false));
        this.txtTOPromoPN.setText(Fraction.simplifyDouble(this.module.getSommePNPromo(), false));
        this.txtTORepartion.setText(Fraction.simplifyDouble(this.module.getSomme(), false));
        this.txtTOPromo.setText(Fraction.simplifyDouble(this.module.getSommePromo(), false));
        this.txtTOAffecte.setText(Fraction.simplifyDouble(this.module.getSommeAffecte(), false));

        if ( this.module.estModuleInvalide() && !this.warn ) {
            PopUp.warning("Module invalide", null, "Le nombre d'heure affectées dépasse le nombre d'heure du programme national").showAndWait();
            this.warn = true;
        }
    }

    private void initAttribution()
    {
        List<CheckMenuItem> items    = new ArrayList<>();

        for (CategorieHeure c : Controleur.get().getMetier().getCategorieHeures())
        {
            CheckMenuItem item = new CheckMenuItem(c.getNom());

            for (Attribution a : this.module.getAttributions()) if (a.getCatHr()     == c && !a.isSupprime()) { item.setSelected( true ); break; }
            for (Affectation a : this.module.getAffectations()) if (a.getTypeHeure() == c) { item.setDisable ( true ); break; }

            item.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
	            Attribution a =  this.module.getAttribution(c);

	            if (newValue) {
		            if (a != null) a.reactiver();
                    else {
                        if (c.estHebdo())
                            new Attribution(Fraction.valueOf(""), Fraction.valueOf(""), 0, this.module, c);
                        else
                            new Attribution(Fraction.valueOf(""), Fraction.valueOf(""), this.module, c);
                    }
                } else {
		            if (a != null) a.supprimer();
                }

                // todo : re générer le détails
                this.gestionDetails();
            });

            items.add(item);
        }

        this.btnTypeHeure.getItems().addAll(items);
    }

    /**
     * Est-ce que le module possède une attribution à une catégorie d'heure hebdomadaire ou non
     */
    private boolean estModuleHebdo()
    {
        for (Attribution a : this.module.getAttributions()) if (a.getCatHr().estHebdo() && !a.isSupprime()) return true;

        return false;
    }

    private void initTableColumns() {

        /*ArrayList<String>      ensNomCatH = new ArrayList<>();

        for (CategorieHeure catHr : ensCatHrPresent)
                ensNomCatH.add(catHr.getNom());

        ArrayList<String> ensCatHHebdo    = new ArrayList<>();
        ArrayList<String> ensCatHNonHebdo = new ArrayList<>();
        for (String s : ensNomCatH) {
            if (Astre.rechercherCatHr(ensCatHrPresent, s).estHebdo())
                ensCatHHebdo.add(s);
            else
                ensCatHNonHebdo.add(s);
        }*/

        /* todo : ajout de colonne de catégorie d'heure
        for (CategorieHeure cat : ensCatHrPresent)
        {
            ajouterColonne(cat.getNom());
        }
        ajouterColonne("TO");*/

        tc.setCellValueFactory(cellData -> new SimpleStringProperty(getCellValue(cellData.getValue())));
        tc.setCellFactory(column -> Emoji.getCellFactory());

        tcIntervenant.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIntervenant().getNom() + " " + cellData.getValue().getIntervenant().getPrenom()));

        tcType.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTypeHeure()));
        tcType.setCellFactory(column -> new TableCellChoiceBoxModifiable<>() {
            @Override
            public ComboBox<CategorieHeure> createChoiceBox()
            {
                CategorieHeure c = this.getItem();
                ComboBox<CategorieHeure> choiceBox = super.createChoiceBox();
                ArrayList<CategorieHeure> lst = new ArrayList<>();
                for (CategorieHeure cat : StageSaisieRessource.this.module.getEnsCatHr())
                    if (c.estHebdo() == cat.estHebdo()) lst.add(cat);
                choiceBox.setItems(FXCollections.observableList( lst ));
                return choiceBox;
            }
        });
        tcType.setOnEditCommit(event -> {
            Affectation a = event.getRowValue();
            a.setTypeHeure(event.getNewValue());
            this.tableau.refresh();
            this.chargerCalculs();
        });

        tcNbH.setCellValueFactory(cellData -> cellData.getValue().hasNbHeure() ? new SimpleStringProperty (cellData.getValue().getNbHeure().toString()) : null);
        tcNbH.setCellFactory(column -> new TableCellTextFieldModifiable<>() {
            private final HashMap<TextField, Boolean> valid = new HashMap<>();

            @Override
            public TextField createTextField()
            {
                TextField textField = super.createTextField();
                Regex.activerRegex(Fraction.REGEX, Fraction.REGEX_CARAC, textField, valid, false);
                return textField;
            }

            @Override
            public boolean isTextFieldValid() { return Regex.estValide(valid); }
        });

        tcNbH.setOnEditCommit(event -> {
            Affectation a = event.getRowValue();
            a.setNbHeure(Fraction.valueOf(event.getNewValue()));
            this.tableau.refresh();
            this.chargerCalculs();
        });

        tcSemaine.setCellValueFactory(cellData -> cellData.getValue().hasGrpAndNbSemaine() ? new SimpleStringProperty (String.valueOf(cellData.getValue().getNbSemaine())) : null);
        tcSemaine.setCellFactory(column -> new TableCellTextFieldModifiable<>() {
            private final HashMap<TextField, Boolean> valid = new HashMap<>();

            @Override
            public TextField createTextField()
            {
                TextField textField = super.createTextField();
                Regex.activerRegex(Regex.REGEX_INT_NOT_EMPTY, Regex.REGEX_INT, textField, valid, false);
                return textField;
            }

            @Override
            public boolean isTextFieldValid() { return Regex.estValide(valid); }
        });

        tcSemaine.setOnEditCommit(event -> {
            Affectation a = event.getRowValue();
            a.setNbSemaine(Integer.parseInt(event.getNewValue()));
            this.tableau.refresh();
            this.chargerCalculs();
        });

        tcGrp.setCellValueFactory(cellData -> cellData.getValue().hasGrpAndNbSemaine() ? new SimpleStringProperty (String.valueOf(cellData.getValue().getNbGroupe())) : null);
        tcGrp.setCellFactory(column -> new TableCellTextFieldModifiable<>() {
            private final HashMap<TextField, Boolean> valid = new HashMap<>();

            @Override
            public TextField createTextField()
            {
                TextField textField = super.createTextField();
                Regex.activerRegex(Regex.REGEX_INT_NOT_EMPTY, Regex.REGEX_INT, textField, valid, false);
                return textField;
            }

            @Override
            public boolean isTextFieldValid() {
                return Regex.estValide(valid) && !this.getTableRow().getItem().getTypeHeure().getTypeGroupe().equalsIgnoreCase("CM");
            }
        });

        tcGrp.setOnEditCommit(event -> {
            Affectation a = event.getRowValue();
            a.setNbGroupe(Integer.parseInt(event.getNewValue()));
            this.tableau.refresh();
            this.chargerCalculs();
        });

        tcTotalEqtd.setCellValueFactory(cellData -> new SimpleStringProperty (Fraction.simplifyDouble(cellData.getValue().getTotalEqtd(),true)));


        tcCommentaire.setCellValueFactory(cellData -> new SimpleStringProperty (String.valueOf(cellData.getValue().getCommentaire())));
        tcCommentaire.setCellFactory(column -> new TableCellTextFieldModifiable<>() {
            private final HashMap<TextField, Boolean> valid = new HashMap<>();

            @Override
            public TextField createTextField()
            {
                TextField textField = super.createTextField();
                Regex.activerRegex(Regex.REGEX_ALL, Regex.REGEX_ALL, textField, valid, false);
                return textField;
            }

            @Override
            public boolean isTextFieldValid() { return Regex.estValide(valid); }
        });

        tcCommentaire.setOnEditCommit(event -> {
            Affectation a = event.getRowValue();
            a.setCommentaire(event.getNewValue());
            this.tableau.refresh();
        });
    }

    public void initPn(Module mod)
    {
        for (Attribution att : mod.getAttributions())
        {
            if (estTjrsPresent(att.getCatHr())) {
                ArrayList<TextField> lstTxtf = this.hmTxtPn.get(att.getCatHr().getNom().toUpperCase());
                if (lstTxtf != null) {
                    lstTxtf.get(0).setText(att.getNbHeurePN().toString());

                    String a = calculeNvValeur(Integer.parseInt(textOrDefault(this.hmTxtPn.get(att.getCatHr().getNom().toUpperCase()).get(0).getText())), att.getCatHr());
                    this.hmTxtPn.get(att.getCatHr().getNom().toUpperCase()).get(1).setText(a);
                }
            }
        }
        calculeTotaux();
    }

    public boolean estTjrsPresent(CategorieHeure catHr2)
    {
        for (CategorieHeure catHr: this.ensCatHrPresent)
            if(catHr.equals(catHr2)) return true;
        return false;
        
    }

    private void initRepartitionColumns()
    {
        this.gridPaneRepartition.getChildren().clear();
        for (CategorieHeure cat : ensCatHrPresent)
        {
            ajouterColonneRepartition(cat);
        }
    }
    public void initSemaine(Module mod)
    {
        for (Attribution att : mod.getAttributions())
        {
            if( att.getCatHr().estHebdo() && estTjrsPresent(att.getCatHr()))
            {
                this.hmTxtSemaine.get(att.getCatHr().getNom().toUpperCase()).get(0).setText("" + att.getNbSemaine());

                if(!att.getCatHr().estHebdo()) this.hmTxtSemaine.get(att.getCatHr().getNom().toUpperCase()).get(0).setText(att.getNbHeure().toString());
            }
        }
        majValeurSemaine(this.hmTxtSemaine);
    }

    private void initTabPan()
    {
        this.tabPaneSemaine.getTabs().clear();
        for (CategorieHeure cat : ensCatHrPresent) ajouterOnglet(cat);
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

                    if (index > -1 && (index < pnList.size() || txtf.getId().contains("Affecte")) )
                    {
                        int pnValue = 0;
                        if(!pnList.get(1).getText().isEmpty())
                            pnValue = Integer.parseInt(pnList.get(1).getText());

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
                        txtf.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
                    }
                }
                else
                {
                    if(Astre.rechercherCatHr(ensCatHrPresent,nom).estHebdo()) txtf.setStyle("");
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

    private String definirCode()
    {
        if (this.typeModule != null) {
            String debut    = this.typeModule.getPrefix();
            int numSemestre = this.semestre  .getNumero();

            for (int i = 1; i <= this.semestre.getModules().size() + 1; i++) {
                String code = String.format("%s%d.%02d", debut, numSemestre, i);

                if (!Controleur.get().getMetier().existeModule(this.semestre, code)) return code;
            }
        }

        return "";
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

    public void setCatH(ArrayList<CategorieHeure> lstCatValide)
    {
        this.ensCatHrPresent = lstCatValide;
    }
}