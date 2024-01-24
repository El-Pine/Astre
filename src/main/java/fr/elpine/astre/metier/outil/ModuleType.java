package fr.elpine.astre.metier.outil;

public enum ModuleType {
	RESSOURCE("ressource", "Ressource", "R"),
	SAE("sae", "SAÉ", "S"),
	PPP("ppp", "PPP", "P"),
	STAGE("stage", "Stage / Suivi", "ST");

	private final String nom;
	private final String prefix;
	private final String label;

	ModuleType(String nom, String label, String prefix)
	{
		this.nom    = nom;
		this.label  = label;
		this.prefix = prefix;
	}

	public String getNom()    { return this.nom;    }
	public String getLabel()  { return this.label;  }
	public String getPrefix() { return this.prefix; }
	public String toString()  { return this.label;  }
}
