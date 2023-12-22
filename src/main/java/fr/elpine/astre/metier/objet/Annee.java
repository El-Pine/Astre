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

    private boolean ajoute;
    private boolean supprime;
    private boolean modifie;

    public Annee(String nom, String dateDeb, String dateFin)
    {
        this.nom         = nom;
        this.dateDeb     = dateDeb;
        this.dateFin     = dateFin;

        this.ensSemestre = new ArrayList<>();

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;
    }

    public String getNom() { return nom;  }
    public String getDateDeb () { return dateDeb; }
    public String getDateFin () { return dateFin; }

    public void setDateDeb (String dateDeb ) { this.dateDeb = dateDeb; this.modifie = true;  }
    public void setDateFin (String dateFin ) { this.dateFin = dateFin; this.modifie = true;  }

    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }


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


    public boolean supprimer( boolean recursive )
    {
        for (Semestre semestre : this.ensSemestre)
            if (!recursive) return false;
            else semestre.supprimer(true);

        // supprimer l'element
        return this.supprime = true;
    }

    public Annee dupliquer( String nom )
    {
        Astre astre = Controleur.get().getMetier();
        Annee a     = astre.ajouterAnnee( new Annee( nom, this.dateDeb, this.dateFin) );

        for ( Semestre semestre : this.ensSemestre)
        {
            Semestre s = astre.ajouterSemestre( new Semestre(
                    semestre.getNumero(),
                    semestre.getNbGrpTD(),
                    semestre.getNbGrpTP(),
                    semestre.getNbEtd(),
                    semestre.getNbSemaine(),
                    a
            ));

            for (Module module : semestre.getModules())
            {
                Module m = astre.ajouterModule( new Module(
                        module.getNom(),
                        module.getCode(),
                        module.getAbreviation(),
                        module.getTypeModule(),
                        module.getCouleur(),
                        module.estValide(),
                        s
                ));

                for (Attribution attribution : module.getAttributions())
                {
                    if (attribution.hasNbSemaine()) {
                        astre.ajouterAttribution(new Attribution(
                                attribution.getNbHeure(),
                                attribution.getNbSemaine(),
                                m,
                                attribution.getCatHr()
                        ));
                    } else {
                        astre.ajouterAttribution(new Attribution(
                                attribution.getNbHeure(),
                                m,
                                attribution.getCatHr()
                        ));
                    }
                }

                for (Affectation affectation : module.getAffectations())
                {
                    if (affectation.hasGrpAndNbSemaine()) {
                        astre.ajouterAffectation(new Affectation(
                                m,
                                affectation.getIntervenant(),
                                affectation.getTypeHeure(),
                                affectation.getNbGroupe(),
                                affectation.getNbSemaine(),
                                affectation.getCommentaire()
                        ));
                    } else {
                        astre.ajouterAffectation(new Affectation(
                                m,
                                affectation.getIntervenant(),
                                affectation.getTypeHeure(),
                                affectation.getNbHeure(),
                                affectation.getCommentaire()
                        ));
                    }
                }
            }
        }

        return a;
    }


    @Override
    public String toString() {
        return "Annee{" +
                "nom='" + nom + '\'' +
                ", dateDeb='" + dateDeb + '\'' +
                ", dateFin='" + dateFin + '\'' +
                ", ensSemestre=" + ensSemestre +
                '}';
    }
}
