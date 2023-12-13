package fr.elpine.astre.metier.interfaces;

import fr.elpine.astre.metier.objet.Module;
import java.util.*;

public interface IModule {
    void ajoutModule(Module module);
    void majModule(Module module);
    void supprModule(String code);

    ArrayList<Module> getModules();
}
