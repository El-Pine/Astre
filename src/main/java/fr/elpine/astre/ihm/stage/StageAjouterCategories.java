package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;

import fr.elpine.astre.metier.objet.CategorieHeure;
import fr.elpine.astre.metier.objet.CategorieIntervenant;

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

public class StageAjouterCategories
{
    private Stage stage;


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

    static StageAccueilConfig parent;

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

    public static Stage creer( StageAccueilConfig parent ) throws IOException
    {
        Stage stage = new Stage();
        StageAjouterCategories.parent = parent;

        FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("ajouterCategories.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        StageAjouterCategories stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) stageCtrl.setStage(stage);

        stage.setTitle("Accueil Config");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            parent.activer();
        });

        return stage;
    }

    private void setStage(Stage stage) { this.stage = stage; }

    public void onBtnEnregistrerCatInter(ActionEvent actionEvent)
    {
        String code    =                     txtfCodeCatInter   .getText();
        String nom     =                     txtfNomCatInter    .getText();
        double ratioTD = Double .parseDouble(txtfRatioCatInter  .getText());
        int    nbHM    = Integer.parseInt   (txtfNbHMCatInter   .getText());
        int    nbHServ = Integer.parseInt   (txtfNbHServCatInter.getText());

        if(estValide(code,nom))
        {
            Controleur.get().getDb().ajouterCategorieIntervenant(new CategorieIntervenant(code, nom, nbHM, nbHServ, ratioTD));
        }
        else
            //lblErreur.setText("Les champs code et nom doivent être remplis");

        parent.activer();
        stage.close();
    }


    public boolean estValide(String code, String nom)
    {
        if(code == null  && nom == null)
            return false;
        return true;
    }

    public void onBtnAnnulerCatInter(ActionEvent actionEvent) {
        parent.activer();
        stage.close();
    }

    public void onBtnEnregistrerCatH(ActionEvent actionEvent) {

        String nom = txtfNomCatH.getText();
        double eqtd = Double.parseDouble(txtfEqtdCatH.getText());
        boolean ressources = cbRessourcesCatH.isSelected();
        boolean c_sae = cbSaeCatH.isSelected();
        boolean c_ppp = cbPppCatH.isSelected();
        boolean c_stage = cbStageCatH.isSelected();

        CategorieHeure cat = new CategorieHeure(nom, eqtd, ressources, c_sae, c_ppp, c_stage);

        Controleur.get().getDb().ajouterCategorieHeure(cat);

        parent.activer();
        stage.close();
    }


    public void onBtnAnnulerCatH(ActionEvent actionEvent) {
        parent.activer();
        stage.close();
    }


}