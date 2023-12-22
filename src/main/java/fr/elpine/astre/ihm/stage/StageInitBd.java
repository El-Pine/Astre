package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.stage.PopUp.StagePopUp;
import fr.elpine.astre.metier.DB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StageInitBd implements Initializable
{
    private ArrayList<TextField> textFields;
    @FXML
    public TextField txtfPort;
    @FXML
    public TextField txtfId;
    @FXML
    public TextField txtfMdp;
    @FXML
    public TextField txtfBdd;
    @FXML
    public TextField txtfIp;

    private Stage stage;
    private static StageAccueilConfig parent;

    public static Stage creer( StageAccueilConfig parent ) throws IOException
    {
        Stage stage = new Stage();
        StageInitBd.parent = parent;

        FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("initDb.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        StageInitBd stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) {
            stageCtrl.setStage(stage);
            stageCtrl.addTxtField();
        }

        stage.setTitle("Config Base de données");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            if ( parent != null )
                parent.activer();
        });

        return stage;
    }

    private void setStage(Stage stage) { this.stage = stage; }

    private void addTxtField()
    {
        this.textFields = new ArrayList<TextField>();
        this.textFields.add(this.txtfIp);
        this.textFields.add(this.txtfPort);
        this.textFields.add(this.txtfId);
        this.textFields.add(this.txtfMdp);
        this.textFields.add(this.txtfBdd);
    }

    public void onBtnValider(ActionEvent actionEvent)
    {
        String ip          = txtfIp   .getText();
        String port        = txtfPort .getText();
        String identifiant = txtfId   .getText();
        String password    = txtfMdp  .getText();
        String database    = txtfBdd  .getText();

        boolean dbReloaded = Controleur.get().getDb().reloadDB( ip, Integer.parseInt(port), database, identifiant, password );

        if (dbReloaded)
        {
            stage.close();

            if ( parent != null ) parent.activer();
            else
            {
                try {
                    StagePrincipal.creer().show();
                } catch (IOException e) { throw new RuntimeException(e); }
            }
        }
        else
            StagePopUp.PopUpErreur("Erreur de connection", "Les informations entrées sont invalides, ou la base de données n'est pas accessible");
    }

    public void onBtnAnnuler(ActionEvent actionEvent) throws IOException
    {
        if ( parent != null ) parent.activer();
        else stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        String[] elements = DB.getInformations();

        if ( elements != null )
        {
            this.txtfIp.setText(elements[0]);
            this.txtfPort.setText(elements[1]);
            this.txtfBdd.setText(elements[2]);
            this.txtfId.setText(elements[3]);
        }
    }

    @FXML
    private void handleTabKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            TextField sourceField = (TextField) event.getSource();
            boolean shiftPressed = event.isShiftDown();
            event.consume();

            int currentIndex = textFields.indexOf(sourceField);
            if (!shiftPressed) {
                focusField(textFields.get((currentIndex + 1) % textFields.size()));
            } else {
                focusField(textFields.get((currentIndex - 1 + textFields.size()) % textFields.size()));
            }
        }
    }

    private void focusField(TextField field) {
        field.requestFocus();
    }
}