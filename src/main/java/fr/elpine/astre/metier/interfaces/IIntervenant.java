package fr.elpine.astre.metier.interfaces;

import fr.elpine.astre.metier.objet.Intervenant;

import java.util.List;

public interface IIntervenant {
    void ajoutIntervenant(Intervenant intervenant);
    void majIntervenant(Intervenant intervenant);
    void supprIntervenant(String nom, String prenom);
    Intervenant getIntervenantbyNomPenom(String nom, String prenom);
    List<Intervenant> getIntervenant();
}
