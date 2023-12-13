package fr.elpine.astre.metier.db;

import fr.elpine.astre.metier.interfaces.IIntervenant;
import fr.elpine.astre.metier.objet.Intervenant;

import java.util.List;

public class IntervenantSql implements IIntervenant
{
    @Override
    public void ajoutIntervenant(Intervenant intervenant) {

    }

    @Override
    public void majIntervenant(Intervenant intervenant) {

    }

    @Override
    public void supprIntervenant(String nom, String prenom) {

    }

    @Override
    public Intervenant getIntervenantbyNomPenom(String nom, String prenom) {
        return null;
    }

    @Override
    public List<Intervenant> getIntervenant() {
        return null;
    }
}
