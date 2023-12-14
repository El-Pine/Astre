package fr.elpine.astre.metier.objet;

public class Annee
{

    private String numero;
    private String dateDeb;
    private String dateFin;

    public Annee(String numero, String dateDeb, String dateFin)
    {
        this.numero = numero;
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
    }

    public String getNumero  () { return numero;  }
    public String getDateDeb () { return dateDeb; }
    public String getDateFin () { return dateFin; }

    public void setNumero  (String numero  ) {this.numero = numero;}
    public void setDateDeb (String dateDeb ) {this.dateDeb = dateDeb;}
    public void setDateFin (String dateFin ) {this.dateFin = dateFin;}
}
