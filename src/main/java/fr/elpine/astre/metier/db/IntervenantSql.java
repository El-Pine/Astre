package fr.elpine.astre.metier.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.objet.Intervenant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class IntervenantSql
{

	private DB db;
	private Connection co;
	private PreparedStatement ps;

	public IntervenantSql(Controleur ctrl) { this.db = ctrl.getDb(); }

	public void ajoutIntervenant(Intervenant intervenant) {

	}

	public void majIntervenant(Intervenant intervenant) {

	}

	public void supprIntervenant(String nom, String prenom) {

	}

	public Intervenant getIntervenantbyNomPenom(String nom, String prenom) {
		return null;
	}

	public ArrayList<Intervenant> getIntervenants() {
		return null;
	}
}
