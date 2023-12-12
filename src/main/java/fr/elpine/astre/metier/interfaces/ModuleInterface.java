package fr.elpine.astre.metier.interfaces;

import fr.elpine.astre.metier.objet.Module;
import java.util.*;

public interface ModuleInterface {
    void ajoutModule(Module module);
    void majModule(Module module);
    void supprModule(int moduleId);
    Module getModulebyNom(String moduleNom);
    List<Module> getModules();
}
