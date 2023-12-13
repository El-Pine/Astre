DROP TABLE IF EXISTS CategorieIntervenant;
DROP TABLE IF EXISTS Intervenant;
DROP TABLE IF EXISTS Semestre;
DROP TABLE IF EXISTS Ressource;
DROP TABLE IF EXISTS SAE;
DROP TABLE IF EXISTS Stage;
DROP TABLE IF EXISTS typeHeure;


CREATE TABLE CategorieIntervenant (
    code VARCHAR(255) PRIMARY KEY,
    nom VARCHAR(255),
    nbHeureMax INT,
    service VARCHAR(255),
    ratioTP DECIMAL(5, 2),
    estCompléter BOOLEAN,
);

CREATE TABLE Intervenant (
    nom VARCHAR(255) PRIMARY KEY,
    prenom VARCHAR(255) PRIMARY KEY,
    mail varchar(255),
    total FLOAT
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

CREATE TABLE typeHeure (
    nom VARCHAR(255) PRIMARY KEY,
    ratioTd DECIMAL(5, 2)
);

CREATE TABLE Semestre (
    numero INT PRIMARY KEY,
    estPair BOOLEAN,
    service INT
);

CREATE TABLE Ressource (
    code VARCHAR(255) PRIMARY KEY,
    nom VARCHAR(255),
    commentaire VARCHAR(255),
    nb_heure_sem INT,
    semestre INT REFERENCES Semestre(numero),
    nb_heure_ttl INT,
    nb_grp INT,
    nb_semaine INT
);

