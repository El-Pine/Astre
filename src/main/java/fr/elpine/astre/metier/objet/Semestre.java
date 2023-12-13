package fr.elpine.astre.metier.objet;

import fr.elpine.astre.Controleur;

import java.util.ArrayList;
import java.util.HashMap;

public class Semestre {

    private int numero;
    private int nbHeureTotPlacer;

    public Semestre (int numero, int nbHeureTotPlacer)
    {
        this.numero           = numero;
        this.nbHeureTotPlacer = nbHeureTotPlacer;
    }
    public int getNumero           () { return numero;           }
    public int getNbHeureTotPlacer () { return nbHeureTotPlacer; }

    public void setNumero          ( int numero          ) { this.numero = numero;                     }
    public void setNbHeureTotPlacer( int nbHeureTotPlacer) { this.nbHeureTotPlacer = nbHeureTotPlacer; }
}
