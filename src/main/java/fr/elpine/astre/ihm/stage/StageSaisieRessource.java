package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.Affectation;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StageSaisieRessource implements Initializable
{
    public TableView<Affectation> tableau;
    public TableColumn<Affectation, String> tcIntervenant;
    public TableColumn<Affectation, String> tcType;
    public TableColumn<Affectation, Integer> tcNbH;
    public TableColumn<Affectation, Integer> tcGrp;
    public TableColumn<Affectation, String> tcCommentaire;
    public TableColumn<Affectation, Integer> tcTotalEqtd;
    public static ObservableList<Affectation> ensAff;
    public TextField txtTypeModule;
    public CheckBox cbValidation;
    public TextField txtnbGpTP;
    public TextField txtNbGpTD;
    public TextField txtNbEtd;
    public TextField txtLibelleLong;
    public TextField txtLibelleCourt;
    public TextField txtSemestre;
    private Stage stage;

    @FXML
    private TextField txtCode;

    public static Stage creer(String nomMod, int id) throws IOException
    {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(StageSaisieRessource.class.getResource("saisieRessource.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1000, 660);

        StageSaisieRessource stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) {
            stageCtrl.setStage(stage);
            stageCtrl.init(nomMod,id);
        }

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

        Semestre sem = Controleur.get().getDb().getSemestreById(id,"2022-2023");

        txtNbEtd .setText("" + sem.getNbEtd  ());
        txtNbGpTD.setText("" + sem.getNbGrpTD());
        txtnbGpTP.setText("" + sem.getNbGrpTP());
    }

    @FXML
    protected void onBtnAjouter(ActionEvent e) throws IOException {
        this.desactiver();
        //TODO:pas oublier d'affciher la fenetre
    }

    @FXML
    protected void onBtnSupprimer(ActionEvent e) throws IOException {
        /*Affectation affectation = tableau.getSelectionModel().getSelectedItem();

        if(affectation != null) {
            tableau.getItems().remove(affectation);
            Controleur.get().getDb().supprimeraff(affectation);
        }
        else
            System.out.println("Pb objet pas supprimer");*/
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
    protected void onBtnEnregistrer() throws IOException {
        stage.close();
        StagePrevisionnel.creer().show();
    }

    @FXML
    protected void onBtnAnnuler() throws IOException {
        stage.close();
        StagePrevisionnel.creer().show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        tcIntervenant.setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getIntervenant().getNom()));
        tcType       .setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getTypeHeure().getNom()));
        tcGrp        .setCellValueFactory (cellData -> new SimpleIntegerProperty(cellData.getValue().getNbGroupe()).asObject());
        tcNbH        .setCellValueFactory (cellData -> new SimpleIntegerProperty(cellData.getValue().getNbSemaine()).asObject());
        tcTotalEqtd  .setCellValueFactory (cellData -> new SimpleIntegerProperty(cellData.getValue().getNbHeure()).asObject());
        tcCommentaire.setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getCommentaire()));

        StageSaisieRessource.ensAff = FXCollections.observableArrayList(Controleur.get().getMetier().getAllAffectation());
        tableau.setItems(StageSaisieRessource.ensAff);


    }

    public void refresh() {
        StageSaisieRessource.ensAff = FXCollections.observableArrayList(Controleur.get().getMetier().getAllAffectation());
        tableau.setItems(StageSaisieRessource.ensAff);
    }

    public void onBtnDetail(ActionEvent actionEvent) {
    }
}