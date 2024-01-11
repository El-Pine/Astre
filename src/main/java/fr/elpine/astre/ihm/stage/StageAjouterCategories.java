package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.metier.objet.CategorieHeure;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class StageAjouterCategories extends Stage implements Initializable
{
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
    @FXML
    private CheckBox          cbHebdo;
    @FXML
    private ChoiceBox<String> cbbTypeGroupe;


    public StageAjouterCategories() // fxml -> "ajouterCategorieHeure" / "ajouterCategorieInter"
    {
        this.setTitle("Ajout d'une catégorie");
        this.setMinWidth(600);
        this.setMinHeight(350);
        this.setResizable(false);
    }

    public void onBtnEnregistrerCatInter()
    {
        String code    =                     txtfCodeCatInter   .getText();
        String nom     =                     txtfNomCatInter    .getText();

        Fraction ratioTD = Fraction.valueOf( txtfRatioCatInter  .getText() );
        Fraction nbHM    = Fraction.valueOf( txtfNbHMCatInter   .getText() );
        Fraction nbHServ = Fraction.valueOf( txtfNbHServCatInter.getText() );

        if (code.isEmpty() || nom.isEmpty() || ratioTD == null || nbHM == null || nbHServ == null) {
            PopUp.warning("Champ invalide", null, "Erreur dans la saisie, certains champs ne sont pas valide !").showAndWait();
        } else if (Controleur.get().getMetier().existeCatInter(code))
            PopUp.warning("Code indisponible", null, "Une catégorie d'intervenant portant le code '%s' existe déjà !".formatted(code)).showAndWait();
        else {
            new CategorieIntervenant(code, nom, nbHM, nbHServ, ratioTD);

            this.close();
        }
    }

    public void onBtnAnnulerCatInter() {
        this.close();
    }

    public void onBtnEnregistrerCatH() {

        String nom         = txtfNomCatH.getText();
        boolean ressources = cbRessourcesCatH.isSelected();
        boolean c_sae      = cbSaeCatH.isSelected();
        boolean c_ppp      = cbPppCatH.isSelected();
        boolean c_stage    = cbStageCatH.isSelected();
        boolean c_hebdo    = cbHebdo.isSelected();
        String  c_typeGroupe = cbbTypeGroupe.getSelectionModel().getSelectedItem();
        Fraction eqtd      = Fraction.valueOf( txtfEqtdCatH.getText() );

        if (eqtd == null)
            PopUp.warning("Champ incorrect", null, "Le champ Equivalent TD doit être renseigné correctement !").showAndWait();
        else if (nom.isEmpty())
            PopUp.warning("Champ vide", null, "Vous devez préciser un nom !").showAndWait();
        else if (Controleur.get().getMetier().existeCatHeure(nom))
            PopUp.warning("Non indisponible", null, "Une catégorie d'heure portant le nom '%s' existe déjà !".formatted(nom)).showAndWait();
        else {
            new CategorieHeure(nom, eqtd, ressources, c_sae, c_ppp, c_stage, c_hebdo, c_typeGroupe);

            this.close();
        }
    }


    public void onBtnAnnulerCatH() {
        this.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

        if (cbbTypeGroupe != null) {
            cbbTypeGroupe.setItems(FXCollections.observableList(new ArrayList<>(Arrays.asList("TD", "TP", "CM"))));
            cbbTypeGroupe.setValue("TD");
        }
    }
}