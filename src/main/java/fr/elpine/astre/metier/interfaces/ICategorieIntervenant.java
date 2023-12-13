package fr.elpine.astre.metier.interfaces;

import fr.elpine.astre.metier.objet.CategorieIntervenant;

import java.util.List;

public interface ICategorieIntervenant {
    void ajoutCatInter(CategorieIntervenant catInter);
    void majCatInter(CategorieIntervenant catInter);
    void supprCatInter(String code);
    CategorieIntervenant getcatInterbyCode(String code);
    List<CategorieIntervenant> getCatInter();
}
