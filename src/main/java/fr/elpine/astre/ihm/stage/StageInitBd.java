package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StageInitBd implements Initializable
{
    
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
        if (stageCtrl != null) stageCtrl.setStage(stage);

        stage.setTitle("Config Base de données");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            if ( parent != null )
                parent.activer();
        });

        return stage;
    }

    private void setStage(Stage stage) { this.stage = stage; }

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
    }

    public void onBtnAnnuler(ActionEvent actionEvent) throws IOException
    {
        if ( parent != null ) parent.activer();
        else stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        String[] elements = Controleur.get().getDb().getInformations();

        if ( elements != null )
        {
            this.txtfIp.setText(elements[0]);
            this.txtfPort.setText(elements[1]);
            this.txtfBdd.setText(elements[2]);
            this.txtfId.setText(elements[3]);
        }
    }
}