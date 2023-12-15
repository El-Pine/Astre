package fr.elpine.astre.metier.objet;

public class AffectationSae
{
	private String intervenant;
	private String type;
	private int nbH;
	private double totalEqtd;
	private String commentaire;
	private int nbSemaine;
	private int nbGp;

	public AffectationSae(String intervenant, String type, int nbSemaine, int nbH, int nbGp, double totalEqtd, String commentaire) {
		this.intervenant = intervenant;
		this.type = type;
		this.nbH = nbH;
		this.totalEqtd = totalEqtd;
		this.commentaire = commentaire;
		this.nbSemaine = nbSemaine;
		this.nbGp = nbGp;
	}

	public int getNbSemaine() {return nbSemaine;}

	public int getNbGp() {return nbGp;}

	public void setNbGp(int nbGp) {this.nbGp = nbGp;}

	public void setNbSemaine(int nbSemaine) {this.nbSemaine = nbSemaine;}

	public String getIntervenant() {
		return intervenant;
	}

	public void setIntervenant(String intervenant) {
		this.intervenant = intervenant;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNbH() {
		return nbH;
	}

	public void setNbH(int nbH) {
		this.nbH = nbH;
	}

	public double getTotalEqtd() {
		return totalEqtd;
	}

	public void setTotalEqtd(double totalEqtd) {
		this.totalEqtd = totalEqtd;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
}
