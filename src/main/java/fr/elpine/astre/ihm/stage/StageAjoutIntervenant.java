package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.ihm.outil.Regex;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import fr.elpine.astre.metier.objet.Intervenant;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StageAjoutIntervenant extends Stage implements Initializable
{
    @FXML private ComboBox<CategorieIntervenant>  cpbContrat;
    @FXML private TextField txtfRatio;
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtService;
    @FXML private TextField txtComplementaire;
    @FXML private TextField txtEmail;

    private HashMap<TextField,Boolean> hmChampValider;


    public StageAjoutIntervenant() // fxml -> "saisieIntervenant"
    {
        this.setTitle("Ajout d'un intervenant");
        this.setMinWidth(700);
        this.setMinHeight(450);
    }


    @FXML private void onBtnValider()
    {
        if ( Regex.estValide(this.hmChampValider) ) {
            new Intervenant(
                    this.txtNom.getText(),
                    this.txtPrenom.getText(),
                    this.txtEmail.getText(),
                    this.cpbContrat.getValue(),
                    Fraction.valueOf(this.txtService.getText()),
                    Fraction.valueOf(this.txtComplementaire.getText()),
                    Fraction.valueOf(this.txtfRatio.getText())
            );

            this.close();
        }
        else
            PopUp.warning("Informations incorrectes", null, "Les informations entrées ne sont pas toutes valides").showAndWait();
    }

    @FXML private void btnAnnuler() {
        this.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

        this.hmChampValider = new HashMap<>();

        Regex.activerRegex(Regex.REGEX_NOM, Regex.REGEX_NOM_CARAC, this.txtNom, this.hmChampValider, false);
        Regex.activerRegex(Regex.REGEX_NOM, Regex.REGEX_NOM_CARAC, this.txtPrenom, this.hmChampValider, false);
        Regex.activerRegex(Regex.REGEX_MAIL, Regex.REGEX_MAIL_CARAC, this.txtEmail, this.hmChampValider, true);
        Regex.activerRegex(Fraction.REGEX, Fraction.REGEX_CARAC, this.txtService, this.hmChampValider, false);
        Regex.activerRegex(Fraction.REGEX, Fraction.REGEX_CARAC, this.txtComplementaire, this.hmChampValider, false);
        Regex.activerRegex(Fraction.REGEX, Fraction.REGEX_CARAC, this.txtfRatio, this.hmChampValider, false);

        ObservableList<CategorieIntervenant> enscatInter = FXCollections.observableList(Controleur.get().getMetier().getCategorieIntervenants());
        cpbContrat.setItems(enscatInter);
        if (!enscatInter.isEmpty()) cpbContrat.setValue(enscatInter.get(0));
    }
}
