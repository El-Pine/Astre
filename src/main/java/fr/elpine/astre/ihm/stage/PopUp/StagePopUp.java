package fr.elpine.astre.ihm.stage.PopUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

// TODO : classe inutile

public class StagePopUp {

    @FXML private Label  lblMessage;
    private static boolean estValider;
    private Stage stage;

    private static Stage creerPopUp(FXMLLoader fxmlLoader, String titre, String message) throws IOException
    {
        Stage stage = new Stage();

        Scene scene = new Scene(fxmlLoader.load(), 335, 200);

        StagePopUp stageCtrl = fxmlLoader.getController();
        if (stageCtrl != null) stageCtrl.setStage(stage,message);

        stage.setTitle(titre);
        stage.setScene(scene);
        stage.requestFocus();
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);

        return stage;
    }

    public static void PopUpInfo( String titre, String message)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(StagePopUp.class.getResource("PopUpInfo.fxml"));

        try {
            creerPopUp(fxmlLoader, titre, message).showAndWait();
        } catch (IOException ignored) { }
    }

    public static void PopUpErreur( String titre, String message)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(StagePopUp.class.getResource("PopUpErreur.fxml"));

        try {
            creerPopUp(fxmlLoader, titre, message).showAndWait();
        } catch (IOException ignored) { }
    }

    public static boolean PopUpConfirmation( String titre, String message ) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(StagePopUp.class.getResource("PopUpConfirmation.fxml"));
        StagePopUp.estValider = false;

        creerPopUp(fxmlLoader, titre, message).showAndWait();
        return StagePopUp.estValider;
    }

    private void setStage(Stage stage, String message) {
        this.stage = stage;
        this.lblMessage.setText(message);
    }

    public void onBtnOk(ActionEvent actionEvent)
    {
        this.stage.close();
    }

    public void onBtnConfirmer(ActionEvent actionEvent)
    {
        StagePopUp.estValider = true;
        this.stage.close();
    }

    public void onBtnAnnuler(ActionEvent actionEvent)
    {
        this.stage.close();
    }
}
