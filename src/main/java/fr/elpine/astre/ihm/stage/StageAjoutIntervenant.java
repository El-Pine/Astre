package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import fr.elpine.astre.metier.objet.Intervenant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StageAjoutIntervenant
{
    private Stage stage;
    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private ComboBox  cpbContrat;
    @FXML
    private TextField txtService;
    @FXML
    private TextField txtComplementaire;
    @FXML
    private TextField txtEmail;

    public static Stage creer() throws IOException
    {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(StageAjoutIntervenant.class.getResource("saisieIntervenant.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 450);

        StageAjoutIntervenant stagectrl = fxmlLoader.getController();

        if(stagectrl != null) stagectrl.setStage(stage);

        stage.setTitle("Ajout Intervenant");
        stage.setScene(scene);
        stagectrl.setCpbContrat();

        stage.setOnCloseRequest(e -> {
            // perform actions before closing
            try { StagePrincipal.creer().show(); } catch (IOException ignored) {}
        });

        return stage;
    }

    private void setStage(Stage stage) { this.stage = stage; }

    public void setCpbContrat()
    {
        for (CategorieIntervenant catInter : Controleur.get().getDb().getAllCategorieIntervenant())
            cpbContrat.setValue(catInter);
    }

    public void onBtnValider(ActionEvent actionEvent)
    {
        String nom                  = txtNom   .getText();
        String prenom               = txtPrenom.getText();
        String email                = txtEmail .getText();
        CategorieIntervenant statut = (CategorieIntervenant) cpbContrat.getValue();
        int heureService            = Integer.parseInt(txtService.getText());
        int total                   = Integer.parseInt(txtComplementaire.getText()) + heureService;

        Controleur.get().getDb().ajouterIntervenant(new Intervenant(nom,prenom,email,statut,heureService,total));
    }

    public void btnAnnuler(ActionEvent actionEvent) throws IOException
    {
        this.stage.close();
        StageIntervenant.creer().show();
    }
}
