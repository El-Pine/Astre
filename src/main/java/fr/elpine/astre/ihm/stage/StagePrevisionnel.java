package fr.elpine.astre.ihm.stage;


import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.ihm.outil.Emoji;
import fr.elpine.astre.ihm.outil.Manager;
import fr.elpine.astre.ihm.outil.Regex;
import fr.elpine.astre.metier.objet.Annee;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.objet.Semestre;
import fr.elpine.astre.metier.outil.Fraction;
import fr.elpine.astre.metier.outil.ModuleType;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class StagePrevisionnel extends Stage implements Initializable
{
	private ArrayList< TableView<Module> > lstViews;
	private ArrayList< TextField > lstTxtNbTD;
	private ArrayList< TextField > lstTxtNbTP;
	private ArrayList< TextField > lstTxtNbEtd;
	private ArrayList< TextField > lstTxtNbSemaine;

	@FXML private TableView<Module> tabs1;
	@FXML private TableView<Module> tabs2;
	@FXML private TableView<Module> tabs3;
	@FXML private TableView<Module> tabs4;
	@FXML private TableView<Module> tabs5;
	@FXML private TableView<Module> tabs6;

	@FXML private TextField txtNbTDs1;
	@FXML private TextField txtNbTDs2;
	@FXML private TextField txtNbTDs3;
	@FXML private TextField txtNbTDs4;
	@FXML private TextField txtNbTDs5;
	@FXML private TextField txtNbTDs6;

	@FXML private TextField txtNbTPs1;
	@FXML private TextField txtNbTPs2;
	@FXML private TextField txtNbTPs3;
	@FXML private TextField txtNbTPs4;
	@FXML private TextField txtNbTPs5;
	@FXML private TextField txtNbTPs6;

	@FXML private TextField txtNbEtds1;
	@FXML private TextField txtNbEtds2;
	@FXML private TextField txtNbEtds3;
	@FXML private TextField txtNbEtds4;
	@FXML private TextField txtNbEtds5;
	@FXML private TextField txtNbEtds6;

	@FXML private TextField txtNbSemaines1;
	@FXML private TextField txtNbSemaines2;
	@FXML private TextField txtNbSemaines3;
	@FXML private TextField txtNbSemaines4;
	@FXML private TextField txtNbSemaines5;
	@FXML private TextField txtNbSemaines6;

	@FXML private ChoiceBox<ModuleType> selectedModuleType;

	@FXML private TabPane pnlControlSem;

	private Semestre semestreActuel;
	private TableView<Module> viewActuel;

	private final ArrayList< ArrayList< HashMap<TextField,Boolean> > > valids = new ArrayList<>();


	public StagePrevisionnel() // fxml -> "previsionnel"
	{
		this.setTitle("Prévisionnel");
		this.setMinHeight(450);
		this.setMinWidth(800);
		this.setOnCloseRequest(e -> Controleur.get().getMetier().rollback());
	}


	@FXML private void onBtnEnregistrer() {
		this.setChamps();

		Controleur.get().getMetier().enregistrer();
		this.close();
	}

	@FXML private void onBtnFermer() {
		Controleur.get().getMetier().rollback();
		this.close();
	}

	@FXML private void onBtnCreer() {
		if (verification()) {
			//String type = this.selectedModuleType.getValue().replace("SAÉ", "Sae").replace("Stage/Suivi", "Stage");

			this.saisieModule(this.selectedModuleType.getValue(), null);
			this.refresh();
		}
	}

	@FXML private void onBtnSupprimer()
	{
		if (verification()) {
			Module mod = this.viewActuel.getSelectionModel().getSelectedItem();

			if (mod != null)
				if (PopUp.confirmationR("Suppression d'un module", null, "Êtes-vous sûr de vouloir supprimer ce module : " + mod.getCode()))
					if (!mod.supprimer(false, true))
						if (PopUp.confirmationR("Suppression d'un module", null, "Êtes-vous sûr de vouloir supprimer le module '%s' ainsi que ces affectations".formatted(mod.getCode())))
							mod.supprimer(true, false);

			Controleur.get().getMetier().enregistrer();
			this.refresh();
		}
	}

	@FXML private void onBtnModifier()
	{
		if (verification()) {
			Module mod = this.viewActuel.getSelectionModel().getSelectedItem();

			if (mod != null) {
				this.saisieModule(mod.getTypeModule(), mod);
				this.refresh();
			}
		}
	}

	private boolean verification()
	{
		this.setChamps();

		if (!Controleur.get().getMetier().saveNecessaire()) return true;

		Alert a = PopUp.confirmation("Enregistrement", null, "Voulez-vous enregistrer les changements ?");

		ButtonType btnTypeE = new ButtonType("Oui");
		ButtonType btnTypeA = new ButtonType("Non");
		ButtonType btnTypeR = new ButtonType("Retour", ButtonData.CANCEL_CLOSE);

		a.getButtonTypes().setAll(btnTypeE, btnTypeA, btnTypeR);

		Optional<ButtonType> result = a.showAndWait();

		if (result.isPresent())
		{
			if (result.get() == btnTypeE){
				Controleur.get().getMetier().enregistrer();
				return true;
			} else if (result.get() == btnTypeA) {
				Controleur.get().getMetier().rollback();
				return true;
			}
		}

		return false;
	}

	private void saisieModule(ModuleType typeModule, Module module)
	{
		StageSaisieRessource stage = Manager.creer("saisieRessource", this);

		if (stage != null)
		{
			stage.setSemestre(this.semestreActuel);
			stage.setTypeModule(typeModule);

			if (module != null) stage.setModule(module);

			stage.init();
			stage.showAndWait();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.setWidth( this.getMinWidth() );
		this.setHeight( this.getMinHeight() );

		for (int i = 0; i < 6; i++) valids.add( new ArrayList<>(Arrays.asList( new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>() )) );

		this.lstViews        = new ArrayList<>(Arrays.asList(tabs1, tabs2, tabs3, tabs4, tabs5, tabs6));
		this.lstTxtNbTD      = new ArrayList<>(Arrays.asList(txtNbTDs1, txtNbTDs2, txtNbTDs3, txtNbTDs4, txtNbTDs5, txtNbTDs6));
		this.lstTxtNbTP      = new ArrayList<>(Arrays.asList(txtNbTPs1, txtNbTPs2, txtNbTPs3, txtNbTPs4, txtNbTPs5, txtNbTPs6));
		this.lstTxtNbEtd     = new ArrayList<>(Arrays.asList(txtNbEtds1, txtNbEtds2, txtNbEtds3, txtNbEtds4, txtNbEtds5, txtNbEtds6));
		this.lstTxtNbSemaine = new ArrayList<>(Arrays.asList(txtNbSemaines1, txtNbSemaines2, txtNbSemaines3, txtNbSemaines4, txtNbSemaines5, txtNbSemaines6));

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
					(cellData.getValue().estValide() ? "V" : "I") +
					(cellData.getValue().estModuleInvalide() ? "W" : "")
			));
			validation.setCellFactory(column -> Emoji.getCellFactory());

			view.setEditable(true);
			view.setOnMousePressed(event -> {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
					if (verification()) {
						Module mod = this.viewActuel.getSelectionModel().getSelectedItem();

						if (mod != null) {
							this.saisieModule(mod.getTypeModule(), mod);
							this.refresh();
						}
					}
				}
			});
		}

		//selectedModuleType.setItems(FXCollections.observableArrayList(Arrays.asList("Ressource", "SAÉ", "Stage/Suivi", "PPP")));
		//selectedModuleType.setValue("Ressource");
		selectedModuleType.setItems(FXCollections.observableArrayList(ModuleType.values()));
		selectedModuleType.setValue(ModuleType.RESSOURCE);

		pnlControlSem.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) ->
		{
			for (TableView<Module> tableView : this.lstViews) tableView.getSelectionModel().clearSelection();

			initSemestre();
		});

		for (TextField t : this.lstTxtNbTD)      Regex.activerRegex(Regex.REGEX_INT_NOT_EMPTY, Regex.REGEX_INT, t, this.valids.get( this.lstTxtNbTD.indexOf(t)      ).get(0), false);
		for (TextField t : this.lstTxtNbTP)      Regex.activerRegex(Regex.REGEX_INT_NOT_EMPTY, Regex.REGEX_INT, t, this.valids.get( this.lstTxtNbTP.indexOf(t)      ).get(1), false);
		for (TextField t : this.lstTxtNbEtd)     Regex.activerRegex(Regex.REGEX_INT_NOT_EMPTY, Regex.REGEX_INT, t, this.valids.get( this.lstTxtNbEtd.indexOf(t)     ).get(2), false);
		for (TextField t : this.lstTxtNbSemaine) Regex.activerRegex(Regex.REGEX_INT_NOT_EMPTY, Regex.REGEX_INT, t, this.valids.get( this.lstTxtNbSemaine.indexOf(t) ).get(3), false);

		this.refresh();
	}

	public void refresh()
	{
		this.initSemestre();

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

			lstTxtNbTD     .get(s.getNumero() - 1).setText(s.getNbGrpTD()   > 0 ? String.valueOf(s.getNbGrpTD())   : "");
			lstTxtNbTP     .get(s.getNumero() - 1).setText(s.getNbGrpTP()   > 0 ? String.valueOf(s.getNbGrpTP())   : "");
			lstTxtNbEtd    .get(s.getNumero() - 1).setText(s.getNbEtd()     > 0 ? String.valueOf(s.getNbEtd())     : "");
			lstTxtNbSemaine.get(s.getNumero() - 1).setText(s.getNbSemaine() > 0 ? String.valueOf(s.getNbSemaine()) : "");
		}
	}

	private void setChamps() {
		int num = semestreActuel.getNumero() - 1;

		if ( Regex.estValide( this.valids.get(num).get(0) ) ) semestreActuel.setNbGrpTD  (Integer.parseInt(lstTxtNbTD     .get(num).getText()));
		if ( Regex.estValide( this.valids.get(num).get(1) ) ) semestreActuel.setNbGrpTP  (Integer.parseInt(lstTxtNbTP     .get(num).getText()));
		if ( Regex.estValide( this.valids.get(num).get(2) ) ) semestreActuel.setNbEtd    (Integer.parseInt(lstTxtNbEtd    .get(num).getText()));
		if ( Regex.estValide( this.valids.get(num).get(3) ) ) semestreActuel.setNbSemaine(Integer.parseInt(lstTxtNbSemaine.get(num).getText()));
	}
}