package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.Annee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Border;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StageAjouterAnnee {
    public TextField txtfNonAnnee;
    public DatePicker dateDebut;
    public DatePicker dateFin;

    private static StageAnnee parent;

    private Stage stage;

    public static Stage creer( StageAnnee parent ) throws IOException
    {
        Stage stage = new Stage();
        StageAjouterAnnee.parent = parent;

        FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("ajouterAnnee.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 100);

        StageAjouterAnnee stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) {
            stageCtrl.setStage(stage);
            stageCtrl.creerFormatter("^(\\d{4})-(\\d{4}).*",stageCtrl.txtfNonAnnee);
        }

        stage.setTitle("Ajouter une annee");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {

        });

        return stage;
    }

    private void setStage(Stage stage) { this.stage = stage; }

    public void onBtnValider(ActionEvent actionEvent)
    {
        Annee a = new Annee(txtfNonAnnee.getText(),dateDebut.getValue().toString(),dateFin.getValue().toString());
        Controleur.get().getMetier().ajouterAnnee(a);
        parent.setCpbContrat();
        this.stage.close();
    }

    public void onBtnAnnuler(ActionEvent actionEvent)
    {
        this.stage.close();
    }

    private void creerFormatter(String regex, TextField txtf) {
        txtf.setTextFormatter(new TextFormatter<>(change -> {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(txtf.getText());
            if (matcher.find() && (Integer.parseInt(matcher.group(2)) - Integer.parseInt(matcher.group(1)) == 1))
            {
                txtf.setStyle("");
                dateDebut.setValue(LocalDate.of(Integer.parseInt(matcher.group(1)),1,1));
                dateFin.setValue(LocalDate.of(Integer.parseInt(matcher.group(2)),1,1));
            } else if (change.getControlNewText().isEmpty()) {
                txtf.setStyle("");
            } else {
                txtf.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
            }
            return change;
        }));
    }
}
