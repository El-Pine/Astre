package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.objet.Annee;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class StageAnnee {
    public ComboBox cbbAnnee;
    private Stage stage;


    public static Stage creer() throws IOException
    {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("saisieAnnee.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 350, 100);

        StageAnnee stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) {
            stageCtrl.setStage(stage);
            stageCtrl.setCpbContrat();
        }

        stage.setTitle("Choix de l'annee");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            try {
                StagePrincipal.creer().show();
            } catch (IOException ignored) { }
        });

        return stage;
    }

    private void setStage(Stage stage) { this.stage = stage; }

    public void setCpbContrat()
    {
        ArrayList<Annee>  lstAnnee = Controleur.get().getMetier().getAnnees();
        ArrayList<String> lstNomAnnee = new ArrayList<String>();
        for ( Annee an : lstAnnee)
            lstNomAnnee.add(an.getNom());

        ObservableList<String> oLstNonAnnee = FXCollections.observableList(lstNomAnnee);
        this.cbbAnnee.setItems(oLstNonAnnee);
    }

    public void btnAjouter(ActionEvent actionEvent) {
    }

    public void btnConsulter(ActionEvent actionEvent) throws IOException {
        Annee an = null;
        for ( Annee annee : Controleur.get().getMetier().getAnnees())
            if ( annee.getNom().equals(this.cbbAnnee.getValue()))
                an = annee;
        Controleur.get().getMetier().changerAnneeActuelle(an);
        this.stage.close();
        StagePrincipal.creer().show();
    }

    public void btnDupliquer(ActionEvent actionEvent) {
    }

    public void btnSupprimer(ActionEvent actionEvent)
    {

    }
}
