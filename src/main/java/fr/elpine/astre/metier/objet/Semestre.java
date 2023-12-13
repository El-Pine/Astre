package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;

import java.util.ArrayList;
import java.util.HashMap;

public class Semestre {

    private int numero;
    private ArrayList<Module> alModules;
    private int service ;
    private double ratioTD ;

    public Semestre(int numero, int service, double ratioTD) {
        this.numero = numero;
        this.alModules = new ArrayList<Module>();
        this.service = service;
        this.ratioTD = ratioTD;
    }
    public int getNumero() {return numero;}
    public void setNumero(int numero) {this.numero = numero;}
    public boolean estPair(){ return this.numero % 2 == 0;}
    public ArrayList<Module> getAlModules() {return alModules;}
    public void ajouterModule(Module module) {this.alModules.add(module);}
    public void supprModule(Module module) {this.alModules.remove(module);}

}
