package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.CategorieIntervenant;
import fr.elpine.astre.metier.objet.Intervenant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StageAjoutIntervenant
{
    @FXML
    private Label lblErreur;
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

    private static StageIntervenant parent;

    public static Stage creer( StageIntervenant parent) throws IOException
    {
        Stage stage = new Stage();
        StageAjoutIntervenant.parent = parent;

        FXMLLoader fxmlLoader = new FXMLLoader(StageAjoutIntervenant.class.getResource("saisieIntervenant.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 450);

        StageAjoutIntervenant stagectrl = fxmlLoader.getController();

        if(stagectrl != null) stagectrl.setStage(stage);

        stage.setTitle("Ajout Intervenant");
        stage.setScene(scene);
        stagectrl.setCpbContrat();

        stage.setOnCloseRequest(e -> {
            // perform actions before closing
            parent.activer();
        });

        return stage;
    }

    private void setStage(Stage stage) { this.stage = stage; }

    public void setCpbContrat()
    {
        ObservableList<CategorieIntervenant> enscatInter = FXCollections.observableList(Controleur.get().getDb().getAllCategorieIntervenant());
        cpbContrat.setItems(enscatInter);
    }

    public void onBtnValider(ActionEvent actionEvent)
    {
        String nom                  = txtNom   .getText();
        String prenom               = txtPrenom.getText();
        String email                = txtEmail .getText();
        CategorieIntervenant statut = (CategorieIntervenant) cpbContrat.getValue();
        int heureService            = Integer.parseInt(txtService.getText());
        int total                   = Integer.parseInt(txtComplementaire.getText()) + heureService;
        int ratio                   = 0;

        Intervenant inter = Intervenant.creerIntervenant(nom,prenom,email,statut,heureService,total,ratio);
        if ( inter != null )
        {
            Controleur.get().getDb().ajouterIntervenant(inter);
            this.stage.close();
            parent.activer();
        }
        else
        {
            lblErreur.setVisible(true);
            try {
                Thread.sleep(7000);
            } catch (InterruptedException ignored) { }
        }
    }

    public void btnAnnuler(ActionEvent actionEvent) throws IOException
    {
        this.stage.close();
        parent.activer();
    }
}
