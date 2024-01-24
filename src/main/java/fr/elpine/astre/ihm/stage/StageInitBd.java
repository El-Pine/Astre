package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.ihm.outil.Manager;
import fr.elpine.astre.ihm.outil.Regex;
import fr.elpine.astre.metier.DB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class StageInitBd extends Stage implements Initializable
{
    @FXML private TextField txtfPort;
    @FXML private TextField txtfId;
    @FXML private TextField txtfMdp;
    @FXML private TextField txtfBdd;
    @FXML private TextField txtfIp;

    private HashMap<TextField,Boolean> hmChampValider;


    public StageInitBd() // fxml -> "initDb"
    {
        this.setTitle("Config Base de données");
        this.setMinWidth(600);
        this.setMinHeight(400);
        this.setResizable(false);
    }


    @FXML private void onBtnValider()
    {
        if ( Regex.estValide(this.hmChampValider) ) {
            String ip          = txtfIp.getText();
            String port        = txtfPort.getText();
            String identifiant = txtfId.getText();
            String password    = txtfMdp.getText();
            String database    = txtfBdd.getText();

            boolean dbReloaded = Controleur.get().getDb().reloadDB(ip, Integer.parseInt(port), database, identifiant, password);

            if (dbReloaded) {
                this.close();

                if (this.getOwner() != null)
                    ((Stage) this.getOwner()).close();
                else {
                    Stage stage = Manager.creer("accueil");

                    stage.show();
                }
            } else
                PopUp.warning("Erreur de connexion", null, "Les informations entrées sont invalides, ou la base de données n'est pas accessible").showAndWait();
        } else
            PopUp.warning("Informations incorrectes", null, "Les informations entrées ne sont pas toutes valides").showAndWait();
    }

    @FXML private void onBtnAnnuler()
    {
        this.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

        String[] elements = DB.getInformations();

        if ( elements != null )
        {
            this.txtfIp.setText(elements[0]);
            this.txtfPort.setText(elements[1]);
            this.txtfBdd.setText(elements[2]);
            this.txtfId.setText(elements[3]);
        }

        this.hmChampValider = new HashMap<>();

        Regex.activerRegex(Regex.REGEX_INT_NOT_EMPTY, Regex.REGEX_INT, this.txtfPort, this.hmChampValider, false);
    }
}