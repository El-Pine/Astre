package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import fr.elpine.astre.metier.objet.Intervenant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class StageAjoutIntervenant
{
    public Button btnValider;
    @FXML
    private TextField txtfRatio;
    private Stage stage;
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

    private static HashMap<TextField,Boolean> hmChampValider;


    private static StageIntervenant parent;

    public static Stage creer( StageIntervenant parent) throws IOException
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
    }

    private void creerFormatter(String regex, TextField txtf) {
        txtf.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches(regex)) {
                txtf.setStyle("");
                StageAjoutIntervenant.hmChampValider.put(txtf,true);
            } else if (change.getControlNewText().isEmpty()) {
                StageAjoutIntervenant.hmChampValider.put(txtf,false);
            } else {
                txtf.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
                StageAjoutIntervenant.hmChampValider.put(txtf,false);
            }
            return change;
        }));
    }

    private void setStage(Stage stage) { this.stage = stage; }

    public void setCpbContrat()
    {
        ObservableList<CategorieIntervenant> enscatInter = FXCollections.observableList(Controleur.get().getMetier().getCategorieIntervenants());
        cpbContrat.setItems(enscatInter);
        cpbContrat.setValue(enscatInter.get(0));
    }

    public void onBtnValider(ActionEvent actionEvent)
    {
        AtomicBoolean test = new AtomicBoolean(true);
        StageAjoutIntervenant.hmChampValider.forEach((key, value) -> {
            if ( !value ) test.set(false);
        });

        if (test.get())
            StageIntervenant.interAAjouter.add(new Intervenant(this.txtNom.getText(),this.txtPrenom.getText(),this.txtEmail.getText(), this.cpbContrat.getValue(),Integer.parseInt(this.txtService.getText()),Integer.parseInt(this.txtComplementaire.getText()),this.txtfRatio.getText()));

        parent.refresh();
        this.stage.close();
        parent.activer();
    }

    public void btnAnnuler() {
        this.stage.close();
        parent.activer();
    }
}
