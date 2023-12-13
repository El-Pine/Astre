package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;

import java.util.ArrayList;
import java.util.HashMap;

public class Semestre {

    private int numero;
    private ArrayList<Module> alModule;
    private int service ;
    private double ratioTD ;

    public Semestre(int numero, int service, double ratioTD) {
        this.numero = numero;
        this.alModule = new ArrayList<Module>();
        this.service = service;
        this.ratioTD = ratioTD;
    }

    public int getNumero() {
        return numero;
    }
    public void setNumero(int numero) {
        this.numero = numero;
    }
    public boolean estPair(){ return this.numero % 2 == 0;}
    public ArrayList<Module> getAlModules() {return alModule;}
    public void ajoutModule(Module module) {this.alModule.add(module);}
    public void supprModule(Module module) {this.alModule.remove(module);}
}
