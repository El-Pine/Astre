package fr.elpine.astre.metier;

import fr.elpine.astre.metier.objet.*;
import fr.elpine.astre.metier.objet.Module;

public class Action implements Comparable<Action>
{
	private int lvl;
	private int action;
	private boolean rollback;
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
	 *   0 : delete
	 *   1 : insert
	 *   2 : update
	 *
	* */
	public Action( boolean rollback, int action, Object objet, boolean affecteDB)
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
		this.rollback  = rollback;
		this.affecteDB = affecteDB;
		this.objet     = objet;
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
