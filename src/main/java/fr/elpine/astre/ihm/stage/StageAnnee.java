package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.metier.objet.Annee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class StageAnnee extends Stage implements Initializable
{
    public ComboBox<Annee> cbbAnnee;
    public Button btnConsulter;
    public Button btnDupliquer;
    public Button btnSupprimer;
    //private Stage stage;


    public StageAnnee() // fxml -> "saisieAnnee"
    {
        this.setTitle("Choix de l'annee");
        this.setMinWidth(400);
        this.setMinHeight(150);
        this.setResizable(false);
    }

    /*public static Stage creer() throws IOException
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

    private void setStage(Stage stage) { this.stage = stage; }*/

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

    public void onBtnAjouter() {
        //StageAjouterAnnee.creer( this ).show();
        Stage stage = Manager.creer("ajouterAnnee", this);

        stage.showAndWait();
        this.setCpbContrat();
    }

    public void onBtnConsulter() {
        Annee an = this.cbbAnnee.getValue();

        Controleur.get().getMetier().changerAnneeActuelle(an);

        this.close();

        //StagePrincipal.creer().show();
    }

    public void onBtnDupliquer() {
        Annee an = this.cbbAnnee.getValue();//null;

        PopUp.textInputDialog(
                this.cbbAnnee.getValue().toString(),
                "Dupliquer",
                String.format("Copie de l'année : %s", this.cbbAnnee.getValue().toString()),
                "Nouveau nom :"
        ).showAndWait().ifPresent(nom -> {
            boolean nomLibre = true;

            for (Annee a : Controleur.get().getMetier().getAnnees()) if (a.getNom().equals(nom)) nomLibre = false;

            if (nomLibre && nom.matches("^(\\d{4})-(\\d{4}).*$") && nom.startsWith(String.format("%s-%s", an.getDateDeb().toLocalDate().getYear(), an.getDateFin().toLocalDate().getYear())))
                an.dupliquer(nom);
            else
                PopUp.warning("Nom incorrect", null, "Le nom '%s' n'est pas valide.".formatted(nom)).showAndWait();
        });

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

                if (resultSecond.isPresent() && resultSecond.get() == ButtonType.OK) {
                    an.supprimer(true);

                    Controleur.get().getMetier().enregistrer();
                }
            }
        }

        this.setCpbContrat();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );

        this.setCpbContrat();
    }
}
