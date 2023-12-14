package fr.elpine.astre.metier.db;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.objet.CategorieHeure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class CategorieHeureSQL
{
	private DB db = Controleur.get().getDb();
	private Connection co;
	private PreparedStatement ps;

	public CategorieHeureSQL(Controleur ctrl) { this.db = ctrl.getDb(); }

	public void ajoutCatHeure(CategorieHeure catHeure) {

	}

	public void majCatHeure(CategorieHeure catHeure) {

	}

	public void supprCatHeure(String code) {

	}

	public CategorieHeure getcatHeurebyCode(String code) {
		return null;
	}

	public ArrayList<CategorieHeure> getCatHeures() {
		return null;
	}
}
