package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.ihm.outil.Regex;
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
import java.util.HashMap;
import java.util.ResourceBundle;

public class StageAjouterCategories extends Stage implements Initializable
{
    @FXML private TextField txtfCodeCatInter;
    @FXML private TextField txtfNomCatInter;
    @FXML private TextField txtfRatioCatInter;
    @FXML private TextField txtfNbHServCatInter;
    @FXML private TextField txtfNbHMCatInter;

    @FXML private TextField txtfNomCatH;
    @FXML private TextField txtfEqtdCatH;
    @FXML private CheckBox cbRessourcesCatH;
    @FXML private CheckBox cbSaeCatH;
    @FXML private CheckBox cbPppCatH;
    @FXML private CheckBox cbStageCatH;
    @FXML private CheckBox          cbHebdoCatH;
    @FXML private ChoiceBox<String> cbbTypeGroupeCatH;

    private HashMap<TextField,Boolean> hmChampValider;


    public StageAjouterCategories() // fxml -> "ajouterCategorieHeure" / "ajouterCategorieInter"
    {
        this.setTitle("Ajout d'une catégorie");
        this.setMinWidth(600);
        this.setMinHeight(350);
        this.setResizable(false);
    }


    @FXML private void onBtnEnregistrerCatInter()
    {
        if ( Regex.estValide(this.hmChampValider) )
        {
            String code = txtfCodeCatInter.getText();

            if ( Controleur.get().getMetier().existeCatInter(code) )
                PopUp.warning("Code indisponible", null, "Une catégorie d'intervenant portant le code '%s' existe déjà".formatted(code)).showAndWait();
            else {
                Fraction hSer = Fraction.valueOf(txtfNbHServCatInter.getText());
                Fraction hMax = Fraction.valueOf(txtfNbHMCatInter.getText());

                if (hMax.value() >= hSer.value()) {
                    new CategorieIntervenant(
                            code,
                            txtfNomCatInter.getText(),
                            hMax,
                            hSer,
                            Fraction.valueOf(txtfRatioCatInter.getText())
                    );

                    this.close();
                }
                else
                    PopUp.warning("Informations incorrectes", null, "Le nombre d'heure de service doit être inférieur au nombre d'heure maximal").showAndWait();
            }
        }
        else
            PopUp.warning("Informations incorrectes", null, "Les informations entrées ne sont pas toutes valides").showAndWait();
    }

    @FXML private void onBtnAnnulerCatInter() {
        this.close();
    }

    @FXML private void onBtnEnregistrerCatH()
    {
        if ( Regex.estValide(this.hmChampValider) )
        {
            String nom = txtfNomCatH.getText();

            if ( Controleur.get().getMetier().existeCatHeure(nom) )
                PopUp.warning("Non indisponible", null, "Une catégorie d'heure portant le nom '%s' existe déjà".formatted(nom)).showAndWait();
            else {
                new CategorieHeure(
                        nom,
                        Fraction.valueOf( txtfEqtdCatH.getText() ),
                        cbRessourcesCatH.isSelected(),
                        cbSaeCatH.isSelected(),
                        cbPppCatH.isSelected(),
                        cbStageCatH.isSelected(),
                        cbHebdoCatH.isSelected(),
                        cbbTypeGroupeCatH.getSelectionModel().getSelectedItem()
                );

                this.close();
            }
        }
        else
            PopUp.warning("Informations incorrectes", null, "Les informations entrées ne sont pas toutes valides").showAndWait();
    }


    @FXML private void onBtnAnnulerCatH() {
        this.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

        this.hmChampValider = new HashMap<>();

        if (this.txtfCodeCatInter != null) { // Catégorie d'intervenants
            Regex.activerRegex(Regex.REGEX_ALL_NOT_EMPTY, Regex.REGEX_ALL, this.txtfCodeCatInter, this.hmChampValider, false);
            Regex.activerRegex(Regex.REGEX_ALL_NOT_EMPTY, Regex.REGEX_ALL, this.txtfNomCatInter, this.hmChampValider, false);
            Regex.activerRegex(Fraction.REGEX, Fraction.REGEX_CARAC, this.txtfRatioCatInter, this.hmChampValider, false);
            Regex.activerRegex(Fraction.REGEX, Fraction.REGEX_CARAC, this.txtfNbHServCatInter, this.hmChampValider, false);
            Regex.activerRegex(Fraction.REGEX, Fraction.REGEX_CARAC, this.txtfNbHMCatInter, this.hmChampValider, false);
        } else { // Catégorie d'heures
            Regex.activerRegex(Regex.REGEX_NOM, Regex.REGEX_NOM_CARAC, this.txtfNomCatH, this.hmChampValider, false);
            Regex.activerRegex(Fraction.REGEX, Fraction.REGEX_CARAC, this.txtfEqtdCatH, this.hmChampValider, false);
        }

        if (cbbTypeGroupeCatH != null) {
            cbbTypeGroupeCatH.setItems(FXCollections.observableList(new ArrayList<>(Arrays.asList("TD", "TP", "CM"))));
            cbbTypeGroupeCatH.setValue("TD");
        }
    }
}