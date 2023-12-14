DROP TABLE IF EXISTS CategorieIntervenant;
DROP TABLE IF EXISTS Intervenant;
DROP TABLE IF EXISTS Semestre;
DROP TABLE IF EXISTS Ressource;
DROP TABLE IF EXISTS SAE;
DROP TABLE IF EXISTS Stage;
DROP TABLE IF EXISTS typeHeure;
DROP TABLE IF EXISTS PPP;

CREATE TABLE CategorieIntervenant (
    code VARCHAR(255) PRIMARY KEY,
    nom VARCHAR(255),
    nbHeureMax INT,
    service INT,
    ratioTP FLOAT,
    estCompleter BOOLEAN,
);

CREATE TABLE CategorieHeure(
    nom VARCHAR(255) PRIMARY KEY,
    nbHeureMax INT,
    service INT,
    ratioTP FLOAT,
    estCompleter BOOLEAN,
);

CREATE TABLE Intervenant (
    nom VARCHAR(255) PRIMARY KEY,
    prenom VARCHAR(255) PRIMARY KEY,
    mail varchar(255),
    statut varchar(255),
    service varchar(255),
    total FLOAT,
);


-- liaisons internvenant avec sa categorie
CREATE TABLE Intervenant_CategorieIntervenant (
    intervenant_nom VARCHAR(255),
    intervenant_prenom VARCHAR(255),
    categorieIntervenant_code VARCHAR(255),
    FOREIGN KEY (intervenant_nom, intervenant_prenom) REFERENCES Intervenant(nom, prenom),
    FOREIGN KEY (categorieIntervenant_code) REFERENCES CategorieIntervenant(code),
    PRIMARY KEY (intervenant_nom, intervenant_prenom, categorieIntervenant_code)
);

-- liaisons intervenants avec ses semestres
CREATE TABLE Intervenant_Semestre (
    intervenant_nom VARCHAR(255),
    intervenant_prenom VARCHAR(255),
    semestre_id INT,
    FOREIGN KEY (intervenant_nom, intervenant_prenom) REFERENCES Intervenant(nom, prenom),
    FOREIGN KEY (semestre_id) REFERENCES Semestre(id),
    PRIMARY KEY (intervenant_nom, intervenant_prenom, semestre_id)
);

CREATE TABLE Module (
    code VARCHAR(255) PRIMARY KEY,
    nom VARCHAR(255),
    commentaire VARCHAR(255),
    nb_heure_pn_sem INT,
    nb_heure_tut INT,
    nb_heure INT
);

CREATE TABLE Semestre (
  numero INT PRIMARY KEY,
  estPair BOOLEAN,
  service INT
);

--liaison module avec semestre
CREATE TABLE Semestre_Module (
     semestre_id INT,
     module_id INT,
     FOREIGN KEY (semestre_id) REFERENCES Semestre(id),
     FOREIGN KEY (module_id) REFERENCES Module(id),
     PRIMARY KEY (semestre_id, module_id)
);

--liaison module avec categorieHeure
CREATE TABLE Module_CategorieHeure (
     module_id INT,
     categorieHeure_id INT,
     FOREIGN KEY (module_id) REFERENCES Module(id),
     FOREIGN KEY (categorieHeure_id) REFERENCES CategorieHeure(id),
     PRIMARY KEY (module_id, categorieHeure_id)
);

CREATE TABLE SAE (
    code VARCHAR(255) PRIMARY KEY,
    nom VARCHAR,
    commentaire VARCHAR,
    nb_heure_pn_sem INT,
    nb_heure_tut INT,
    nb_heure INT
);

CREATE TABLE Stage (
    code VARCHAR(255) PRIMARY KEY,
    nom VARCHAR(255),
    commentaire VARCHAR(255),
    nb_heure_pn_sem INT,
    nb_heure_tut INT,
    nb_heure INT
);

CREATE TABLE PPP(
    -- Module
    code VARCHAR(255) PRIMARY KEY,
    nom VARCHAR(255),
    commentaire VARCHAR(255),
    --PPP
    nbHeureCM INT,
    nbHeureTD INT,
    nbHeureTP INT,
    nbHeureTuto INT,
    nbHeurePonct INT
);


CREATE TABLE Ressource (
    code VARCHAR(255) PRIMARY KEY,
    nom VARCHAR(255),
    commentaire VARCHAR(255),
    nb_heure_sem INT,
    semestre INT REFERENCES Semestre(numero),
    nb_heure_tl INT,
    nb_grp INT,
    nb_semaine INT
);