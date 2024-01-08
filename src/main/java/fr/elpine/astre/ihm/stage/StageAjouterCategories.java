package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.metier.objet.CategorieHeure;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StageAjouterCategories extends Stage implements Initializable
{
    //private Stage stage;


    @FXML
    private TextField txtfCodeCatInter;
    @FXML
    private TextField txtfNomCatInter;
    @FXML
    private TextField txtfRatioCatInter;
    @FXML
    private TextField txtfNbHMCatInter;
    @FXML
    private TextField txtfNbHServCatInter;

    //private Label lblErreur;

    //private static StageAccueilConfig parent;

    @FXML
    private TextField txtfNomCatH;
    @FXML
    private TextField txtfEqtdCatH;
    @FXML
    private CheckBox cbRessourcesCatH;
    @FXML
    private CheckBox cbSaeCatH;
    @FXML
    private CheckBox cbPppCatH;
    @FXML
    private CheckBox cbStageCatH;


    public StageAjouterCategories() // fxml -> "ajouterCategories"
    {
        this.setTitle("Accueil Config");
        this.setMinWidth(600);
        this.setMinHeight(400);
        this.setResizable(false);
    }

    /*public static Stage creer( StageAccueilConfig parent ) throws IOException
    {
        Stage stage = new Stage();

        AstreApplication.refreshIcon(stage);

        StageAjouterCategories.parent = parent;

        FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("ajouterCategories.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        StageAjouterCategories stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) stageCtrl.setStage(stage);

        stage.setTitle("Accueil Config");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> parent.activer());

        return stage;
    }

    private void setStage(Stage stage) { this.stage = stage; }*/

    public void onBtnEnregistrerCatInter(ActionEvent actionEvent)
    {
        String code    =                     txtfCodeCatInter   .getText();
        String nom     =                     txtfNomCatInter    .getText();

        Fraction ratioTD = Fraction.valueOf( txtfRatioCatInter  .getText() );
        Fraction nbHM    = Fraction.valueOf( txtfNbHMCatInter   .getText() );
        Fraction nbHServ = Fraction.valueOf( txtfNbHServCatInter.getText() );

        if (code.isEmpty() || nom.isEmpty() || ratioTD == null || nbHM == null || nbHServ == null) {
            PopUp.warning("Champ Vide", null, "Erreur dans la saisie.").showAndWait();
        } else {
            new CategorieIntervenant(code, nom, nbHM, nbHServ, ratioTD);

            this.close();
        }
    }

    public void onBtnAnnulerCatInter(ActionEvent actionEvent) {
        //parent.activer();
        //parent.refresh();
        this.close();
    }

    public void onBtnEnregistrerCatH(ActionEvent actionEvent) {

        String nom         = txtfNomCatH.getText();
        boolean ressources = cbRessourcesCatH.isSelected();
        boolean c_sae      = cbSaeCatH.isSelected();
        boolean c_ppp      = cbPppCatH.isSelected();
        boolean c_stage    = cbStageCatH.isSelected();

        Fraction eqtd      = Fraction.valueOf( txtfEqtdCatH.getText() );

        if (eqtd == null)
            PopUp.warning("Champ Vide", null, "Erreur dans la saisie.").showAndWait();
        else {
            CategorieHeure cat = new CategorieHeure(nom, eqtd, ressources, c_sae, c_ppp, c_stage);

            this.close();
        }
    }


    public void onBtnAnnulerCatH(ActionEvent actionEvent) {
        //parent.activer();
        this.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

    }
}