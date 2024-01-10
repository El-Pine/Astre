-- Suppression des anciennes tables

DROP TABLE IF EXISTS Affectation;
DROP TABLE IF EXISTS Attribution;
DROP TABLE IF EXISTS Intervenant;
DROP TABLE IF EXISTS CategorieIntervenant;
DROP TABLE IF EXISTS CategorieHeure;
DROP TABLE IF EXISTS Module;
DROP TABLE IF EXISTS Semestre;
DROP TABLE IF EXISTS Annee;

-- Création des tables

CREATE TABLE Annee (
	nom VARCHAR(255) PRIMARY KEY,
	debut DATE,
	fin DATE
);

CREATE TABLE Semestre (
	numero INT,
	annee VARCHAR(255),
	nbGrpTD INT,
	nbGrpTP INT,
	nbEtd INT,
	nbSemaine INT,

	FOREIGN KEY (annee) REFERENCES Annee(nom),

	PRIMARY KEY (numero, annee)
);

CREATE TABLE Module (
	code VARCHAR(255),
	numeroSemestre INT,
	annee VARCHAR(255),
	nom VARCHAR(255),
	abreviation VARCHAR(255),
    typeModule VARCHAR(255),
	couleur VARCHAR(16),
	validation BOOLEAN,

	FOREIGN KEY (numeroSemestre, annee) REFERENCES Semestre(numero, annee),

	PRIMARY KEY (code, numeroSemestre, annee)
);

CREATE TABLE CategorieHeure (
	nom VARCHAR(255) PRIMARY KEY,
	eqtd TEXT,

	ressource BOOLEAN,
	sae BOOLEAN,
	ppp BOOLEAN,
	stage BOOLEAN,

	hebdo BOOLEAN,
);

CREATE TABLE CategorieIntervenant (
	code VARCHAR(255) PRIMARY KEY,
	nom VARCHAR(255),
	nbHeureMaxDefaut TEXT,
	nbHeureServiceDefaut TEXT,
	ratioTPDefaut TEXT
);

CREATE TABLE Intervenant (
	idInter SERIAL PRIMARY KEY,
	nom VARCHAR(255),
	prenom VARCHAR(255),
	mail varchar(255),
	codeCategorie VARCHAR(255),
	heureService TEXT,
	heureMax TEXT,
	ratioTP TEXT,

	FOREIGN KEY (codeCategorie) REFERENCES CategorieIntervenant(code)
);

-- Associations

CREATE TABLE Attribution (
	codeModule VARCHAR(255),
	numeroSemestreModule INT,
	anneeModule VARCHAR(255),
	nomCategorieHeure VARCHAR(255),
	nbHeure TEXT,
	nbSemaine INT,
    nbHeurePN TEXT,

	FOREIGN KEY (codeModule, numeroSemestreModule, anneeModule) REFERENCES Module(code, numeroSemestre, annee),
	FOREIGN KEY (nomCategorieHeure)                             REFERENCES CategorieHeure(nom),

    PRIMARY KEY (codeModule, numeroSemestreModule, anneeModule, nomCategorieHeure)
);

CREATE TABLE Affectation (
    idAffectation SERIAL,
	codeModule VARCHAR(255),
    numeroSemestreModule INT,
    anneeModule VARCHAR(255),
    idInter INT,

    typeHeure   VARCHAR(255),
    nbGroupe    INT,
    nbSemaine   INT,
    nbHeure     TEXT,
    commentaire TEXT,

	FOREIGN KEY (codeModule, numeroSemestreModule, anneeModule) REFERENCES Module(code, numeroSemestre, annee),
	FOREIGN KEY (typeHeure)                                     REFERENCES CategorieHeure(nom),

    PRIMARY KEY (codeModule, numeroSemestreModule, anneeModule, idInter, idAffectation)
);
