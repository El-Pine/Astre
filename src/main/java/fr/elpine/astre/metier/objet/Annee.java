package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.Astre;

import java.util.ArrayList;

public class Annee
{
    private String nom;
    private String dateDeb;
    private String dateFin;

    private ArrayList<Semestre> ensSemestre;

    public Annee(String nom, String dateDeb, String dateFin)
    {
        this.nom         = nom;
        this.dateDeb     = dateDeb;
        this.dateFin     = dateFin;

        this.ensSemestre = new ArrayList<>();
    }

    public String getNom() { return nom;  }
    public String getDateDeb () { return dateDeb; }
    public String getDateFin () { return dateFin; }

    public void setNom(String nom) { this.nom = nom; }
    public void setDateDeb (String dateDeb ) { this.dateDeb = dateDeb; }
    public void setDateFin (String dateFin ) { this.dateFin = dateFin; }


    public void ajouterSemestre(Semestre semestre)
    {
        if (semestre != null && !this.ensSemestre.contains(semestre))
        {
            this.ensSemestre.add(semestre);

            semestre.setAnnee(this);
        }
    }

    public void supprimerSemestre(Semestre semestre)
    {
        if (semestre == null || !this.ensSemestre.contains(semestre)) return;

        this.ensSemestre.remove(semestre);

        semestre.setAnnee(null);
    }

    public ArrayList<Semestre> getSemestres() { return this.ensSemestre; }


    public void supprimer()
    {
        Astre metier = Controleur.get().getMetier();

        for (Semestre semestre : this.getSemestres())
        {
            for (Module module : semestre.getModules())
            {
                for (Affectation affectation : module.getAffectations())
                {
                    affectation.supprimer();
                }

                for (Attribution attribution : module.getAttributions())
                {
                    attribution.supprimer();
                }
            }
        }
    }
}
