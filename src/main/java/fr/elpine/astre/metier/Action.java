package fr.elpine.astre.metier;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.objet.Module;

public class Action implements Comparable<Action>
{
	private int lvl;
	private int action;
	private boolean affecteDB;

	private Object objet;

	/*
	 * Action (non rollback) :
	 *  -1 : update du code module
	 *   0 : delete
	 *   1 : insert
	 *   2 : update
	 *
	 * Action (rollback) :
	 *   0 : delete obj
	 *   1 : rollback + reset
	 *   2 : reset
	 *
	* */
	public Action( int action, Object objet, boolean affecteDB)
	{
		/*
		* 0 - 7
		* */
		int lvlAction = 8;

		if (objet instanceof Affectation)          lvlAction = action == 0 ? 0 : action == 1 ? 7 : action == 2 ? 1 : lvlAction;
		if (objet instanceof Attribution)          lvlAction = action == 0 ? 1 : action == 1 ? 6 : action == 2 ? 1 : lvlAction;
		if (objet instanceof Module)               lvlAction = action == 0 ? 2 : action == 1 ? 5 : action == 2 ? 0 : lvlAction;
		if (objet instanceof Semestre)             lvlAction = action == 0 ? 3 : action == 1 ? 4 : action == 2 ? 1 : lvlAction;
		if (objet instanceof Annee)                lvlAction = action == 0 ? 4 : action == 1 ? 3 : action == 2 ? 1 : lvlAction;
		if (objet instanceof Intervenant)          lvlAction = action == 0 ? 5 : action == 1 ? 2 : action == 2 ? 1 : lvlAction;
		if (objet instanceof CategorieIntervenant) lvlAction = action == 0 ? 6 : action == 1 ? 1 : action == 2 ? 1 : lvlAction;
		if (objet instanceof CategorieHeure)       lvlAction = action == 0 ? 7 : action == 1 ? 0 : action == 2 ? 1 : lvlAction;

		this.lvl       = Integer.parseInt(String.format("%02d%02d", action + 2, lvlAction));

		this.action    = action;
		this.affecteDB = affecteDB;
		this.objet     = objet;
	}

	public void executer( boolean rollback )
	{
		if ( rollback ) {
			this.executionRollback();
		}
		else {
			this.executionNormal();
		}
	}

	private void executionNormal() // manque les objets
	{
		DB    db     = Controleur.get().getDb();
		Astre metier = Controleur.get().getMetier();

		if (this.objet instanceof Affectation aff)
		{
			if (action == 0) {
				if (this.affecteDB) db.supprimerAffectation( aff );

				aff.getModule().supprimerAffectation(aff);
				aff.getIntervenant().supprimerAffectation(aff);

				metier.getAffectations().remove(aff);
			}
			if (action == 1) { if (this.affecteDB) db.ajouterAffectation(   aff ); }
			if (action == 2) { if (this.affecteDB) db.majAffectation(       aff ); }

			aff.reset();
		}

		if (this.objet instanceof Attribution att)
		{
			if (action == 0) {
				if (this.affecteDB) db.supprimerAttribution( att );

				att.getModule().supprimerAttribution( att );
				att.getCatHr ().supprimerAttribution( att );

				metier.getAttributions().remove( att );
			}
			if (action == 1) { if (this.affecteDB) db.ajouterAttribution(   att ); }
			if (action == 2) { if (this.affecteDB) db.majAttribution(       att ); }

			att.reset();
		}

		if (this.objet instanceof Module m)
		{
			if (action == -1) { db.majCodeModule( m ); } // uniquement db
			else {
				if (action == 0) {
					if (this.affecteDB) db.supprimerModule( m);

					m.getSemestre().supprimerModule( m );

					metier.getModules().remove( m );
				}
				if (action == 1) { if (this.affecteDB) db.ajouterModule(   m); }
				if (action == 2) { if (this.affecteDB) db.majModule(       m); }

				m.reset();
			}
		}

		if (this.objet instanceof Semestre s)
		{
			if (action == 0) {
				if (this.affecteDB) db.supprimerSemestre( s );

				s.getAnnee().supprimerSemestre( s );

				metier.getSemestres().remove( s );
			}
			if (action == 1) { if (this.affecteDB) db.ajouterSemestre(   s ); }
			if (action == 2) { if (this.affecteDB) db.majSemestre(       s ); }

			s.reset();
		}

		if (this.objet instanceof Annee a)
		{
			if (action == 0) {
				if (this.affecteDB) db.supprimerAnnee( a );

				metier.getAnnees().remove( a );
			}
			if (action == 1) { if (this.affecteDB) db.ajouterAnnee(   a ); }
			if (action == 2) { if (this.affecteDB) db.majAnnee(       a ); }

			a.reset();
		}

		if (this.objet instanceof Intervenant inter)
		{
			if (action == 0) {
				if (this.affecteDB) db.supprimerIntervenant( inter );

				metier.getIntervenants().remove( inter );
			}
			if (action == 1) { if (this.affecteDB) db.ajouterIntervenant(   inter ); }
			if (action == 2) { if (this.affecteDB) db.majIntervenant(       inter ); }

			inter.reset();
		}

		if (this.objet instanceof CategorieIntervenant categorieIntervenant)
		{
			if (action == 0) {
				if (this.affecteDB) db.supprimerCategorieIntervenant( categorieIntervenant );

				metier.getCategorieIntervenants().remove( categorieIntervenant );
			}
			if (action == 1) { if (this.affecteDB) db.ajouterCategorieIntervenant(   categorieIntervenant ); }
			if (action == 2) { if (this.affecteDB) db.majCategorieIntervenant(       categorieIntervenant ); }

			categorieIntervenant.reset();
		}

		if (this.objet instanceof CategorieHeure categorieHeure)
		{
			if (action == 0) {
				if (this.affecteDB) db.supprimerCategorieHeure( categorieHeure );

				metier.getCategorieHeures().remove( categorieHeure );
			}
			if (action == 1) { if (this.affecteDB) db.ajouterCategorieHeure(   categorieHeure ); }
			if (action == 2) { if (this.affecteDB) db.majCategorieHeure(       categorieHeure ); }

			categorieHeure.reset();
		}
	}

	private void executionRollback()
	{
		Astre metier = Controleur.get().getMetier();

		/*
		 * Action (rollback) :
		 *   0 : delete obj
		 *   1 : rollback + reset
		 *   2 : reset
		 * */

		if (this.objet instanceof Affectation aff)
		{
			if (action == 0) {  }
			if (action == 1) {  }
			if (action == 2) {  }
		}

		if (this.objet instanceof Attribution att)
		{
			if (action == 0) {  }
			if (action == 1) {  }
			if (action == 2) {  }
		}

		if (this.objet instanceof Module m)
		{
			if (action == 0) {  }
			if (action == 1) {  }
			if (action == 2) {  }
		}

		if (this.objet instanceof Semestre s)
		{
			if (action == 0) {  }
			if (action == 1) {  }
			if (action == 2) {  }
		}

		if (this.objet instanceof Annee a)
		{
			if (action == 0) {  }
			if (action == 1) {  }
			if (action == 2) {  }
		}

		if (this.objet instanceof Intervenant inter)
		{
			if (action == 0) {  }
			if (action == 1) {  }
			if (action == 2) {  }
		}

		if (this.objet instanceof CategorieIntervenant categorieIntervenant)
		{
			if (action == 0) {  }
			if (action == 1) {  }
			if (action == 2) {  }
		}

		if (this.objet instanceof CategorieHeure categorieHeure)
		{
			if (action == 0) {  }
			if (action == 1) {  }
			if (action == 2) {  }
		}
	}

	public int compareTo(Action o)
	{
		return this.lvl - o.lvl;
	}

	public String toString()
	{
		return String.format("Lvl: %d [db: %-5s] Obj: %s", this.lvl, this.affecteDB, this.objet);
	}
}
