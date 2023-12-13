package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;

import java.util.ArrayList;
import java.util.HashMap;

public class Semestre {

    private int numero;
    private boolean estPair;
    private HashMap<Double, ArrayList<Intervenant>> HashIntervenant;
    private int service ;
    private double ratioTD ;

    public Semestre(int numero, boolean estPair, HashMap<Double, ArrayList<Intervenant>> hashIntervenant, int service, double ratioTD) {
        this.numero = numero;
        this.estPair = estPair;
        HashIntervenant = hashIntervenant;
        this.service = service;
        this.ratioTD = ratioTD;
    }

    public int getNumero() {
        return numero;
    }

    public boolean estPair(){ return this.numero % 2 == 0;}

    public HashMap<Double, ArrayList<Intervenant>> getHashIntervenant() {
        return HashIntervenant;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setHashIntervenant(HashMap<Double, ArrayList<Intervenant>> hashIntervenant) {
        HashIntervenant = hashIntervenant;
    }

    public void addIntervenant(Intervenant intervenant, double nbHeure)
    {
        if(this.HashIntervenant.containsKey(nbHeure))
        {
            this.HashIntervenant.get(nbHeure).add(intervenant);
        }
        else
        {
            ArrayList<Intervenant> list = new ArrayList<Intervenant>();
            list.add(intervenant);
            this.HashIntervenant.put(nbHeure,list);
        }
    }

}
