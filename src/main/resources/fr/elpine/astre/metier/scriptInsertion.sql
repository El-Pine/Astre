-- Insertion des données dans la table Annee
INSERT INTO Annee (nom, debut, fin) VALUES
    ('2022-2023', '2022-09-01', '2023-08-31'),
    ('2023-2024', '2023-09-01', '2024-08-31'),
    ('2024-2025', '2024-09-01', '2025-08-31');

-- Insertion des données dans la table Semestre (pour 6 semestres par année)
INSERT INTO Semestre (numero, annee, nbGrpTD, nbGrpTP, nbEtd, nbSemaine) VALUES
    (1, '2022-2023', 4, 2, 70, 20),
    (2, '2022-2023', 1, 6, 20, 10),
    (3, '2022-2023', 9, 2, 90, 17),
    (4, '2022-2023', 1, 1, 23, 19),
    (5, '2022-2023', 3, 8, 10, 15),
    (6, '2022-2023', 2, 10, 18, 4),
    (1, '2023-2024', 1, 1, 15, 12),
    (2, '2023-2024', 2, 3, 13, 11),
    (3, '2023-2024', 4, 2, 1, 8),
    (4, '2023-2024', 2, 4, 22, 2),
    (5, '2023-2024', 3, 1, 12, 26),
    (6, '2023-2024', 4, 2, 13, 12);

-- Insertion des données dans la table Module
INSERT INTO Module (code, numeroSemestre, annee, nom, abreviation, couleur, typeModule, validation) VALUES
    ('R1.01', 1, '2022-2023', 'Mathématiques', 'MATH', 'rgb(255,255,255)', 'ressource', true),
    ('R1.02', 1, '2022-2023', 'Analyse', 'ANA', 'rgb(255,255,255)', 'ressource', true),
    ('R1.03', 1, '2022-2023', 'Physique', 'PHY', 'rgb(255,255,255)', 'ressource', true),
    ('R2.01', 2, '2022-2023', 'Algèbre', 'ALG', 'rgb(255,255,255)', 'ressource', false),
    ('R2.02', 2, '2022-2023', 'Géométrie', 'GEO', 'rgb(255,255,255)', 'ressource', false),
    ('R2.03', 2, '2023-2024', 'Optique', 'OPT', 'rgb(255,255,255)', 'ressource', false);

-- Insertion des données dans la table CategorieHeure
INSERT INTO CategorieHeure (nom, eqtd, ressource, sae, ppp, stage) VALUES
    ('CM', '3/2', true, false, false, false),
    ('TD', '1'  , true, false, false, false),
    ('TP', '1'  , true, false, false, false),
    ('H tut', '1', false, true, true, true),
    ('REH', '1', false, false, true, true),
    ('H Saé', '1', false, true, false, false),
    ('HP', '1', true, false, false, false);

-- Insertion des données dans la table CategorieIntervenant
INSERT INTO CategorieIntervenant (code, nom, nbHeureMaxDefaut, nbHeureServiceDefaut, ratioTPDefaut) VALUES
    ('CAT1', 'Enseignant', '20', '15', '1/2'),
    ('CAT2', 'Chercheur', '30', '25', '3/5');

-- Insertion des données dans la table Intervenant
INSERT INTO Intervenant (nom, prenom, mail, codeCategorie, heureService, heureMax, ratioTP) VALUES
    ('Dupont', 'Jean', 'jean.dupont@example.com', 'CAT1', '18', '20', '2/5'),
    ('Martin', 'Alice', 'alice.martin@example.com', 'CAT2', '25', '30', '3/5');

-- Insertion des données dans la table Attribution
INSERT INTO Attribution (codeModule, numeroSemestreModule, anneeModule, nomCategorieHeure, nbHeure, nbSemaine, nbHeurePN) VALUES
    ('MATH101', 1, '2022-2023', 'CM', '30', '12', '5'),
    ('MATH102', 1, '2022-2023', 'TD', '15', '12', '5'),
    ('PHYS101', 1, '2022-2023', 'TP', '20', '12', '5');

-- Insertion des données dans la table Affectation
INSERT INTO Affectation (codeModule, numeroSemestreModule, anneeModule, idInter, typeHeure, nbGroupe, nbSemaine, nbHeure, commentaire) VALUES
    ('MATH101', 1, '2022-2023', 1, 'CM', 1, 12, null, 'Affectation pour le semestre 1'),
    ('MATH102', 1, '2022-2023', 1, 'TD', 1, 12, null, 'Affectation pour le semestre 1'),
    ('PHYS101', 1, '2022-2023', 2, 'TP', 1, 12, null, 'Affectation pour le semestre 1'),
    ('PHYS101', 1, '2022-2023', 1, 'HP', null, null, 6, 'Affectation pour le semestre 1');
