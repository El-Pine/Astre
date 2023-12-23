package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.metier.objet.Annee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class StageAnnee {
    public ComboBox<Annee> cbbAnnee;
    private Stage stage;

    public static Stage creer() throws IOException
    {
        Stage stage = new Stage();

        AstreApplication.refreshIcon(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("saisieAnnee.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 400, 150);

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
        /*ArrayList<String> lstNomAnnee = new ArrayList<String>();
        for ( Annee an : lstAnnee)
            lstNomAnnee.add(an.getNom());*/

        ObservableList<Annee/*String*/> oLstNonAnnee = FXCollections.observableList(lstAnnee/*lstNomAnnee*/);
        this.cbbAnnee.setItems(oLstNonAnnee);
        if (Controleur.get().getMetier().getAnneeActuelle() != null)
            this.cbbAnnee.setValue(Controleur.get().getMetier().getAnneeActuelle()/*.getNom()*/);
    }

    public void btnAjouter() throws IOException {
        StageAjouterAnnee.creer( this ).show();
    }

    public void btnConsulter() throws IOException {
        Annee an = this.cbbAnnee.getValue();//null;
        /*for ( Annee annee : Controleur.get().getMetier().getAnnees())
            if ( annee.getNom().equals(this.cbbAnnee.getValue()))
                an = annee;*/
        Controleur.get().getMetier().changerAnneeActuelle(an);
        this.stage.close();
        StagePrincipal.creer().show();
    }

    public void btnDupliquer() {
        Annee an = this.cbbAnnee.getValue();//null;
        /*for ( Annee annee : Controleur.get().getMetier().getAnnees())
            if ( annee.getNom().equals(this.cbbAnnee.getValue()))
                an = annee;

	    assert an != null;*/

        TextInputDialog dialog = new TextInputDialog(this.cbbAnnee.getValue().toString());

        dialog.setTitle("Dupliquer");
        dialog.setHeaderText(String.format("Copie de l'année : %s", this.cbbAnnee.getValue().toString()));
        dialog.setContentText("Nouveau nom :");

	    dialog.showAndWait().ifPresent(name -> {
            Annee a = an.dupliquer(name);

            System.out.println(a.getSemestres());
        });

        this.setCpbContrat();
    }

    public void btnSupprimer()
    {
        Annee an = this.cbbAnnee.getValue();/*null;
        for ( Annee annee : Controleur.get().getMetier().getAnnees())
            if ( annee.getNom().equals(this.cbbAnnee.getValue()))
                an = annee;

        assert an != null;*/

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(String.format("Voulez vous supprimer l'année \"%s\" ?", an.getNom()));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
        {
            boolean good = an.supprimer( false );

            if (!good)
            {
                Alert alertSecond = new Alert(Alert.AlertType.CONFIRMATION);
                alertSecond.setTitle("Confirmation");
                alertSecond.setHeaderText("La suppression est impossible, il y a encore des données présente dans cette année !");
                alertSecond.setContentText(String.format("Supprimer l'année \"%s\" et tout son contenu ?", an.getNom()));

                Optional<ButtonType> resultSecond = alertSecond.showAndWait();
                if (resultSecond.isPresent() && resultSecond.get() == ButtonType.OK) an.supprimer( true );

                // TODO : fonction d'enregistrement ici (ps : je gère)
            }
        }

        this.setCpbContrat();
    }
}
