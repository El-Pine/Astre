package fr.elpine.astre.metier.interfaces;

import fr.elpine.astre.metier.objet.Semestre;
import java.util.*;

public interface ISemestre {
    void ajoutSemestre(Semestre module);
    void majSemestre(Semestre module);
    void supprSemestre(String code);
    Semestre getSemestreByNumero(int semestreId);

    ArrayList<Semestre> getSemestres();
}


