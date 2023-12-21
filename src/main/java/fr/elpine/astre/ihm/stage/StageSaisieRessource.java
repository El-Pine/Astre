package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.stage.PopUp.StagePopUp;
import fr.elpine.astre.metier.objet.Affectation;
import fr.elpine.astre.metier.objet.Intervenant;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.objet.Semestre;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Integer.*;

public class StageSaisieRessource implements Initializable
{
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
    public static TextField txtTypeModule;
    @FXML
    public static CheckBox cbValidation;
    @FXML
    public TextField txtnbGpTP;
    @FXML
    public TextField txtNbGpTD;
    @FXML
    public TextField txtNbEtd;
    @FXML
    public static TextField txtLibelleLong;
    @FXML
    public static TextField txtLibelleCourt;
    @FXML
    public static TextField txtSemestre;
    private Stage stage;
    public static ArrayList<Affectation> affAAjouter;
    public static ArrayList<Affectation> affAEnlever;
    @FXML
    private static TextField txtCode;

    private static Module module;

    public static Stage creer(int semestre) throws IOException
    {
        Stage stage = new Stage();

        StageSaisieRessource.ensAff = FXCollections.observableArrayList(Controleur.get().getMetier().getAffectations());
        StageSaisieRessource.affAAjouter = new ArrayList<Affectation>();
        StageSaisieRessource.affAEnlever = new ArrayList<Affectation>();

        FXMLLoader fxmlLoader = new FXMLLoader(StageSaisieRessource.class.getResource("saisieRessource.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1000, 660);

        StageSaisieRessource stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) {
            stageCtrl.setStage(stage);
            stageCtrl.init("Ressource", semestre);
        }

        StageSaisieRessource.module = new Module(txtLibelleLong.getText(), txtCode.getText(), txtLibelleCourt.getText(), txtTypeModule.getText(), Color.rgb(255,255,255), cbValidation.isSelected(), Controleur.get().getMetier().getSemestres().get(parseInt(txtSemestre.getText())));

        stage.setTitle("Affectation");
        stage.setScene(scene);


        stage.setOnCloseRequest(e -> {
            try { StagePrevisionnel.creer().show(); } catch (IOException ignored) {}
        });

        return stage;
    }

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
        StageAjoutRessource.creer(StageSaisieRessource.module ,this ).show();
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