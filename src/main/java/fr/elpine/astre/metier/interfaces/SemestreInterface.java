package fr.elpine.astre.metier.interfaces;

import fr.elpine.astre.metier.objet.Semestre;
import java.util.*;

public interface SemestreInterface {
    void ajoutSemestre(Module module);
    void majSemestre(Module module);
    Module getModuleId(int moduleId);

    List<Semestre> getSemestres();
}


