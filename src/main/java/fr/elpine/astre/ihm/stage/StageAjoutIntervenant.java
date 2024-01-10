package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
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
    @FXML
    private ComboBox<CategorieIntervenant>  cpbContrat;
    @FXML
    private TextField txtfRatio;
    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtService;
    @FXML
    private TextField txtComplementaire;
    @FXML
    private TextField txtEmail;

    private HashMap<TextField,Boolean> hmChampValider;


    public StageAjoutIntervenant() // fxml -> "saisieIntervenant"
    {
        this.setTitle("Ajout d'un intervenant");
        this.setMinWidth(700);
        this.setMinHeight(450);
    }

    private void creerFormatter(String regex, TextField txtf) {
        txtf.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches(regex)) {
                txtf.setStyle("");
                this.hmChampValider.put(txtf,true);
            } else if (change.getControlNewText().isEmpty()) {
                this.hmChampValider.put(txtf,false);
            } else {
                txtf.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
                this.hmChampValider.put(txtf,false);
            }
            return change;
        }));
    }

    public void onBtnValider()
    {
        boolean test = true;

        for (Map.Entry<TextField, Boolean> e : this.hmChampValider.entrySet())
	        if (!e.getValue()) { test = false; break; }

        if (test) {
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

    public void btnAnnuler() {
        this.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

        this.hmChampValider = new HashMap<>();

        this.hmChampValider.put(this.txtNom,false);
        this.hmChampValider.put(this.txtPrenom,false);
        this.hmChampValider.put(this.txtEmail,false);
        this.hmChampValider.put(this.txtService,false);
        this.hmChampValider.put(this.txtComplementaire,false);
        this.hmChampValider.put(this.txtfRatio,false);

        this.creerFormatter("[\\p{L}]+",this.txtNom);
        this.creerFormatter("[\\p{L}]+",this.txtPrenom);
        this.creerFormatter("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",this.txtEmail);
        this.creerFormatter("\\b[1-9]\\d*\\b",this.txtService);
        this.creerFormatter("\\b[1-9]\\d*\\b",this.txtComplementaire);
        this.creerFormatter("^(0*(0(\\.\\d+)?|0\\.[0-9]*[1-9]+)|0*([1-9]\\d*|0)\\/[1-9]\\d*)$",this.txtfRatio);

        ObservableList<CategorieIntervenant> enscatInter = FXCollections.observableList(Controleur.get().getMetier().getCategorieIntervenants());
        cpbContrat.setItems(enscatInter);
        if (!enscatInter.isEmpty()) cpbContrat.setValue(enscatInter.get(0));
    }
}
