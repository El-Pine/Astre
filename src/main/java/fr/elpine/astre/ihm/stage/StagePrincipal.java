package fr.elpine.astre.ihm.stage;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.ihm.AstreApplication;
import fr.elpine.astre.metier.objet.Annee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StagePrincipal extends Stage implements Initializable
{
	public ImageView image;
	public Button btnPrev;
	public Button btnInter;
	public Button btnEtat;
	@FXML private Button btnAnnee;
	//private Stage stage;

	public StagePrincipal() // fxml -> "accueil"
	{
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
	protected void onBtnClickParametre() {
		Stage stage = Manager.creer("accueilConfig", this);

        assert stage != null;
        stage.showAndWait();
	}

	@FXML
	protected void onBtnClickPrevisionnel() {
		Stage stage = Manager.creer("previsionnel", this);

        assert stage != null;
        stage.showAndWait();
	}

	@FXML
	protected void onBtnClickIntervenant() {
		Stage stage = Manager.creer("intervenant", this);

        assert stage != null;
        stage.showAndWait();
	}

	@FXML
	protected void onBtnClickEtat() {
		Stage stage = Manager.creer("etats", this);

        assert stage != null;
        stage.showAndWait();
	}

	public void onBtnClickAnnee() {
		Stage stage = Manager.creer("saisieAnnee", this);

        assert stage != null;
        stage.showAndWait();

		this.refresh();
	}

	public void refresh()
	{
		Annee a = Controleur.get().getMetier().getAnneeActuelle();

		if (a == null)
		{
			this.btnPrev.setDisable( true );
			this.btnEtat.setDisable( true );
			this.btnAnnee.setText("Année");
		}
		else
		{
			this.btnPrev.setDisable( false );
			this.btnEtat.setDisable( false );
			this.btnAnnee.setText("Année (%s)".formatted(a.getNom()));
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