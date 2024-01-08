package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.Affectation;
import fr.elpine.astre.metier.objet.CategorieHeure;
import fr.elpine.astre.metier.objet.Intervenant;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StageAjoutAffectation extends Stage implements Initializable
{
    @FXML
    private ComboBox<Intervenant>    cbIntervenant;
    @FXML
    private ComboBox<CategorieHeure> cbbCatHeure;

    @FXML
    private TextField             txtCommentaire;
    @FXML
    private TextField             txtNbHeure;
    @FXML
    private TextField             txtNbSemaine;
    @FXML
    private TextField             txtNbGp;

    @FXML
    private RadioButton           rbHP;
    @FXML
    private RadioButton           rbAutre;

    private Module module;
    private CategorieHeure catHp;

    public StageAjoutAffectation() //fxml -> ajoutAffectation
    {
        this.setTitle("Ajout Affectation");
    }

    @FXML
    private void onBtnAjouter()
    {
        Affectation aff = null;
        if(rbHP.isSelected() && cbIntervenant.getValue()!=null && !txtNbHeure.getText().isEmpty())
        {
            aff = new Affectation(this.module, cbIntervenant.getValue(),this.catHp, Fraction.valueOf(txtNbHeure.getText()), txtCommentaire.getText() );
            StageSaisieRessource.ensAff.add( aff );
        }
        else if (rbAutre.isSelected() && cbIntervenant.getValue()!=null && cbbCatHeure.getValue()!=null && !txtNbGp.getText().isEmpty() && !txtNbSemaine.getText().isEmpty())
        {
            aff = new Affectation(this.module, cbIntervenant.getValue(),cbbCatHeure.getValue(), Integer.parseInt(txtNbGp.getText()),Integer.parseInt(txtNbSemaine.getText()),txtCommentaire.getText());
            StageSaisieRessource.ensAff.add( aff );
        }

        this.close();
    }
    @FXML
    private void onBtnAnnuler() {
        this.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        cbIntervenant.setItems(FXCollections.observableList(Controleur.get().getMetier().getIntervenants()));

        ObservableList<CategorieHeure> lst = FXCollections.observableList( new ArrayList<>() );
        for (CategorieHeure catH : Controleur.get().getMetier().getCategorieHeures()) {
            if (catH.getNom().equals("HP")) {
                this.catHp = catH;
            } else {
                lst.add(catH);
            }
        }
        cbbCatHeure.setItems(lst);

        ToggleGroup toggleGroup = new ToggleGroup();

        rbHP.setToggleGroup(toggleGroup);
        rbAutre.setToggleGroup(toggleGroup);

        // Ajouter des ChangeListeners pour chaque RadioButton
        rbHP.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                rbAutre.setSelected(false); // Désélectionne l'autre bouton si celui-ci est sélectionné
                this.txtNbHeure.setDisable(false);
                this.cbbCatHeure.setDisable(true);
                this.txtNbSemaine.setText("");
                this.txtNbGp.setText("");
                this.txtNbSemaine.setDisable(true);
                this.txtNbGp.setDisable(true);
            }
        });

        rbAutre.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                rbHP.setSelected(false); // Désélectionne l'autre bouton si celui-ci est sélectionné
                this.txtNbHeure.setText("");
                this.txtNbHeure.setDisable(true);
                this.cbbCatHeure.setDisable(false);
                this.txtNbSemaine.setDisable(false);
                this.txtNbGp.setDisable(false);
            }
        });

        rbHP.setSelected(true);
        this.cbbCatHeure.setDisable(true);
        this.txtNbSemaine.setDisable(true);
        this.txtNbGp.setDisable(true);

        creerFormatter(this.txtNbGp);
        creerFormatter(this.txtNbHeure);
        creerFormatter(this.txtNbSemaine);
    }

    public void setModule(Module mod) { this.module = mod; }

    private void creerFormatter(TextField txtf) {
        txtf.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("^\\d+$")) {
                txtf.setStyle("");
                return change;
            } else if (change.getText().isEmpty()) {
                txtf.setStyle("");
                return change;
            } else {
                return null;
            }
        }));
    }

}
