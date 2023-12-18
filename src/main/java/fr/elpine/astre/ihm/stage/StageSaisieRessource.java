package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.metier.objet.Affectation;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private Stage stage;

    public static Stage creer() throws IOException
    {
        Stage stage = new Stage();

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
    protected void onBtnAjouter() throws IOException {
    }

    @FXML
    protected void onBtnSupprimer() throws IOException {
    }

    @FXML
    protected void onBtnDetail() {
        // A FAIRE
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
        tableauIntervenant.setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getIntervenant()));
        tableauType.setCellValueFactory        (cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        tableauNbH.setCellValueFactory         (cellData -> new SimpleIntegerProperty(cellData.getValue().getNbH()).asObject());
        tableauTotalEqtd.setCellValueFactory   (cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalEqtd()).asObject());
        tableauCommentaire.setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getCommentaire()));

        ObservableList<Affectation> elements = FXCollections.observableArrayList(
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 15, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 16, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 17, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 18, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc"),
                new Affectation("aaaaaa", "bbbbbb", 156, 10.2, "ccccc")
        );

        tableau.setItems(elements);
    }
}