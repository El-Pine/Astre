package fr.elpine.astre.ihm.stage;


import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.metier.objet.Affectation;
import fr.elpine.astre.metier.objet.Annee;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.objet.Semestre;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class StagePrevisionnel extends Stage implements Initializable
{
	private ArrayList< TableView<Module> > lstViews;

	@FXML
	public TableView<Module>           tabs1;
	@FXML
	public TableView<Module>           tabs2;
	@FXML
	public TableView<Module>           tabs3;
	@FXML
	public TableView<Module>           tabs4;
	@FXML
	public TableView<Module>           tabs5;
	@FXML
	public TableView<Module>           tabs6;

	@FXML
	private TextField txtNbTD;
	@FXML
	private TextField txtNbTP;
	@FXML
	private TextField txtNbEtd;
	@FXML
	private TextField txtNbSemaine;

	public ChoiceBox<String> selectedModuleType;

	@FXML
	private TabPane pnlControlSem;

	private Semestre semestreActuel;
	private TableView<Module> viewActuel;


	public StagePrevisionnel() // fxml -> "previsionnel"
	{
		this.setTitle("Prévisionnel");
		this.setMinHeight(450);
		this.setMinWidth(800);
	}

	@FXML
	public void onBtnEnregistrer() {
		this.setChamps();

		Controleur.get().getMetier().enregistrer();
		this.close();
	}

	public void onBtnFermer() {
		Controleur.get().getMetier().rollback();
		this.close();
	}

	public void onBtnCreer() {
		this.setChamps();

		String type = this.selectedModuleType.getValue().replace("SAÉ", "Sae").replace("Stage/Suivi", "Stage");

		StageSaisieRessource stage = Manager.creer("saisieRessource",this);

		if (stage != null)
		{
			stage.setSemestre(this.semestreActuel.getNumero());
			stage.setTypeModule(type);
			stage.init();

			stage.showAndWait();

			this.refresh();
			this.initSemestre();
		}
	}

	@FXML
	public void onBtnSupprimer()
	{
		Module mod = this.viewActuel.getSelectionModel().getSelectedItem();

		if ( mod != null )
		{
			if (PopUp.confirmationR("Suppression d'un module",null, "Êtes-vous sûr de vouloir supprimer ce module : " + mod.getCode())) {
				if (!mod.supprimer(false, true)) {
					if (PopUp.confirmationR("Suppression d'un module",null, "Êtes-vous sûr de vouloir supprimer le module '%s' ainsi que ces affectations".formatted(mod.getCode()))) mod.supprimer(true, false);
				}
			}
		}

		Controleur.get().getMetier().enregistrer();
		this.refresh();
	}

	@FXML
	public void onBtnModifier()
	{
		modifierModule();
	}

	private void modifierModule(){
		this.setChamps();

		Module mod = this.viewActuel.getSelectionModel().getSelectedItem();
		System.out.println("mod previsionnel : " + mod);
		if ( mod != null )
		{
			StageSaisieRessource stage = Manager.creer("saisieRessource", this);

			if (stage != null)
			{
				stage.setSemestre(this.semestreActuel.getNumero());
				stage.setModule(mod);
				stage.init();
				stage.showAndWait();
				this.refresh();
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.setWidth( this.getMinWidth() );
		this.setHeight( this.getMinHeight() );

		this.lstViews  = new ArrayList<>(Arrays.asList(tabs1, tabs2, tabs3, tabs4, tabs5, tabs6));

		initSemestre();

		for (TableView<Module> view : this.lstViews)
		{
			@SuppressWarnings("unchecked") TableColumn<Module,String> code       = (TableColumn<Module, String>) view.getColumns().get(0);
			@SuppressWarnings("unchecked") TableColumn<Module,String> liblong    = (TableColumn<Module, String>) view.getColumns().get(1);
			@SuppressWarnings("unchecked") TableColumn<Module,String> hahpn      = (TableColumn<Module, String>) view.getColumns().get(2);
			@SuppressWarnings("unchecked") TableColumn<Module,String> validation = (TableColumn<Module, String>) view.getColumns().get(3);

			code   .setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
			liblong.setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getNom ()));
			hahpn  .setCellValueFactory (cellData -> new SimpleStringProperty(
					String.format("%s / %s",
						Fraction.simplifyDouble(cellData.getValue().getSommeAffecte(), true),
						Fraction.simplifyDouble(cellData.getValue().getSommePNPromo(), true)
					)
			));
			validation.setCellValueFactory (cellData -> new SimpleStringProperty(
					(cellData.getValue().estValide() ? "✔" : "❌") +
					(cellData.getValue().getSommeAffecte() > cellData.getValue().getSommePNPromo() ? " ⚠" : "")
			));

			view.setEditable(true);
			view.setOnMousePressed(event -> {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
					Module rowData = view.getSelectionModel().getSelectedItem();
					if (rowData!=null)
						modifierModule();
				}
			});
		}

		selectedModuleType.setItems(FXCollections.observableArrayList(Arrays.asList("Ressource", "SAÉ", "Stage/Suivi", "PPP")));
		selectedModuleType.setValue("Ressource");

		pnlControlSem.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) ->
		{
			for (TableView tableView : this.lstViews) tableView.getSelectionModel().clearSelection();

			initSemestre();
		});

		this.refresh();
	}

	public void refresh()
	{
		Annee a = Controleur.get().getMetier().getAnneeActuelle();

		if ( a != null )
			a.getSemestres().forEach(s -> {
				TableView<Module> view = this.lstViews.get(s.getNumero() - 1);

				if ( view != null ) {
					view.setItems(FXCollections.observableArrayList(s.getModules()));
					view.refresh();
				}
			});
		else this.close();
	}

	private void initSemestre()
	{
		Annee a = Controleur.get().getMetier().getAnneeActuelle();

		if ( a != null ) {
			int numView = pnlControlSem.getSelectionModel().getSelectedIndex();

			this.viewActuel     = this.lstViews.get(numView);
			this.semestreActuel = null;

			a.getSemestres().forEach(s -> {
				if (s.getNumero() == numView + 1) this.semestreActuel = s;
			});

			if ( this.semestreActuel == null )
				this.semestreActuel = new Semestre(numView + 1, 0, 0, 0, 0, a);
		}
		else this.close();

		if ( this.semestreActuel != null ) {
			Semestre s = this.semestreActuel;

			txtNbTD     .setText(s.getNbGrpTD()   > 0 ? String.valueOf(s.getNbGrpTD())   : "");
			txtNbTP     .setText(s.getNbGrpTP()   > 0 ? String.valueOf(s.getNbGrpTP())   : "");
			txtNbEtd    .setText(s.getNbEtd()     > 0 ? String.valueOf(s.getNbEtd())     : "");
			txtNbSemaine.setText(s.getNbSemaine() > 0 ? String.valueOf(s.getNbSemaine()) : "");
		}
	}

	private void setChamps() {
		if ( txtNbEtd    .getText().matches("\\d+") ) semestreActuel.setNbEtd    (Integer.parseInt(txtNbEtd    .getText()));
		if ( txtNbTD     .getText().matches("\\d+") ) semestreActuel.setNbGrpTD  (Integer.parseInt(txtNbTD     .getText()));
		if ( txtNbTP     .getText().matches("\\d+") ) semestreActuel.setNbGrpTP  (Integer.parseInt(txtNbTP     .getText()));
		if ( txtNbSemaine.getText().matches("\\d+") ) semestreActuel.setNbSemaine(Integer.parseInt(txtNbSemaine.getText()));
	}
}