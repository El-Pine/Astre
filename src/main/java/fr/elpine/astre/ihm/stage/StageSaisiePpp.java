package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.stage.PopUp.StagePopUp;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.objet.Module;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class StageSaisiePpp implements Initializable {
    @FXML
    public TableView<Affectation> tableau;
    @FXML
    private TableColumn<Intervenant,String> tc;
    @FXML
    public TableColumn<Affectation, String> tcIntervenant;
    @FXML
    public TableColumn<Affectation, String> tcType;
    @FXML
    public TableColumn<Affectation, Integer> tcNbH;
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
    private Stage stage;
    public static ArrayList<Affectation> affAAjouter;
    public static ArrayList<Affectation> affAEnlever;
    @FXML
    private TextField txtCode;
    private static Module module;

    public GridPane gridPn;
    public GridPane gridPaneRepartition;

    public TabPane tabPaneSemaine;

    private HashMap<String, ArrayList<TextField>> hmTxtPn;
    private HashMap<String, ArrayList<TextField>> hmTxtSemaine;
    private HashMap<String, ArrayList<TextField>> hmTxtRepartion;
    public static Stage creer(int semestre) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(StageSaisieRessource.class.getResource("saisieSae.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 700);

        Stage stage = new Stage();
        stage.setTitle("Affectation");
        stage.setScene(scene);

        AstreApplication.refreshIcon(stage);

        StageSaisiePpp stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) {
            stageCtrl.setStage(stage);
            stageCtrl.init("PPP", semestre);
        }

        stage.setOnCloseRequest(e -> {
            try {
                StagePrevisionnel.creer().show();
            } catch (IOException ignored) {
            }
        });

        stage.show();

        return stage;
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
        if (content instanceof AnchorPane) {
            extractTextFieldsFromAnchorPane((AnchorPane) content, textFields);
        }
    }

    private static void extractTextFieldsFromAnchorPane(AnchorPane anchorPane, ArrayList<TextField> textFields) {
        anchorPane.getChildren().forEach(node -> {
            if (node instanceof GridPane) {
                extractTextFieldsFromGridPane((GridPane) node, textFields);
            }
        });
    }

    private static void extractTextFieldsFromGridPane(GridPane gridPane, ArrayList<TextField> textFields) {
        gridPane.getChildren().forEach(node -> {
            if (node instanceof FlowPane) {
                extractTextFieldsFromFlowPane((FlowPane) node, textFields);
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
            if (node instanceof FlowPane) {
                FlowPane flowPane = (FlowPane) node;
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

    private void setStage(Stage stage) { this.stage = stage; }

    private void init(String nomMod, int id)
    {
        txtTypeModule.setText(nomMod);
        txtSemestre.setText("" + id);
        int code = Controleur.get().getMetier().rechercheSemestreByNumero(id).getModules().size() + 1;
        txtCode.setText("R" + id + "." + String.format("%02d", id));
        Semestre sem = Controleur.get().getMetier().rechercheSemestreByNumero(id); //TODO: Rajouter l'année une fois que l'on a géré

        txtNbEtd .setText("" + sem.getNbEtd  ());
        txtNbGpTD.setText("" + sem.getNbGrpTD());
        txtnbGpTP.setText("" + sem.getNbGrpTP());
    }

    @FXML
    protected void onBtnAjouter(ActionEvent e) throws IOException {
        this.desactiver();
        StageAjoutPpp.creer(StageSaisiePpp.module ,this ).show();
    }

    @FXML
    protected void onBtnSupprimer(ActionEvent e) throws IOException {
        Affectation affectation = tableau.getSelectionModel().getSelectedItem();

        if(affectation != null && StagePopUp.PopUpConfirmation("Suppresion d'une ressource", "Etes-vous sûr de supprimer cette ressource ?")) {
            tableau.getItems().remove(affectation);
            Controleur.get().getDb().supprimeraff(affectation);
            this.refresh();
        }
    }

    public void desactiver()
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
    }

    @FXML
    protected void onBtnEnregistrer() throws IOException, SQLException {
        for (Affectation aff : StageSaisieRessource.affAAjouter) {
            Controleur.get().getMetier().ajouterAffectation(aff);
        }
        for (Affectation aff : StageSaisieRessource.affAAjouter) {
            Controleur.get().getMetier().ajouterAffectation(aff);
        }

        Controleur.get().getDb().enregistrer();
        stage.close();
        StagePrincipal.creer().show();
    }

    @FXML
    protected void onBtnAnnuler() throws IOException, SQLException {
        StageSaisieRessource.affAAjouter = StageSaisieRessource.affAEnlever = new ArrayList<Affectation>();
        stage.close();
        StagePrincipal.creer().show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.hmTxtPn = new HashMap<String,ArrayList<TextField>>();
        StageSaisiePpp.module = new Module(txtLibelleLong.getText(), txtCode.getText(), txtLibelleCourt.getText(), txtTypeModule.getText(), Color.rgb(255,255,255), cbValidation.isSelected(), Controleur.get().getMetier().getSemestres().get(parseInt(txtSemestre.getText())));

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
        tcIntervenant.setCellValueFactory (cellData -> new SimpleStringProperty (cellData.getValue().getIntervenant().getNom()));
        tcType       .setCellValueFactory (cellData -> new SimpleStringProperty (cellData.getValue().getTypeHeure  ().getNom()));
        tcGrp        .setCellValueFactory (cellData -> new SimpleIntegerProperty(cellData.getValue().getNbGroupe   ()).asObject());
        tcNbH        .setCellValueFactory (cellData -> new SimpleIntegerProperty(cellData.getValue().getNbSemaine  ()).asObject());
        tcTotalEqtd  .setCellValueFactory (cellData -> new SimpleIntegerProperty(cellData.getValue().getNbHeure    ()).asObject());
        tcCommentaire.setCellValueFactory (cellData -> new SimpleStringProperty (cellData.getValue().getCommentaire()));

        tableau.setItems(StageSaisieRessource.ensAff);


        this.hmTxtPn = initHmPn(getAllTextFieldsPn(gridPn));

        this.hmTxtPn.forEach((key,value) -> {
            for (TextField txt: value)
            {
                if(txt.isEditable())
                {
                    ajouterListener(txt);
                }
            }
        });



        this.hmTxtSemaine = initHmPn(extractTextFields(tabPaneSemaine));

        System.out.println(hmTxtSemaine);

        this.hmTxtSemaine.forEach((key,value) -> {
            for (TextField txt: value)
            {
                System.out.println("je suis la aussi ");
                ajouterListenerSemaine(txt);
            }
        });

        this.hmTxtRepartion = initHmPn(getAllTextFieldsPn(gridPaneRepartition));

    }

    /*-----------------------------*/
    /*    Ajout des Listenner      */
    /*-----------------------------*/

    private void ajouterListener(TextField txt)
    {
        txt.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                valeurChangerPn(txt, newValue);
            }
        });
    }

    private void ajouterListenerSemaine(TextField txt)
    {
        System.out.println("je suis la");
        txt.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                valeurChangerSemaine(txt, newValue);
            }
        });
    }

    private void valeurChangerSemaine(TextField txt, String newValue)
    {
        String keyCatHr = txt.getId().substring(3,5);
        String keyCatHrMAJ = txt.getId().substring(3,5).toUpperCase();

        CategorieHeure catHr = Astre.rechercherCatHr(Controleur.get().getMetier().getCategorieHeures(), keyCatHrMAJ);

        int valeurSemaine = 0;
        int valeurFinal   = 0;

        if(!(this.hmTxtSemaine.get(keyCatHrMAJ).get(0).getText().equals("")) && !(this.hmTxtSemaine.get(keyCatHrMAJ).get(1).getText().equals("")) )
        {
            valeurSemaine = Integer.parseInt(this.hmTxtSemaine.get(keyCatHrMAJ).get(0).getText()) * Integer.parseInt(this.hmTxtSemaine.get(keyCatHrMAJ).get(1).getText());
        }
        for (TextField txt1 : this.hmTxtRepartion.get(keyCatHrMAJ))
        {
            System.out.println(txt1);
            if(txt1.getId().equals("txt" + keyCatHr +"Repartition"))
            {
                txt1.setText(Integer.toString(valeurSemaine));
            }

            if(txt1.getId().equals("txt" + keyCatHr + "PromoRepartion"))
            {
                if(catHr.getNom().equals("TP") || catHr.getNom().equals("TD"))
                {
                    int valeurXgroupe = valeurSemaine * (catHr.getNom().equals("TP") ? Integer.parseInt(this.txtnbGpTP.getText()) : Integer.parseInt(this.txtNbGpTD.getText()));
                    valeurFinal   = (int) (valeurXgroupe * catHr.getEquivalentTD());
                }
                else
                {
                    valeurFinal = (int) (valeurSemaine * catHr.getEquivalentTD());
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
                if(txt.getText() != "")
                {
                    int valeurInitial = Integer.parseInt(txt.getText());
                    txt1.setText(calculeNvValeur(valeurInitial, Controleur.get().getDb().getCatHrByNom(keyCatHr)));
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
                System.out.println("bah ? je ne rentre pas ici ? ");
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
                    if (txt.getId().equals("txtTotPn"))
                        txtTotPn = txt;
                    else
                        txtTotPromoPn = txt;
                }
            }
        }
        setValeurTxtTot(txtTotPn, total, txtTotPromoPn, totalPromo);
    }

    private void setValeurTxtTot(TextField txt1, int total, TextField txt2, int totalPromo) {
        System.out.println("je rentre ici non ?");
        System.out.println("total : " + total + " totalPromo : " + totalPromo);

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
            int valeurFinal   = (int) (valeurXgroupe * catHr.getEquivalentTD());
            return Integer.toString(valeurFinal);
        }
        else
        {
            System.out.println();
            int valeurFinal = (int) (valeurInitial * catHr.getEquivalentTD());
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

    private String getCellValue(Intervenant intervenant) {
        if (StageIntervenant.interAAjouter.contains(intervenant)) {
            return "➕";
        } else if (StageIntervenant.interAEnlever.contains(intervenant)) {
            return "❌";
        } else {
            return "";
        }
    }

    public void refresh() {
        StageSaisieRessource.ensAff = FXCollections.observableArrayList(Controleur.get().getMetier().getAffectations());
        tableau.setItems(StageSaisieRessource.ensAff);
    }
}
