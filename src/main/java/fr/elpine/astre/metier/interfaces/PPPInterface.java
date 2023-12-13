package fr.elpine.astre.metier.interfaces;

import fr.elpine.astre.metier.objet.Module;

import java.util.List;

public interface PPPInterface
{
    void ajoutPPP(Module module);
    void majPPP(Module module);
    void supprPPP(int moduleId);
    Module getPPPbyNom(String moduleNom);
    List<Module> getPPPs();
}
