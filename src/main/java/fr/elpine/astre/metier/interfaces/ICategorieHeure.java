package fr.elpine.astre.metier.interfaces;

import fr.elpine.astre.metier.objet.CategorieHeure;

import java.util.List;

public interface ICategorieHeure {
    void ajoutCatHeure(CategorieHeure catHeure);
    void majCatHeure(CategorieHeure catHeure);
    void supprCatHeure(String code);
    CategorieHeure getcatHeurebyCode(String code);
    List<CategorieHeure> getCatHeure();
}
