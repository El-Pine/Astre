package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;
import fr.elpine.astre.metier.Astre;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class Annee
{
    private String nom;
    private Date dateDeb;
    private Date dateFin;

    private ArrayList<Semestre> ensSemestre;

    private boolean                 ajoute;
    private boolean                 supprime;
    private boolean                 modifie;
    private HashMap<String, Object> rollbackDatas;

    public Annee(String nom, Date dateDeb, Date dateFin)
    {
        this.nom         = nom;
        this.dateDeb     = dateDeb;
        this.dateFin     = dateFin;

        this.ensSemestre = new ArrayList<>();

        this.ajoute = Controleur.get().getMetier() != null;
        this.modifie = false;
        this.supprime = false;

        this.setRollback();
    }

    public Annee(String nom, String dateDeb, String dateFin)
    {
        // Les dates doivent être au format 2023-12-31
        this(nom, Date.valueOf(dateDeb), Date.valueOf(dateFin));
    }

    public String getNom() { return nom;  }
    public Date getDateDeb () { return dateDeb; }
    public Date getDateFin () { return dateFin; }

    public void setDateDeb (Date dateDeb ) { this.dateDeb = dateDeb; this.modifie = true;  }
    public void setDateFin (Date dateFin ) { this.dateFin = dateFin; this.modifie = true;  }

    /* Synchronisation */
    public boolean isAjoute() { return this.ajoute; }
    public boolean isSupprime() { return this.supprime; }
    public boolean isModifie() { return this.modifie; }
    public void reset() { this.ajoute = false; this.supprime = false; this.modifie = false; this.setRollback(); }


    public Annee ajouterSemestre(Semestre semestre)
    {
        if (semestre != null && !this.ensSemestre.contains(semestre))
            this.ensSemestre.add(semestre);

        return this;
    }

    public void supprimerSemestre(Semestre semestre) // call uniquement si l'objet Semestre a deja ete supprimer a 100%
    {
        if (semestre == null || !this.ensSemestre.contains(semestre)) return;

        this.ensSemestre.remove(semestre);
    }

    public ArrayList<Semestre> getSemestres() { return this.ensSemestre; }


    public boolean supprimer( boolean recursive )
    {
        for (Semestre semestre : this.ensSemestre)
            if (!recursive) return false;
            else semestre.supprimer(true);

        // supprimer l'element
        if (Controleur.get().getMetier().getAnneeActuelle() == this) Controleur.get().getMetier().changerAnneeActuelle(null);
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
                                attribution.getNbHeurePN(),
                                attribution.getNbHeure(),
                                attribution.getNbSemaine(),
                                m,
                                attribution.getCatHr()
                        ));
                    } else {
                        astre.ajouterAttribution(new Attribution(
                                attribution.getNbHeurePN(),
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

    public void rollback()
    {
        if (this.rollbackDatas == null) return;

        this.dateDeb = (Date) this.rollbackDatas.get("dateDeb");
        this.dateFin = (Date) this.rollbackDatas.get("dateFin");

        this.rollbackDatas.clear();
    }

    private void setRollback()
    {
        if (this.rollbackDatas == null) this.rollbackDatas = new HashMap<>(); else this.rollbackDatas.clear();

        this.rollbackDatas.put("dateDeb", this.dateDeb);
        this.rollbackDatas.put("dateFin", this.dateFin);
    }


    @Override
    public String toString() {
        return this.nom;
    }
}
