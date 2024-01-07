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
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
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

    public StageAjoutAffectation() //fxml -> ajoutAffectation
    {
        this.setTitle("Ajout Affectation");
    }

    public void init()
    {
        ObservableList<Intervenant> ensIntervant = FXCollections.observableList(Controleur.get().getMetier().getIntervenants());
        cbIntervenant.setItems(ensIntervant);

        ObservableList<CategorieHeure> ensCatHr  = FXCollections.observableList(Controleur.get().getMetier().getCategorieHeures());
        cbbCatHeure  .setItems( ensCatHr   );
    }

    @FXML
    private void onBtnAjouter()
    {
        Affectation aff = null;
        if(rbHP.isSelected())
        {
            aff = new Affectation(null, cbIntervenant.getValue(),cbbCatHeure.getValue(), Fraction.valueOf(txtNbHeure.getText()), txtCommentaire.getText() );
        }
        else
        {
            aff = new Affectation(this.module, cbIntervenant.getValue(),cbbCatHeure.getValue(), Integer.parseInt(txtNbGp.getText()),Integer.parseInt(txtNbSemaine.getText()),txtCommentaire.getText());
        }
        StageSaisieRessource.ensAff.add(aff);

        this.close();
    }

    @FXML
    private void onBtnAnnuler()
    {
        System.out.println("bite !");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        init();
    }

    public void setModule(Module mod) { this.module = mod; }

}
