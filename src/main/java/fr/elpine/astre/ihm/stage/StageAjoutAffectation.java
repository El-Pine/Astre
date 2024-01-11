package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.Affectation;
import fr.elpine.astre.metier.objet.CategorieHeure;
import fr.elpine.astre.metier.objet.Intervenant;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class StageAjoutAffectation extends Stage
{
    @FXML private ComboBox<Intervenant>    cbIntervenant;
    @FXML private ComboBox<CategorieHeure> cbbCatHeure;

    @FXML private TextField             txtCommentaire;
    @FXML private TextField             txtNbHeure;
    @FXML private TextField             txtNbSemaine;
    @FXML private TextField             txtNbGp;

    private Module module;
    private CategorieHeure catHp;
    private StageSaisieRessource parent;

    public StageAjoutAffectation() //fxml -> ajoutAffectation
    {
        this.setTitle("Ajout Affectation");
    }

    @FXML private void onBtnAjouter()
    {
        Affectation aff;
        if(!cbbCatHeure.getValue().estHebdo() && cbIntervenant.getValue()!=null && !txtNbHeure.getText().isEmpty())
        {
            aff = new Affectation(this.module, cbIntervenant.getValue(),cbbCatHeure.getValue(), Fraction.valueOf(txtNbHeure.getText()), txtCommentaire.getText() );
            StageSaisieRessource.ensAff.add( aff );
            System.out.println(StageSaisieRessource.ensAff);
        }
        else if (cbIntervenant.getValue()!=null && cbbCatHeure.getValue()!=null && !txtNbGp.getText().isEmpty() && !txtNbSemaine.getText().isEmpty())
        {
            aff = new Affectation(this.module, cbIntervenant.getValue(),cbbCatHeure.getValue(), Integer.parseInt(txtNbGp.getText()),Integer.parseInt(txtNbSemaine.getText()),txtCommentaire.getText());
            StageSaisieRessource.ensAff.add( aff );
            System.out.println(StageSaisieRessource.ensAff);
        }

        this.close();
    }
    @FXML private void onBtnAnnuler() {
        this.close();
    }

    public void init()
    {
        cbIntervenant.setItems(FXCollections.observableList(Controleur.get().getMetier().getIntervenants()));
        cbbCatHeure.setItems(FXCollections.observableList(this.parent.getLstCatH()));

        // Ajouter des ChangeListeners pour chaque RadioButton
        cbbCatHeure.setOnAction((observable) -> {
            // Assurez-vous que cbbCatHeure.getValue() n'est pas null avant de l'utiliser
            changementFocus();
        });

        cbbCatHeure.setValue(cbbCatHeure.getItems().get(0));
        changementFocus();

        creerFormatter(this.txtNbGp);
        creerFormatter(this.txtNbHeure);
        creerFormatter(this.txtNbSemaine);
    }

    private void changementFocus()
    {
        if (cbbCatHeure.getValue() != null) {
            if (cbbCatHeure.getValue().estHebdo()) {
                this.txtNbGp.setDisable(false);
                this.txtNbSemaine.setDisable(false);
                this.txtNbHeure.setText("");
                this.txtNbHeure.setDisable(true);
            } else {
                this.txtNbGp.setDisable(true);
                this.txtNbSemaine.setDisable(true);
                this.txtNbGp.setText("");
                this.txtNbSemaine.setText("");
                this.txtNbHeure.setDisable(false);
            }
        }
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

    public void setParent(StageSaisieRessource stageSaisieRessource)
    {
        this.parent = stageSaisieRessource;
    }
}
