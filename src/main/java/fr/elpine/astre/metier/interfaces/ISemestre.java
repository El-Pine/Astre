package fr.elpine.astre.metier.interfaces;

import fr.elpine.astre.metier.objet.Semestre;
import java.util.*;

public interface ISemestre {
    void ajoutSemestre(Module module);
    void majSemestre(Module module);
    void supprSemestre(String code);
    Module getModuleId(int moduleId);
    ArrayList<Module> getModules();
    void supprModule(int moduleId);
    void ajoutModule(Module module);
    void majModule(Module module);

    ArrayList<Semestre> getSemestres();
}


