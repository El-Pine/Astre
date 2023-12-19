package fr.elpine.astre.metier.objet;

import java.util.ArrayList;

public class Annee
{

    private String numero;
    private String dateDeb;
    private String dateFin;

    private ArrayList<Semestre> ensSemestre;

    public Annee(String numero, String dateDeb, String dateFin)
    {
        this.numero      = numero;
        this.dateDeb     = dateDeb;
        this.dateFin     = dateFin;
        this.ensSemestre = new ArrayList<>();
    }

    public String getNumero  () { return numero;  }
    public String getDateDeb () { return dateDeb; }
    public String getDateFin () { return dateFin; }

    public void setNumero  (String numero  ) {this.numero = numero;}
    public void setDateDeb (String dateDeb ) {this.dateDeb = dateDeb;}
    public void setDateFin (String dateFin ) {this.dateFin = dateFin;}

    public void ajouterSemestre(Semestre sem)
    {
        if(sem != null)
        {
            this.ensSemestre.add(sem);
        }
    }
    public Semestre getSemestre(int i){ return this.ensSemestre.get(i); }
}
