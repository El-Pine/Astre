package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StageInitBd
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
        String ip    = txtfIp   .getText();
        String port  = txtfPort .getText();
        String id    = txtfId   .getText();
        String mdp   = txtfMdp  .getText();
        String bdd   = txtfBdd  .getText();

        boolean dbReloaded = Controleur.get().getDb().reloadDb(ip,port,id,mdp,bdd);

        if (dbReloaded) {
            stage.close();
            if (parent != null)
                parent.activer();
            else {
                try {
                    StagePrincipal.creer().show();
                } catch (IOException e) { throw new RuntimeException(e); }
            }
        }
        else
        {
            System.out.println("erreur");
        }
    }

    public void onBtnAnnuler(ActionEvent actionEvent) throws IOException {
        stage.close();
        if (parent != null)
            parent.activer();
        else
            StagePrincipal.creer().show();
    }
}