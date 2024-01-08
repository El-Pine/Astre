package fr.elpine.astre.ihm.stage;


import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.PopUp;
import fr.elpine.astre.metier.objet.Annee;
import fr.elpine.astre.metier.objet.Module;
import fr.elpine.astre.metier.objet.Semestre;
import fr.elpine.astre.metier.outil.Fraction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StagePrevisionnel extends Stage implements Initializable
{
	private HashMap< Integer, ObservableList<Module> > lstModule;
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

	@FXML
	private Button btnEnregistrer;

	@FXML
	private TabPane pnlControlSem;

	//private Stage stage;


	public StagePrevisionnel() // fxml -> "previsionnel"
	{
		this.setTitle("Previsions");
		this.setMinHeight(450);
		this.setMinWidth(800);
	}

	/*public static Stage creer() throws IOException
	{
		Stage stage = new Stage();

		AstreApplication.refreshIcon(stage);

		FXMLLoader fxmlLoader = new FXMLLoader(StagePrevisionnel.class.getResource("previsionnel.fxml"));

		Scene scene = new Scene(fxmlLoader.load());
		stage.setMinHeight(400);
		stage.setMinWidth(800);
		stage.setTitle("Previsions");
		stage.setScene(scene);

		StagePrevisionnel stageCtrl = fxmlLoader.getController();
		if (stageCtrl != null) stageCtrl.setStage(stage);

		stage.setOnCloseRequest(e -> {
			try {
				Controleur.get().getMetier().rollback();
				StagePrincipal.creer().show();
			} catch (IOException ignored) {
			}
		});

		return stage;
	}

	private void setStage(Stage stage) {
		this.stage = stage;
	}*/

	@FXML
	public void onBtnCreerPpp(ActionEvent actionEvent) throws IOException
	{
		StageSaisieRessource stage = Manager.creer("saisieRessource",this);
		int num = pnlControlSem.getSelectionModel().getSelectedIndex() + 1;
		stage.setSemestre(num);
		stage.setTypeModule("Stage");
		stage.showAndWait();
	}
	@FXML
	public void onBtnEnregistrer(ActionEvent actionEvent) throws IOException {
		Annee annee = Controleur.get().getMetier().getAnneeActuelle();

		ArrayList<Semestre> semestres = annee.getSemestres();

		if(semestres == null) {
			System.out.println("null");
			semestres.add(new Semestre(1, Integer.parseInt(txtNbTD.getText()), Integer.parseInt(txtNbTP.getText()), Integer.parseInt(txtNbSemaine.getText()), Integer.parseInt(txtNbEtd.getText()), annee));

		}
		else {
			System.out.println("non null");
			Semestre semestre = Controleur.get().getMetier().rechercheSemestreByNumero(pnlControlSem.getSelectionModel().getSelectedIndex() + 1);

			semestre.setNbEtd(Integer.parseInt(txtNbEtd.getText()));
			semestre.setNbGrpTD(Integer.parseInt(txtNbTD.getText()));
			semestre.setNbGrpTP(Integer.parseInt(txtNbTP.getText()));
			semestre.setNbSemaine(Integer.parseInt(txtNbSemaine.getText()));

		}

		Controleur.get().getMetier().enregistrer();
	}

	@FXML
	public void onBtnCreerSae(ActionEvent actionEvent) throws IOException
	{
		StageSaisieRessource stage = Manager.creer("saisieRessource",this);
		int num = pnlControlSem.getSelectionModel().getSelectedIndex() + 1;
		stage.setSemestre(num);
		stage.setTypeModule("Stage");
		stage.init();
		stage.showAndWait();
	}

	@FXML
	public void onBtnCreerStage(ActionEvent actionEvent) throws IOException
	{
		StageSaisieRessource stage = Manager.creer("saisieRessource",this);
		int num = pnlControlSem.getSelectionModel().getSelectedIndex() + 1;
		stage.setSemestre(num);
		stage.setTypeModule("Stage");
		stage.init();
		stage.showAndWait();
	}

	@FXML
	public void onBtnCreerRessource(ActionEvent actionEvent) throws IOException {
		StageSaisieRessource stage = Manager.creer("saisieRessource", this);

		int num = pnlControlSem.getSelectionModel().getSelectedIndex() + 1;

		stage.setSemestre(num);
		stage.setTypeModule("Ressource");
		stage.init();
		stage.showAndWait();
		this.refresh();
	}

	@FXML
	public void onBtnSupprimer(ActionEvent actionEvent)
	{
		this.lstViews.forEach(value -> {
			if (value.getSelectionModel().getSelectedItem() != null)
			{
				Module mod = value.getSelectionModel().getSelectedItem();

				if (PopUp.confirmationR("Suppression d'un module",null, "Etes-vous sûr de vouloir supprimer ce module : " + mod.getCode())) {
					if (!mod.supprimer(false, true)) {
						if (PopUp.confirmationR("Suppression d'un module",null, "Etes-vous sûr de vouloir supprimer le module '%s' ainsi que ces affectations".formatted(mod.getCode()))) mod.supprimer(true, false);
					}
				}
				Controleur.get().getMetier().enregistrer();
			}
		});

		this.refresh();
	}

	@FXML
	public void onBtnModifier(ActionEvent actionEvent) {
		System.out.println(tabs1.getSelectionModel().getSelectedIndex());

		this.lstViews.forEach(value -> {
			if(value.getSelectionModel().getSelectedIndex() != -1)
			{
				Module mod = value.getSelectionModel().getSelectedItem();
				//StageSaisieRessource.creer(pnlControlSem.getSelectionModel().getSelectedIndex() + 1, mod).show();
				StageSaisieRessource stage = Manager.creer("saisieRessource", this);


				stage.setSemestre(pnlControlSem.getSelectionModel().getSelectedIndex() + 1);
				stage.setTypeModule(mod.getTypeModule());
				stage.setModule(mod);
				stage.init();
				stage.showAndWait();
				this.refresh();
			}
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.setWidth( this.getMinWidth() );
		this.setHeight( this.getMinHeight() );

		this.lstViews  = new ArrayList<>(Arrays.asList(tabs1, tabs2, tabs3, tabs4, tabs5, tabs6));

		for (TableView<Module> view : this.lstViews)
		{
			TableColumn<Module,String> code       = (TableColumn<Module, String>) view.getColumns().get(0);
			TableColumn<Module,String> liblong    = (TableColumn<Module, String>) view.getColumns().get(1);
			TableColumn<Module,String> hahpn      = (TableColumn<Module, String>) view.getColumns().get(2);
			TableColumn<Module,String> validation = (TableColumn<Module, String>) view.getColumns().get(3);

			code   .setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
			liblong.setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().getNom ()));
			hahpn  .setCellValueFactory (cellData -> new SimpleStringProperty(
					String.format("%s / %s",
						Fraction.simplifyDouble(cellData.getValue().getSommeAffecte(), true),
						Fraction.simplifyDouble(cellData.getValue().getSommePNPromo(), true)
					)
			));
			validation.setCellValueFactory (cellData -> new SimpleStringProperty(cellData.getValue().estValide() ? "✅" : "❌"));
		}

		this.refresh();

		pnlControlSem.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
			if (newTab != null) {
				initializeTextFields();
			}
		});

		initializeTextFields();
	}

	public void refresh()
	{
		this.lstModule = new HashMap<>();

		Controleur.get().getMetier().getAnneeActuelle().getSemestres().forEach(s -> this.lstModule.put(s.getNumero(), FXCollections.observableArrayList(s.getModules())));

		this.lstViews.forEach(view -> view.setItems( this.lstModule.get( this.lstViews.indexOf(view) + 1 ) ));

	}

	private void initializeTextFields()
	{
		Semestre semestre = Controleur.get().getMetier().rechercheSemestreByNumero(pnlControlSem.getSelectionModel().getSelectedIndex() + 1);

		if (semestre != null) {
			txtNbTD.setText("" + semestre.getNbGrpTD());
			txtNbTP.setText("" + semestre.getNbGrpTP());
			txtNbEtd.setText("" + semestre.getNbEtd());
			txtNbSemaine.setText("" + semestre.getNbSemaine());
		}

	}
}