package fr.elpine.astre.metier.interfaces;

import fr.elpine.astre.metier.objet.Ressource;

import java.util.List;

public interface RessourceInterface {
    void ajoutRessource(Ressource ressource);
    void majRessource(Ressource ressource);
    void supprRessource(String code);
    Ressource getRessourcebyCode(String code);
    List<Ressource> getRessource();
}
