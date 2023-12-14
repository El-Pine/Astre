package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StageAjoutIntervenant
{
    private Stage stage;
    private TextField txtNom;
    private TextField txtPrenom;
    private ComboBox  cpbContrat;
    private TextField txtService;
    private TextField txtComplementaire;
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

        stage.setOnCloseRequest(e -> {
            // perform actions before closing
            try { StagePrincipal.creer().show(); } catch (IOException ignored) {}
        });

        return stage;
    }

    private void setStage(Stage stage) { this.stage = stage; }


    protected void onBtnEnregistrer()
    {

    }

    public void onBtnValider(ActionEvent actionEvent)
    {
        System.out.println(txtNom.getText());
        System.out.println(txtPrenom.getText());
        System.out.println(txtEmail.getText());
        System.out.println(txtService.getText());
        System.out.println(txtComplementaire.getText());
    }

    public void btnAnnuler(ActionEvent actionEvent) {
    }
}
