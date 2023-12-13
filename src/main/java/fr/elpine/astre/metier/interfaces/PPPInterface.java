package fr.elpine.astre.metier.interfaces;

import fr.elpine.astre.metier.objet.PPP;

import java.util.List;

public interface PPPInterface
{
    void ajoutPPP(PPP ppp);
    void majPPP(PPP ppp);
    void supprPPP(String code);
    List<PPP> getPPPs();
}
