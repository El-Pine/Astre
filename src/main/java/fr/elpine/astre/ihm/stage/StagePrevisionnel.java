package fr.elpine.astre.ihm.stage;


import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
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
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StagePrevisionnel implements Initializable {
	private static ObservableList<Module> moduleListS1 = FXCollections.observableArrayList();
	private static ObservableList<Module> moduleListS2 = FXCollections.observableArrayList();
	private static ObservableList<Module> moduleListS3 = FXCollections.observableArrayList();
	private static ObservableList<Module> moduleListS4 = FXCollections.observableArrayList();
	private static ObservableList<Module> moduleListS5 = FXCollections.observableArrayList();
	private static ObservableList<Module> moduleListS6 = FXCollections.observableArrayList();

	@FXML
	public TableColumn<Module,String>  s1code;
	@FXML
	public TableColumn<Module,String>  s1liblong;
	@FXML
	public TableColumn<Module,String> s1hahpn;
	@FXML
	public TableColumn<Module,String>  s2code;
	@FXML
	public TableColumn<Module,String>  s2liblong;
	@FXML
	public TableColumn<Module,String> s2hahpn;
	@FXML
	public TableColumn<Module,String>  s3code;
	@FXML
	public TableColumn<Module,String>  s3liblong;
	@FXML
	public TableColumn<Module,String> s3hahpn;
	@FXML
	public TableColumn<Module,String>  s4code;
	@FXML
	public TableColumn<Module,String>  s4liblong;
	@FXML
	public TableColumn<Module,String> s4hahpn;
	@FXML
	public TableColumn<Module,String>  s5code;
	@FXML
	public TableColumn<Module,String>  s5liblong;
	@FXML
	public TableColumn<Module,String> s5hahpn;
	@FXML
	public TableColumn<Module,String>  s6code;
	@FXML
	public TableColumn<Module,String>  s6liblong;
	@FXML
	public TableColumn<Module,String> s6hahpn;
	@FXML
	public TableView<Module>           tabs6;
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
	private TextField txtNbTD;
	@FXML
	private TextField txtNbTP;
	@FXML
	private TextField txtNbEtd;
	@FXML
	private TextField txtNbSemaine;


	@FXML
	private TabPane pnlControlSem;


	private Stage stage;

	public static Stage creer() throws IOException {
		Stage stage = new Stage();

		AstreApplication.refreshIcon(stage);

		ArrayList<Semestre> semestres = Controleur.get().getMetier().getAnneeActuelle().getSemestres();

		for (Semestre s : semestres)
		{
			switch (s.getNumero())
			{
				case 1 -> StagePrevisionnel.moduleListS1 = FXCollections.observableArrayList(s.getModules());
				case 2 -> StagePrevisionnel.moduleListS2 = FXCollections.observableArrayList(s.getModules());
				case 3 -> StagePrevisionnel.moduleListS3 = FXCollections.observableArrayList(s.getModules());
				case 4 -> StagePrevisionnel.moduleListS4 = FXCollections.observableArrayList(s.getModules());
				case 5 -> StagePrevisionnel.moduleListS5 = FXCollections.observableArrayList(s.getModules());
				case 6 -> StagePrevisionnel.moduleListS6 = FXCollections.observableArrayList(s.getModules());
			}
		}

		FXMLLoader fxmlLoader = new FXMLLoader(StagePrevisionnel.class.getResource("previsionnel.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 700, 400);

		StagePrevisionnel stageCtrl = fxmlLoader.getController();
		if (stageCtrl != null) stageCtrl.setStage(stage);

		stage.setTitle("Previsions");
		stage.setScene(scene);

		stage.setOnCloseRequest(e -> {
			try {
				StagePrincipal.creer().show();
			} catch (IOException ignored) {
			}
		});

		return stage;
	}

	private void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	public void onBtnCreerPpp(ActionEvent actionEvent) throws IOException {
		stage.close();
		StageSaisiePpp.creer(pnlControlSem.getSelectionModel().getSelectedIndex() + 1).show();
	}

	@FXML
	public void onBtnCreerSae(ActionEvent actionEvent) throws IOException {
		stage.close();
		StageSaisieSae.creer(pnlControlSem.getSelectionModel().getSelectedIndex() + 1).show();
	}

	@FXML
	public void onBtnCreerStage(ActionEvent actionEvent) throws IOException {
		stage.close();
		StageSaisieStage.creer(pnlControlSem.getSelectionModel().getSelectedIndex() + 1).show();
	}

	@FXML
	public void onBtnCreerRessource(ActionEvent actionEvent) throws IOException {
		stage.close();
		StageSaisieRessource.creer(pnlControlSem.getSelectionModel().getSelectedIndex() + 1).show();
	}

	@FXML
	public void onBtnSupprimer(ActionEvent actionEvent) {
		System.out.println(txtNbTD.getText());
	}

	@FXML
	public void onBtnModifier(ActionEvent actionEvent) {
		// Logique pour modifier
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		s1code.setCellValueFactory   (cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
		s1liblong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom ()));
		s1hahpn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s/%s",
				Fraction.simplifyDouble(cellData.getValue().getSommeAffecte(), true),
				Fraction.simplifyDouble(cellData.getValue().getSommePNPromo(), true)
		)));
		tabs1.setItems(moduleListS1);

		s2code.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
		s2liblong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
		s2hahpn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s/%s",
				Fraction.simplifyDouble(cellData.getValue().getSommeAffecte(), true),
				Fraction.simplifyDouble(cellData.getValue().getSommePNPromo(), true)
		)));
		tabs2.setItems(moduleListS2);

		s3code.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
		s3liblong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
		s3hahpn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s/%s",
				Fraction.simplifyDouble(cellData.getValue().getSommeAffecte(), true),
				Fraction.simplifyDouble(cellData.getValue().getSommePNPromo(), true)
		)));
		tabs3.setItems(moduleListS3);

		s4code.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
		s4liblong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
		s4hahpn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s/%s",
				Fraction.simplifyDouble(cellData.getValue().getSommeAffecte(), true),
				Fraction.simplifyDouble(cellData.getValue().getSommePNPromo(), true)
		)));
		tabs4.setItems(moduleListS4);

		s5code.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
		s5liblong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
		s5hahpn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s/%s",
				Fraction.simplifyDouble(cellData.getValue().getSommeAffecte(), true),
				Fraction.simplifyDouble(cellData.getValue().getSommePNPromo(), true)
		)));
		tabs5.setItems(moduleListS5);

		s6code.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
		s6liblong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
		s6hahpn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s/%s",
				Fraction.simplifyDouble(cellData.getValue().getSommeAffecte(), true),
				Fraction.simplifyDouble(cellData.getValue().getSommePNPromo(), true)
		)));
		tabs6.setItems(moduleListS6);

		pnlControlSem.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
			if (newTab != null) {
				// Rafraîchir les données lorsque l'onglet change
				initializeTextFields();
			}
		});

		initializeTextFields();
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