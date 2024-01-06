package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import fr.elpine.astre.metier.objet.Intervenant;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class StageAjoutIntervenant extends Stage implements Initializable
{
    @FXML
    private TextField txtfRatio;
    //private Stage stage;
    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private ComboBox<CategorieIntervenant>  cpbContrat;
    @FXML
    private TextField txtService;
    @FXML
    private TextField txtComplementaire;
    @FXML
    private TextField txtEmail;

    private HashMap<TextField,Boolean> hmChampValider;


    //private static StageIntervenant parent;


    public StageAjoutIntervenant() // fxml -> "saisieIntervenant"
    {
        this.setTitle("Ajout Intervenant");
        this.setMinWidth(700);
        this.setMinHeight(450);

        this.setCpbContrat();
    }

    /*public static Stage creer( StageIntervenant parent) throws IOException
    {
        Stage stage = new Stage();

        AstreApplication.refreshIcon(stage);

        StageAjoutIntervenant.parent = parent;
        StageAjoutIntervenant.hmChampValider = new HashMap<>();

        FXMLLoader fxmlLoader = new FXMLLoader(StageAjoutIntervenant.class.getResource("saisieIntervenant.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 450);

        StageAjoutIntervenant stagectrl = fxmlLoader.getController();

        if(stagectrl != null)
        {
            StageAjoutIntervenant.hmChampValider.put(stagectrl.txtNom,false);
            StageAjoutIntervenant.hmChampValider.put(stagectrl.txtPrenom,false);
            StageAjoutIntervenant.hmChampValider.put(stagectrl.txtEmail,false);
            StageAjoutIntervenant.hmChampValider.put(stagectrl.txtService,false);
            StageAjoutIntervenant.hmChampValider.put(stagectrl.txtComplementaire,false);
            StageAjoutIntervenant.hmChampValider.put(stagectrl.txtfRatio,false);
            stagectrl.setStage(stage);
            stagectrl.creerFormatter("[\\p{L}]+",stagectrl.txtNom);
            stagectrl.creerFormatter("[\\p{L}]+",stagectrl.txtPrenom);
            stagectrl.creerFormatter("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",stagectrl.txtEmail);
            stagectrl.creerFormatter("\\b[1-9]\\d*\\b",stagectrl.txtService);
            stagectrl.creerFormatter("\\b[1-9]\\d*\\b",stagectrl.txtComplementaire);
            stagectrl.creerFormatter("^(0*(0(\\.\\d+)?|0\\.[0-9]*[1-9]+)|0*([1-9]\\d*|0)\\/[1-9]\\d*)$",stagectrl.txtfRatio);
        }

        stage.setTitle("Ajout Intervenant");
        stage.setScene(scene);
        stagectrl.setCpbContrat();

        stage.setOnCloseRequest(e -> parent.activer());

        return stage;
    }*/

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

    /*private void setStage(Stage stage) { this.stage = stage; }*/

    public void setCpbContrat()
    {
        ObservableList<CategorieIntervenant> enscatInter = FXCollections.observableList(Controleur.get().getMetier().getCategorieIntervenants());
        cpbContrat.setItems(enscatInter);
        cpbContrat.setValue(enscatInter.get(0));
    }

    public void onBtnValider(ActionEvent actionEvent)
    {
        AtomicBoolean test = new AtomicBoolean(true);
        this.hmChampValider.forEach((key, value) -> {
            if ( !value ) test.set(false);
        });

        if (test.get())
            /*StageIntervenant.interAAjouter.add(*/new Intervenant(
                    this.txtNom.getText(),
                    this.txtPrenom.getText(),
                    this.txtEmail.getText(),
                    this.cpbContrat.getValue(),
                    Fraction.valueOf(this.txtService.getText()),
                    Fraction.valueOf(this.txtComplementaire.getText()),
                    Fraction.valueOf(this.txtfRatio.getText())
            );//);

        //parent.refresh();
        this.close();
        //parent.activer();
    }

    public void btnAnnuler() {
        this.close();
        //parent.activer();
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
    }
}
