package fr.elpine.astre.metier.objet;

public class PPP extends Module {
    private int nbHeureCM;
    private int nbHeureTD;
    private int nbHeureTP;
    private int nbHeureTuto;
    private int nbHeurePonct;

    public PPP(String nom, String code, String commentaire, int nbHeureCM, int nbHeureTD, int nbHeureTP, int nbHeureTuto, int nbHeurePonct) {

        super(nom, code, commentaire);

        this.nbHeureCM    = nbHeureCM;
        this.nbHeureTD    = nbHeureTD;
        this.nbHeureTP    = nbHeureTP;
        this.nbHeureTuto  = nbHeureTuto;
        this.nbHeurePonct = nbHeurePonct;

    }

    public void setNbHeureCM(int nbHeureCM) {
        this.nbHeureCM = nbHeureCM;
    }

    public void setNbHeureTD(int nbHeureTD) {
        this.nbHeureTD = nbHeureTD;
    }

    public void setNbHeureTP(int nbHeureTP) {
        this.nbHeureTP = nbHeureTP;
    }

    public void setNbHeureTuto(int nbHeureTuto) {
        this.nbHeureTuto = nbHeureTuto;
    }

    public void setNbHeurePonct(int nbHeurePonct) {
        this.nbHeurePonct = nbHeurePonct;
    }

    public int getNbHeureCM() {
        return nbHeureCM;
    }

    public int getNbHeureTD() {
        return nbHeureTD;
    }

    public int getNbHeureTP() {
        return nbHeureTP;
    }

    public int getNbHeureTuto() {
        return nbHeureTuto;
    }

    public int getNbHeurePonct() {
        return nbHeurePonct;
    }

    public int getNbHeureTotal() {
        return  nbHeureCM + nbHeureTD + nbHeureTP + nbHeureTuto + nbHeurePonct;
    }

}
