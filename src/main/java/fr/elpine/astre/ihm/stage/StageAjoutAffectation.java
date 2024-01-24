package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.ihm.outil.Regex;
import fr.elpine.astre.metier.objet.Affectation;
import fr.elpine.astre.metier.objet.CategorieHeure;
import fr.elpine.astre.metier.objet.Intervenant;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class StageAjoutAffectation extends Stage implements Initializable
{
    @FXML private ComboBox<Intervenant>    cbIntervenant;
    @FXML private ComboBox<CategorieHeure> cbbCatHeure;

    @FXML private TextField             txtCommentaire;
    @FXML private TextField             txtNbHeure;
    @FXML private TextField             txtNbSemaine;
    @FXML private TextField             txtNbGp;

    private Module module;

    private HashMap<TextField, Boolean> validHebdo;
    private HashMap<TextField, Boolean> valid;


    public StageAjoutAffectation() //fxml -> ajoutAffectation
    {
        this.setTitle("Ajout Affectation");
        this.setMinWidth(600);
        this.setMinHeight(350);
        this.setResizable(false);
    }

    @FXML private void onBtnAjouter()
    {
        if (cbbCatHeure.getValue().estHebdo())
        {
            if (Regex.estValide(validHebdo)) {
                new Affectation(
                        this.module,
                        cbIntervenant.getValue(),
                        cbbCatHeure.getValue(),
                        Integer.parseInt(txtNbGp.getText()),
                        Integer.parseInt(txtNbSemaine.getText()),
                        txtCommentaire.getText()
                );

                this.close();
            }
            else
                PopUp.warning("Informations incorrectes", null, "Les informations entrées ne sont pas toutes valides").showAndWait();
        } else {
            if (Regex.estValide(valid)) {
                new Affectation(
                        this.module,
                        cbIntervenant.getValue(),
                        cbbCatHeure.getValue(),
                        Fraction.valueOf(txtNbHeure.getText()),
                        txtCommentaire.getText()
                );

                this.close();
            }
            else
                PopUp.warning("Informations incorrectes", null, "Les informations entrées ne sont pas toutes valides").showAndWait();
        }
    }
    @FXML private void onBtnAnnuler() {
        this.close();
    }

    public void init()
    {
        this.valid      = new HashMap<>();
        this.validHebdo = new HashMap<>();

        cbIntervenant.setItems(FXCollections.observableList(Controleur.get().getMetier().getIntervenants()));
        cbIntervenant.setValue(cbIntervenant.getItems().get(0));

        cbbCatHeure.setItems(FXCollections.observableList(this.module.getEnsCatHr()));
        cbbCatHeure.setValue(cbbCatHeure.getItems().get(0));

        cbbCatHeure.setOnAction((observable) -> changementFocus());

        changementFocus();

        Regex.activerRegex(Regex.REGEX_INT_NOT_EMPTY, Regex.REGEX_INT, this.txtNbGp,        validHebdo, false);
        Regex.activerRegex(Regex.REGEX_INT_NOT_EMPTY, Regex.REGEX_INT, this.txtNbSemaine,   validHebdo, false);
        Regex.activerRegex(Fraction.REGEX, Fraction.REGEX_CARAC, this.txtNbHeure, valid,      false);
    }

    private void changementFocus()
    {
        if (cbbCatHeure.getValue() != null) {
            if (cbbCatHeure.getValue().estHebdo()) {
                if (cbbCatHeure.getValue().getTypeGroupe().equalsIgnoreCase("CM"))
                {
                    this.txtNbGp.setDisable(true);
                    this.txtNbGp.setText("1");
                } else this.txtNbGp.setDisable(false);

                this.txtNbSemaine.setDisable(false);
                this.txtNbHeure.setText("");
                this.txtNbHeure.setDisable(true);
            } else {
                this.txtNbGp.setDisable(true);
                this.txtNbGp.setText("");
                this.txtNbSemaine.setDisable(true);
                this.txtNbSemaine.setText("");
                this.txtNbHeure.setDisable(false);
            }
        }
    }

    public void setModule(Module mod) { this.module = mod; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );
    }
}
