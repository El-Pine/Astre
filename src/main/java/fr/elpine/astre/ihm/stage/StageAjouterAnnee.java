package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.metier.objet.Annee;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StageAjouterAnnee extends Stage implements Initializable
{
    public TextField txtfNonAnnee;
    public DatePicker dateDebut;
    public DatePicker dateFin;

    private boolean estValide;

    public StageAjouterAnnee() // fxml -> "ajouterAnnee"
    {
        this.setTitle("Ajouter une année");
        this.setMinWidth(610);
        this.setMinHeight(150);
        this.setResizable(false);
    }

    public void onBtnValider()
    {
        if ( !this.estValide ) { return; }
        String  nom      = txtfNonAnnee.getText();

        if (!Controleur.get().getMetier().existeAnnee(nom))
        {
            new Annee(nom, dateDebut.getValue().toString(), dateFin.getValue().toString());

            Controleur.get().getMetier().enregistrer();

            this.close();
        } else {
            PopUp.warning("Nom déjà pris", null, "Une année portant le nom '%s' existe déjà, essayer d'ajouter un suffix.".formatted(nom)).showAndWait();
        }
    }

    public void onBtnAnnuler()
    {
        this.close();
    }

    private void creerFormatter(TextField txtf) {
        txtf.setTextFormatter(new TextFormatter<>(change -> {
            Pattern pattern = Pattern.compile("^(\\d{4})-(\\d{4}).*$");
            Matcher matcher = pattern.matcher(change.getControlNewText());

            if (matcher.find() && Integer.parseInt(matcher.group(2)) - Integer.parseInt(matcher.group(1)) == 1) {
                txtf.setStyle("");

                LocalDate deb = dateDebut.getValue();
                LocalDate fin = dateFin  .getValue();

                dateDebut.setValue( LocalDate.of(Integer.parseInt(matcher.group(1)), deb == null ? 9 : deb.getMonthValue(), deb == null ? 1 : deb.getDayOfMonth()) );
                dateFin  .setValue( LocalDate.of(Integer.parseInt(matcher.group(2)), fin == null ? 7 : fin.getMonthValue(), fin == null ? 1 : fin.getDayOfMonth()) );
                this.estValide = true;
            } else if (change.getControlNewText().isEmpty()) {
                txtf.setStyle("");
                this.estValide = false;
            } else {
                txtf.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px");
                this.estValide = false;
            }

            return change;
        }));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setWidth( this.getMinWidth() );
        this.setHeight( this.getMinHeight() );
        this.estValide = false;

        this.creerFormatter(this.txtfNonAnnee);

        dateDebut.setShowWeekNumbers(true);
        dateFin  .setShowWeekNumbers(true);
    }
}
