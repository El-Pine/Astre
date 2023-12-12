package fr.elpine.astre.metier.interfaces;

import fr.elpine.astre.metier.objet.SAE;

import java.util.List;

public interface SAEInterface {
    void ajoutSAE(SAE sae);
    void majSAE(SAE sae);
    void supprSAE(String code);
    SAE getSAEbyCode(String code);
    List<SAE> getSAE();
}
