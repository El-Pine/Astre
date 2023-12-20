package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.Annee;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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

    public void onBtnValider(ActionEvent actionEvent)
    {
        Annee a = new Annee(txtfNonAnnee.getText(),dateDebut.getValue().toString(),dateFin.getValue().toString());
        Controleur.get().getMetier().ajouterAnnee(a);
        this.stage.close();
    }

    public void onBtnAnnuler(ActionEvent actionEvent)
    {
        this.stage.close();
    }
}
