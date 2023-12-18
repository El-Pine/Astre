package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.Affectation;
import fr.elpine.astre.metier.objet.Intervenant;
import fr.elpine.astre.metier.objet.Modules;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.*;
import javafx.stage.Stage;



import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StagePrevisionnel implements Initializable
{
	private static ObservableList<Modules> modulesListS1 = FXCollections.observableArrayList();
	private static ObservableList<Modules> modulesListS2 = FXCollections.observableArrayList();
	private static ObservableList<Modules> modulesListS3 = FXCollections.observableArrayList();
	private static ObservableList<Modules> modulesListS4 = FXCollections.observableArrayList();
	private static ObservableList<Modules> modulesListS5 = FXCollections.observableArrayList();
	private static ObservableList<Modules> modulesListS6 = FXCollections.observableArrayList();
	@FXML
	public TableColumn<Modules,String> s1code;
	@FXML
	public TableColumn<Modules,String> s1liblong;
	@FXML
	public TableColumn<Modules,Integer> s1hahpn;
	@FXML
	public TableColumn<Modules,String> s2code;
	@FXML
	public TableColumn<Modules,String> s2liblong;
	@FXML
	public TableColumn<Modules,Integer> s2hahpn;
	@FXML
	public TableColumn<Modules,String> s3code;
	@FXML
	public TableColumn<Modules,String> s3liblong;
	@FXML
	public TableColumn<Modules,Integer> s3hahpn;
	@FXML
	public TableColumn<Modules,String> s4code;
	@FXML
	public TableColumn<Modules,String> s4liblong;
	@FXML
	public TableColumn<Modules,Integer> s4hahpn;
	@FXML
	public TableColumn<Modules,String> s5code;
	@FXML
	public TableColumn<Modules,String> s5liblong;
	@FXML
	public TableColumn<Modules,Integer> s5hahpn;
	@FXML
	public TableColumn<Modules,String> s6code;
	@FXML
	public TableColumn<Modules,String> s6liblong;
	@FXML
	public TableColumn<Modules,Integer> s6hahpn;
	@FXML
	public TableView<Modules> tabs6;
	@FXML
	public TableView<Modules> tabs1;
	@FXML
	public TableView<Modules> tabs2;
	@FXML
	public TableView<Modules> tabs3;
	@FXML
	public TableView<Modules> tabs4;
	@FXML
	public TableView<Modules> tabs5;
	@FXML
	private TabPane pnlControlSem;
	private Stage stage;

	public static Stage creer() throws IOException
	{
		Stage stage = new Stage();

		StagePrevisionnel.modulesListS1 = FXCollections.observableArrayList(Controleur.get().getDb().getPrevisionsbySemestre(1));
		StagePrevisionnel.modulesListS2 = FXCollections.observableArrayList(Controleur.get().getDb().getPrevisionsbySemestre(2));
		StagePrevisionnel.modulesListS3 = FXCollections.observableArrayList(Controleur.get().getDb().getPrevisionsbySemestre(3));
		StagePrevisionnel.modulesListS4 = FXCollections.observableArrayList(Controleur.get().getDb().getPrevisionsbySemestre(4));
		StagePrevisionnel.modulesListS5 = FXCollections.observableArrayList(Controleur.get().getDb().getPrevisionsbySemestre(5));
		StagePrevisionnel.modulesListS6 = FXCollections.observableArrayList(Controleur.get().getDb().getPrevisionsbySemestre(6));

		FXMLLoader fxmlLoader = new FXMLLoader(StagePrevisionnel.class.getResource("previsionnel.fxml"));

		Scene scene = new Scene(fxmlLoader.load(), 700, 450);

		StagePrevisionnel stageCtrl = fxmlLoader.getController();
		if (stageCtrl != null) stageCtrl.setStage(stage);

		stage.setTitle("Previsions");
		stage.setScene(scene);


		stage.setOnCloseRequest(e -> {
			// perform actions before closing
			try { StagePrincipal.creer().show(); } catch (IOException ignored) {}
		});

		return stage;
	}

	private void setStage(Stage stage) { this.stage = stage; }

	@FXML
	public void onBtnCreerSae(ActionEvent actionEvent) throws IOException {
		stage.close();
		StageSaisieRessource.creer("SAE", pnlControlSem.getSelectionModel().getSelectedIndex() + 1).show();
	}

	@FXML
	public void onBtnCreerStage(ActionEvent actionEvent)
	{
	}

	@FXML
	public void onBtnCreerRessource(ActionEvent actionEvent) throws IOException {
		stage.close();
		StageSaisieRessource.creer("Ressource", pnlControlSem.getSelectionModel().getSelectedIndex() + 1).show();
	}

	@FXML
	public void onBtnSupprimer(ActionEvent actionEvent)
	{

	}

	@FXML
	public void onBtnModifier(ActionEvent actionEvent)
	{
	}
	@Override
	public void initialize(URL location, ResourceBundle ressources)
	{
		s1code.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
		s1liblong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
		tabs1.setItems(modulesListS1);

		s2code.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
		s2liblong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
		tabs2.setItems(modulesListS2);

		s3code.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
		s3liblong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
		tabs3.setItems(modulesListS3);

		s4code.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
		s4liblong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
		tabs4.setItems(modulesListS4);

		s5code.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
		s5liblong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
		tabs5.setItems(modulesListS5);

		s6code.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
		s6liblong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
		tabs6.setItems(modulesListS6);


	}
}