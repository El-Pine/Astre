package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.CategorieHeure;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StageAjouterCategories
{
    private Stage stage;
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

    public void onBtnEnregistrerCatInter(ActionEvent actionEvent) {
        parent.activer();
        stage.close();
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

        System.out.println(nom + " "+ eqtd + " " + ressources + " " + c_sae + " " + c_ppp + " " + c_stage);

        // create new CategorieHeure
        CategorieHeure cat = new CategorieHeure(nom, eqtd, ressources, c_sae, c_ppp, c_stage);
        // add to DB
        Controleur.get().getDb().ajouterCategorieHeure(cat);

        parent.activer();
        stage.close();
    }

    public void onBtnAnnulerCatH(ActionEvent actionEvent) {
        parent.activer();
        stage.close();
    }
}