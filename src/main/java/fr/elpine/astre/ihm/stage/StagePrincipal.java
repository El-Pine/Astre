package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.ihm.stage.PopUp.StagePopUp;
import fr.elpine.astre.metier.objet.Annee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

// TODO : Bloquer les boutons nécessaire dans le cas ou il n'y a pas d'année

public class StagePrincipal extends Stage implements Initializable
{
	public ImageView image;
	public Button btnPrev;
	public Button btnInter;
	public Button btnEtat;

	private boolean anneeSelectionne;
	//private Stage stage;

	public StagePrincipal() // fxml -> "accueil"
	{
		try {
			Controleur.get().getMetier().getAnneeActuelle().getNom();
			this.anneeSelectionne = true;
		}
		catch ( Exception ignored ) { this.anneeSelectionne = false; }

		this.setTitle("A.S.T.R.E.");
		this.setMinWidth(300);
		this.setMinHeight(450);
		this.setResizable(false);
	}

	/*public static Stage creer() throws IOException
	{
		Stage stage = new Stage();

		AstreApplication.refreshIcon(stage);

		FXMLLoader fxmlLoader = new FXMLLoader(StagePrincipal.class.getResource("accueil.fxml"));

		Scene scene = new Scene(fxmlLoader.load(), 300, 400);

		StagePrincipal stageCtrl = fxmlLoader.getController();
		if (stageCtrl != null) {
			stageCtrl.setStage(stage);
		}

		stage.setTitle("A.S.T.R.E.");
		stage.setScene(scene);

		return stage;
	}

	private void setStage(Stage stage) { this.stage = stage; }*/

	@FXML
	protected void onBtnClickParametre() throws IOException {
		//stage.close();
		//StageAccueilConfig.creer().show();
		Stage stage = Manager.creer("accueilConfig", this);

		stage.showAndWait();
	}

	@FXML
	protected void onBtnClickPrevisionnel() throws IOException {
		//stage.close();
		//StagePrevisionnel.creer().show();
		Stage stage = Manager.creer("previsionnel", this);

		stage.showAndWait();
	}

	@FXML
	protected void onBtnClickIntervenant() throws IOException {
		//stage.close();
		//StageIntervenant.creer().show();
		Stage stage = Manager.creer("intervenant", this);

		stage.showAndWait();
	}

	@FXML
	protected void onBtnClickEtat() throws IOException
	{
		//this.stage.close();
		//StageEtats.creer().show();
		Stage stage = Manager.creer("etats", this);

		stage.showAndWait();
	}

	public void onBtnClickAnnee(ActionEvent actionEvent) throws IOException {
		//this.stage.close();
		//StageAnnee.creer().show();
		Stage stage = Manager.creer("saisieAnnee", this);

		stage.showAndWait();

		this.refresh();
	}

	public void refresh()
	{
		if (Controleur.get().getMetier().getAnneeActuelle() == null)
		{
			this.btnPrev.setDisable( true );
			this.btnEtat.setDisable( true );
		}
		else
		{
			this.btnPrev.setDisable( false );
			this.btnEtat.setDisable( false );
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.setWidth( this.getMinWidth() );
		this.setHeight( this.getMinHeight() );

		image.setImage(new Image(Objects.requireNonNull(AstreApplication.class.getResourceAsStream("icon.png"))));

		this.refresh();
	}
}