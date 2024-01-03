package fr.elpine.astre.metier.outil;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.Astre;
import fr.elpine.astre.metier.DB;
import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.objet.Module;

public class Action implements Comparable<Action>
{
	private final int     lvl;
	private final int     action;
	private final boolean affecteDB;
	private final Object  objet;

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
			if (action == -1) {
				db.majCodeModule( m );
			} else {
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

		if (this.objet instanceof Affectation aff)
		{
			if (action == 0) {
				aff.getModule().supprimerAffectation(aff);
				aff.getIntervenant().supprimerAffectation(aff);

				metier.getAffectations().remove(aff);
			}
			if (action == 1) { aff.rollback(); aff.reset(); }
			if (action == 2) { aff.reset(); }
		}

		if (this.objet instanceof Attribution att)
		{
			if (action == 0) {
				att.getModule().supprimerAttribution( att );
				att.getCatHr ().supprimerAttribution( att );

				metier.getAttributions().remove( att );
			}
			if (action == 1) { att.rollback(); att.reset(); }
			if (action == 2) { att.reset(); }
		}

		if (this.objet instanceof Module m)
		{
			if (action == 0) {
				m.getSemestre().supprimerModule( m );

				metier.getModules().remove( m );
			}
			if (action == 1) { m.rollback(); m.reset(); }
			if (action == 2) { m.reset(); }
		}

		if (this.objet instanceof Semestre s)
		{
			if (action == 0) {
				s.getAnnee().supprimerSemestre( s );

				metier.getSemestres().remove( s );
			}
			if (action == 1) { s.rollback(); s.reset(); }
			if (action == 2) { s.reset(); }
		}

		if (this.objet instanceof Annee a)
		{
			if (action == 0) {
				metier.getAnnees().remove( a );
			}
			if (action == 1) { a.rollback(); a.reset(); }
			if (action == 2) { a.reset(); }
		}

		if (this.objet instanceof Intervenant inter)
		{
			if (action == 0) {
				metier.getIntervenants().remove( inter );
			}
			if (action == 1) { inter.rollback(); inter.reset(); }
			if (action == 2) { inter.reset(); }
		}

		if (this.objet instanceof CategorieIntervenant categorieIntervenant)
		{
			if (action == 0) {
				metier.getCategorieIntervenants().remove( categorieIntervenant );
			}
			if (action == 1) { categorieIntervenant.rollback(); categorieIntervenant.reset(); }
			if (action == 2) { categorieIntervenant.reset(); }
		}

		if (this.objet instanceof CategorieHeure categorieHeure)
		{
			if (action == 0) {
				metier.getCategorieHeures().remove( categorieHeure );
			}
			if (action == 1) { categorieHeure.rollback(); categorieHeure.reset(); }
			if (action == 2) { categorieHeure.reset(); }
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
