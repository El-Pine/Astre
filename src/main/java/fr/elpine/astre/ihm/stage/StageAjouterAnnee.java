package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.metier.objet.Annee;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StageAjouterAnnee extends Stage implements Initializable
{
    public TextField txtfNonAnnee;
    public DatePicker dateDebut;
    public DatePicker dateFin;

    //private static StageAnnee parent;

    //private Stage stage;

    public StageAjouterAnnee() // fxml -> "ajouterAnnee"
    {
        this.setTitle("Ajouter une année");
        this.setMinWidth(600);
        this.setMinHeight(150);
        this.setResizable(false);
    }

    /*public static Stage creer( StageAnnee parent ) throws IOException
    {
        Stage stage = new Stage();

        AstreApplication.refreshIcon(stage);

        StageAjouterAnnee.parent = parent;

        FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("ajouterAnnee.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 100);

        StageAjouterAnnee stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) {
            stageCtrl.setStage(stage);
            stageCtrl.creerFormatter("^(\\d{4})-(\\d{4}).*$",stageCtrl.txtfNonAnnee);
        }

        stage.setTitle("Ajouter une annee");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {

        });

        return stage;
    }

    private void setStage(Stage stage) { this.stage = stage; }*/

    public void onBtnValider(ActionEvent actionEvent)
    {
        String  nom      = txtfNonAnnee.getText();
        boolean nomLibre = true;

        for (Annee a : Controleur.get().getMetier().getAnnees()) if (a.getNom().equals(nom)) nomLibre = false;

        if (nomLibre) {
            Annee a = new Annee(nom, dateDebut.getValue().toString(), dateFin.getValue().toString());
            Controleur.get().getMetier().enregistrer();

            //parent.setCpbContrat();
            this.close();
        } else {
            PopUp.warning("Nom déjà pris", null, "Une année portant le nom '%s' existe déjà, essayer d'ajouter un suffix.".formatted(nom)).showAndWait();
        }
    }

    public void onBtnAnnuler(ActionEvent actionEvent)
    {
        this.close();
    }

    private void creerFormatter(String regex, TextField txtf) {
        txtf.setTextFormatter(new TextFormatter<>(change -> {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(change.getControlNewText());

            if (matcher.find() && Integer.parseInt(matcher.group(2)) - Integer.parseInt(matcher.group(1)) == 1) {
                txtf.setStyle("");
                dateDebut.setValue(LocalDate.of(Integer.parseInt(matcher.group(1)), 1, 1));
                dateFin.setValue(LocalDate.of(Integer.parseInt(matcher.group(2)), 1, 1));
            } else if (change.getControlNewText().isEmpty()) {
                txtf.setStyle("");
            } else {
                txtf.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
            }

            return change;
        }));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

        this.creerFormatter("^(\\d{4})-(\\d{4}).*$", this.txtfNonAnnee);
    }
}
