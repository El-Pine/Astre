package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.Annee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        if (stageCtrl != null) { stageCtrl.setStage(stage); }

        stage.setTitle("Ajouter une annee");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            try {
                StagePrincipal.creer().show();
            } catch (IOException ignored) { }
        });

        return stage;
    }

    private void setStage(Stage stage) { this.stage = stage; }

    @FXML
    private void initialize() {
        // Ajouter un écouteur sur la propriété de focus du TextField
        txtfNonAnnee.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Si le focus quitte le TextField
                String dateText = txtfNonAnnee.getText();

                // Vérifier si le texte correspond à une date
                if (isValidDate(dateText)) {
                    txtfNonAnnee.setStyle("-fx-border-color: transparent;");
                    String[] years = dateText.split("-");

                    // Appliquer la date aux DatePicker
                    dateDebut.setValue(LocalDate.of(Integer.parseInt(years[0]),1,1));
                    dateFin.setValue(LocalDate.of(Integer.parseInt(years[1]),1,1));
                }
                else
                    txtfNonAnnee.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 4px;");
            }
        });
    }

    private boolean isValidDate(String dateStr) {
        // Vérifier si la chaîne correspond au format "yyyy-yyyy"
        boolean estAnnee  = dateStr.matches("\\d{4}-\\d{4}");
        String[] years = dateStr.split("-");
        boolean estProche = (Integer.parseInt(years[1]) - Integer.parseInt(years[0])) == 1;
        return estAnnee && estProche;
    }

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
}
