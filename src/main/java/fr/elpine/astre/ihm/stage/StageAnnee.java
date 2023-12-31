package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.metier.objet.Annee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class StageAnnee
{
    public ComboBox<Annee> cbbAnnee;
    public Button btnConsulter;
    public Button btnDupliquer;
    public Button btnSupprimer;
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
        ArrayList<Annee>      lstAnnee     = Controleur.get().getMetier().getAnnees();
        ObservableList<Annee> oLstNonAnnee = FXCollections.observableList( new ArrayList<>() );
        for (Annee a : lstAnnee) if (!a.isSupprime()) oLstNonAnnee.add(a);

        this.cbbAnnee.setItems(oLstNonAnnee);

        if (Controleur.get().getMetier().getAnneeActuelle() != null)
            this.cbbAnnee.setValue(Controleur.get().getMetier().getAnneeActuelle());

        boolean disabled = this.cbbAnnee.getItems().isEmpty();

        this.cbbAnnee.setDisable( disabled );
        this.btnConsulter.setDisable( disabled );
        this.btnDupliquer.setDisable( disabled );
        this.btnSupprimer.setDisable( disabled );
    }

    public void onBtnAjouter() throws IOException {
        StageAjouterAnnee.creer( this ).show();
    }

    public void onBtnConsulter() throws IOException {
        Annee an = this.cbbAnnee.getValue();

        Controleur.get().getMetier().changerAnneeActuelle(an);

        this.stage.close();

        StagePrincipal.creer().show();
    }

    public void onBtnDupliquer() {
        Annee an = this.cbbAnnee.getValue();//null;

        PopUp.textInputDialog(
                this.cbbAnnee.getValue().toString(),
                "Dupliquer",
                String.format("Copie de l'année : %s", this.cbbAnnee.getValue().toString()),
                "Nouveau nom :"
        ).showAndWait().ifPresent(an::dupliquer);

        this.setCpbContrat();
    }

    public void onBtnSupprimer()
    {
        Annee an = this.cbbAnnee.getValue();

        Optional<ButtonType> result = PopUp.confirmation("Confirmation", null, String.format("Voulez vous supprimer l'année \"%s\" ?", an.getNom())).showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK)
        {
            boolean good = an.supprimer( false );

            if (!good)
            {
                Optional<ButtonType> resultSecond = PopUp.confirmation(
                        "Confirmation",
                        "La suppression est impossible, il y a encore des données présente dans cette année !",
                        String.format("Supprimer l'année \"%s\" et tout son contenu ?", an.getNom())
                ).showAndWait();

                if (resultSecond.isPresent() && resultSecond.get() == ButtonType.OK) an.supprimer( true );

                // TODO : fonction d'enregistrement ici (ps : je gère)
            }
        }

        this.setCpbContrat();
    }
}
