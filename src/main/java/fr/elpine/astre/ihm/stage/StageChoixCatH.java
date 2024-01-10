package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.objet.CategorieHeure;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.objet.Semestre;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StageChoixCatH extends Stage implements Initializable {
    public GridPane gPane;
    public CheckBox cbTP;
    public CheckBox cbTD;
    public CheckBox cbCM;
    public CheckBox cbHtut;
    private ArrayList<CategorieHeure> ensCatHr;
    private String type;
    private int semestre;
    private StagePrevisionnel parent;

    public StageChoixCatH() // fxml -> "choisirCatHeure"
    {
        this.setTitle("Affectation");
        this.setMinWidth(600);
        this.setMinHeight(220);
    }

    public void setType(String type) { this.type = type; }


    //TODO: A finir
    public void onBtnAnnuler(ActionEvent actionEvent) {
    }

    private ArrayList<CheckBox> ensCb = new ArrayList<>();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ensCatHr = Controleur.get().getMetier().getCategorieHeures();

        this.setWidth(this.getMinWidth() / 4 * (this.ensCatHr.size()));
        this.setHeight(this.getMinHeight());

        this.gPane.getChildren().clear();
    }

    public void init()
    {
        // Créer une nouvelle rangée pour les checkboxes


        if(this.parent.getModifieCatHr())
        {
            completerCatHr(this.parent.getModuleModifier());
        }
        else
        {
            int rowIndex = 4;
            for (CategorieHeure categorieHeure : ensCatHr) {
                FlowPane fp = new FlowPane();
                fp.setAlignment(Pos.CENTER);

                // Créer une CheckBox pour chaque CatégorieHeure
                CheckBox checkBox = new CheckBox(categorieHeure.getNom());
                if (estTypeModule(categorieHeure))
                {
                    checkBox.setSelected(true);
                }

                ensCb.add(checkBox);
                fp.getChildren().add(checkBox);

                // Ajouter la colonne au GridPane
                gPane.add(fp, rowIndex, 0);

                // Augmenter l'index de la rangée pour la prochaine colonne
                rowIndex++;
            }
        }
    }

    public boolean estDansModule(Module mod, CategorieHeure catHr2)
    {
        for (CategorieHeure catHr : mod.getEnsCatHr())
        {
            if(catHr.equals(catHr2)) return true;
        }
        return false;
    }

    public void completerCatHr(Module mod)
    {
        int rowIndex = 4;
        for (CategorieHeure categorieHeure : ensCatHr) {
            FlowPane fp = new FlowPane();
            fp.setAlignment(Pos.CENTER);

            // Créer une CheckBox pour chaque CatégorieHeure
            CheckBox checkBox = new CheckBox(categorieHeure.getNom());
            if (estDansModule(mod, categorieHeure))
            {
                checkBox.setSelected(true);
            }

            ensCb.add(checkBox);
            fp.getChildren().add(checkBox);

            // Ajouter la colonne au GridPane
            gPane.add(fp, rowIndex, 0);

            // Augmenter l'index de la rangée pour la prochaine colonne
            rowIndex++;
        }
    }

    // Méthode appelée lors du clic sur le bouton "Valider"
    public void onBtnValider() {
        ArrayList<CategorieHeure> lstCatValide = new ArrayList<>();
        for ( CheckBox cb : ensCb )
        {
            if ( cb.isSelected() )
                lstCatValide.add(Astre.rechercherCatHr(ensCatHr,cb.getText()));
        }
        StageSaisieRessource stage = Manager.creer("saisieRessource",this);

		if (stage != null)
		{
			stage.setSemestre(this.semestre);
			stage.setTypeModule(type);

            if(this.parent.getModuleModifier() != null) stage.setModule(this.parent.getModuleModifier());
            stage.setCatH(lstCatValide);
			stage.init();

			stage.showAndWait();
            this.close();
			this.parent.refresh();
			this.parent.initSemestre();
		}
    }

    public boolean estTypeModule(CategorieHeure catHr)
    {
        boolean b = false;
        if(this.type != null)
        {
            switch (this.type.toUpperCase())
            {
                case "RESSOURCE" -> b = catHr.estRessource();
                case "STAGE"     -> b = catHr.estStage    ();
                case "SAE"       -> b = catHr.estSae      ();
                case "PPP"       -> b = catHr.estPpp      ();
            }
        }
        return b;
    }

    public void setSemestre(int semestreActuel) {
        this.semestre = semestreActuel;
    }

    public void setParent(StagePrevisionnel stagePrevisionnel)
    {
        this.parent = stagePrevisionnel;
    }
}
