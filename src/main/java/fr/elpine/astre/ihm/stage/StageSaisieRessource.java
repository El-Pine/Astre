package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.Affectation;
import fr.elpine.astre.metier.objet.Intervenant;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
    public TableColumn<Affectation, String> tableauIntervenant;
    public TableColumn<Affectation, String> tableauType;
    public TableColumn<Affectation, Integer> tableauNbH;
    public TableColumn<Affectation, Double> tableauTotalEqtd;
    public TableColumn<Affectation, String> tableauCommentaire;
    public static ObservableList<Affectation> ensAff;
    private Stage stage;

    @FXML
    private TextField txtCode;

    public static Stage creer() throws IOException
    {
        Stage stage = new Stage();
        StageSaisieRessource.ensAff = FXCollections.observableArrayList(Controleur.get().getDb().getAllaff());

        FXMLLoader fxmlLoader = new FXMLLoader(StageSaisieRessource.class.getResource("saisieRessource.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1000, 660);

        StageSaisieRessource stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) stageCtrl.setStage(stage);

        stage.setTitle("Affectation");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            // perform actions before closing
            try { StagePrevisionnel.creer().show(); } catch (IOException ignored) {}
        });

        return stage;
    }

    private void setStage(Stage stage) { this.stage = stage; }

    @FXML
    protected void onBtnAjouter(ActionEvent e) throws IOException {
        this.desactiver();
        //TODO:pas oublier d'affciher la fenetre
    }

    @FXML
    protected void onBtnSupprimer(ActionEvent e) throws IOException {
        Affectation affectation = tableau.getSelectionModel().getSelectedItem();

        if(affectation != null) {
            tableau.getItems().remove(affectation);
            Controleur.get().getDb().supprimeraff(affectation);
        }
        else
            System.out.println("Pb objet pas supprimer");
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

        tableauIntervenant.setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getIdInter().getPrenom()));
        tableauType.setCellValueFactory        (cellData -> new SimpleStringProperty(cellData.getValue().getTypeHeure().getNom()));
        tableauNbH.setCellValueFactory         (cellData -> new SimpleIntegerProperty(cellData.getValue().getNbSemaine()).asObject());
        tableauTotalEqtd.setCellValueFactory   (cellData -> new SimpleDoubleProperty(cellData.getValue().getNbHeure()).asObject());
        tableauCommentaire.setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getCommentaire()));

        tableau.setItems(StageSaisieRessource.ensAff);
    }

    public void refresh() {
        StageSaisieRessource.ensAff = FXCollections.observableArrayList(Controleur.get().getDb().getAllaff());
        tableau.setItems(StageSaisieRessource.ensAff);
    }
}