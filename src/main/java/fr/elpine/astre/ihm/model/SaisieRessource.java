package fr.elpine.astre.ihm.model;

public class SaisieRessource
{
	private String intervenant;
	private String type;
	private int nbH;
	private double totalEqtd;
	private String commentaire;

	public SaisieRessource(String intervenant, String type, int nbH, double totalEqtd, String commentaire) {
		this.intervenant = intervenant;
		this.type = type;
		this.nbH = nbH;
		this.totalEqtd = totalEqtd;
		this.commentaire = commentaire;
	}

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
