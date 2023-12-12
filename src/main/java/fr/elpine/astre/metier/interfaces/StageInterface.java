package fr.elpine.astre.metier.interfaces;

import fr.elpine.astre.metier.objet.Stage;

import java.util.List;

public interface StageInterface {
    void ajoutStage(Stage stage);
    void majStage(Stage stage);
    void supprStage(String code);
    Stage getStagebyCode(String code);
    List<Stage> getStage();
}
