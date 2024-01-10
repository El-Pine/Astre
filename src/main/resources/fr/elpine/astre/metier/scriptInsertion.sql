-- Insertion des données dans la table Annee
INSERT INTO Annee (nom, debut, fin) VALUES
    ('2022-2023'        , '2022-09-01' , '2023-08-31'),
    ('2023-2024'        , '2023-09-01' , '2024-08-31'),
    ('2024-2025'        , '2024-09-01' , '2025-08-31'),
    ('2022-2023 bis'    , '2022-09-01' , '2023-08-31'),
    ('2026-2027'        , '2023-09-01' , '2024-08-31'),
    ('2024-2025 Test'   , '2024-09-01' , '2025-08-31');

-- Insertion des données dans la table Semestre (pour 6 semestres par année)
INSERT INTO Semestre (numero, annee, nbGrpTD, nbGrpTP, nbEtd, nbSemaine) VALUES
     (1, '2022-2023'     , 4    , 2     , 28    , 19),
     (2, '2022-2023'     , 1    , 6     , 12    , 14),
     (3, '2022-2023'     , 9    , 2     , 18    , 30),
     (4, '2022-2023'     , 1    , 1     , 25    , 17),
     (5, '2022-2023'     , 3    , 8     , 4     , 24),
     (6, '2022-2023'     , 2    , 10    , 29    , 4 ),
     (1, '2023-2024'     , 1    , 1     , 27    , 10),
     (2, '2023-2024'     , 2    , 3     , 21    , 3 ),
     (3, '2023-2024'     , 4    , 2     , 6     , 19),
     (4, '2023-2024'     , 2    , 4     , 8     , 16),
     (5, '2023-2024'     , 3    , 1     , 20    , 14),
     (6, '2023-2024'     , 4    , 2     , 14    , 18),
     (1, '2024-2025'     , 1    , 1     , 12    , 25),
     (2, '2024-2025'     , 2    , 3     , 16    , 8 ),
     (3, '2024-2025'     , 4    , 2     , 5     , 4 ),
     (4, '2024-2025'     , 2    , 4     , 3     , 30),
     (5, '2024-2025'     , 3    , 1     , 9     , 26),
     (6, '2024-2025'     , 4    , 2     , 28    , 19),
     (1, '2022-2023 bis' , 1    , 1     , 23    , 13),
     (2, '2022-2023 bis' , 2    , 3     , 28    , 14),
     (3, '2022-2023 bis' , 4    , 2     , 17    , 8 ),
     (4, '2022-2023 bis' , 2    , 4     , 11    , 24),
     (5, '2022-2023 bis' , 3    , 1     , 29    , 4 ),
     (6, '2022-2023 bis' , 4    , 2     , 15    , 9 ),
     (1, '2026-2027'     , 1    , 1     , 8     , 18),
     (2, '2026-2027'     , 2    , 3     , 3     , 7 ),
     (3, '2026-2027'     , 4    , 2     , 25    , 27),
     (4, '2026-2027'     , 2    , 4     , 6     , 25),
     (5, '2026-2027'     , 3    , 1     , 19    , 27),
     (6, '2026-2027'     , 4    , 2     , 15    , 24),
     (1, '2024-2025 Test', 1    , 1     , 23    , 14),
     (2, '2024-2025 Test', 2    , 3     , 27    , 3 ),
     (3, '2024-2025 Test', 4    , 2     , 30    , 22),
     (4, '2024-2025 Test', 2    , 4     , 29    , 12),
     (5, '2024-2025 Test', 3    , 1     , 20    , 25),
     (6, '2024-2025 Test', 4    , 2     , 18    , 3 );

-- Insertion des données dans la table Module
INSERT INTO Module (code, numeroSemestre, annee, nom, abreviation, couleur, typeModule, validation) VALUES
    ('R1.01', 1, '2022-2023'    , 'Mathématiques'   , 'MATH'    , 'rgb(27,18,4)'    , 'ressource'  , true ),
    ('R1.02', 1, '2022-2023'    , 'Analyse'         , 'ANA'     , 'rgb(11,35,21)'   , 'ressource'  , true ),
    ('R1.03', 1, '2022-2023'    , 'Qualité Dev'     , 'QDEV'    , 'rgb(9,7,15)'     , 'ressource'  , true ),
    ('R2.01', 2, '2022-2023'    , 'Algèbre'         , 'ALG'     , 'rgb(30,15,25)'   , 'ressource'  , false),
    ('R2.02', 2, '2022-2023'    , 'Cryptographie'   , 'CRYP'    , 'rgb(18,28,9)'    , 'ressource'  , false),
    ('S3.01', 3, '2023-2024'    , 'Sae dévellopement d application', 'S3.01', 'rgb(5,22,33)', 'Sae', false),
    ('PPP'  , 3, '2022-2023'    , 'PPP'             , 'PPP'     , 'rgb(31,22,3)'    , 'Ppp'        , true ),
    ('R4.01', 4, '2022-2023'    , 'Analyse'         , 'ANA'     , 'rgb(11,35,21)'   , 'ressource'  , true ),
    ('R4.02', 4, '2022-2023'    , 'Qualité Dev'     , 'QDEV'    , 'rgb(9,7,15)'     , 'ressource'  , true ),
    ('PPP'  , 4, '2022-2023'    , 'PPP'             , 'PPP'     , 'rgb(31,22,3)'    , 'Ppp'        , false),
    ('R5.01', 5, '2022-2023'    , 'Mathématiques'   , 'MATH'    , 'rgb(27,18,4)'    , 'ressource'  , false),
    ('R6.02', 6, '2023-2024'    , 'Qualité Dev'     , 'QDEV'    , 'rgb(9,7,15)'     , 'ressource'  , false),
    ('S6.01', 6, '2022-2023'    , 'Sae dévellopement d application', 'S3.01', 'rgb(5,22,33)', 'Sae', true ),
    ('PPP'  , 6, '2022-2023'    , 'PPP'             , 'PPP'     , 'rgb(23,31,14)'   , 'Ppp'        , true ),
    ('R1.01', 1, '2022-2023 bis', 'Mathématiques'   , 'MATH'    , 'rgb(27,18,4)'    , 'ressource'  , true ),
    ('R2.01', 2, '2022-2023 bis', 'Algèbre'         , 'ALG'     , 'rgb(19,8,28)'    , 'ressource'  , false),
    ('PPP'  , 3, '2022-2023 bis', 'PPP'             , 'PPP'     , 'rgb(31,22,3)'    , 'Ppp'        , true ),
    ('R4.01', 4, '2022-2023 bis', 'Analyse'         , 'ANA'     , 'rgb(11,35,21)'   , 'ressource'  , false);


-- Insertion des données dans la table CategorieHeure
INSERT INTO CategorieHeure (nom, eqtd, ressource, sae, ppp, stage, hebdo, typeGroupe) VALUES
    ('CM'   , '3/2' , true , false, false, false, true , 'cm'),
    ('TD'   , '1'   , true , false, false, false, true , 'td'),
    ('TP'   , '1'   , true , false, false, false, true , 'tp'),
    ('H tut', '1'   , false, true , true , true , false, 'cm'),
    ('REH'  , '1'   , false, false, true , true , false, 'cm'),
    ('H Saé', '1'   , false, true , false, false, false, 'cm'),
    ('HP'   , '1'   , true , false, false, false, false, 'cm');

-- Insertion des données dans la table CategorieIntervenant
INSERT INTO CategorieIntervenant (code, nom, nbHeureMaxDefaut, nbHeureServiceDefaut, ratioTPDefaut) VALUES
    ('CAT1', 'Enseignant' , '192', '178', '1/2'),
    ('CAT2', 'Chercheur'  , '192', '178', '3/5'),
    ('CAT3', 'Vacataire'  , '192', '178', '3/5'),
    ('CAT4', 'Intérimaire', '192', '178', '3/4');

-- Insertion des données dans la table Intervenant
INSERT INTO Intervenant (nom, prenom, mail, codeCategorie, heureService, heureMax, ratioTP) VALUES
    ('Lumière'  , 'Lucie'       , 'lucie.lumiere@example.com'       , 'CAT1', '25', '30', '3/5'),
    ('Bouton'   , 'Alain'       , 'alain.bouton@example.com'        , 'CAT3', '20', '25', '2/5'),
    ('Riz'      , 'Anna'        , 'anna.riz@example.com'            , 'CAT2', '28', '35', '4/5'),
    ('Soleil'   , 'Ray'         , 'ray.soleil@example.com'          , 'CAT4', '18', '22', '5/5'),
    ('Tige'     , 'Flo'         , 'flo.tige@example.com'            , 'CAT1', '30', '40', '3/5'),
    ('Chaise'   , 'Gérard'      , 'gerard.chaise@example.com'       , 'CAT1', '22', '28', '4/5'),
    ('Miel'     , 'Léa'         , 'lea.miel@example.com'            , 'CAT2', '27', '33', '3/5'),
    ('Fleur'    , 'Paul'        , 'paul.fleur@example.com'          , 'CAT4', '19', '24', '2/5'),
    ('Oreiller' , 'Claire'      , 'claire.oreiller@example.com'     , 'CAT3', '23', '31', '5/5'),
    ('Vent'     , 'Marc'        , 'marc.vent@example.com'           , 'CAT2', '26', '32', '4/5'),
    ('Vague'    , 'Sylvie'      , 'sylvie.vague@example.com'        , 'CAT1', '21', '29', '3/5'),
    ('Montagne' , 'Antoine'     , 'antoine.montagne@example.com'    , 'CAT3', '24', '27', '2/5'),
    ('Feuille'  , 'Caroline'    , 'caroline.feuille@example.com'    , 'CAT4', '17', '23', '5/5'),
    ('Nuage'    , 'Philippe'    , 'philippe.nuage@example.com'      , 'CAT1', '29', '37', '4/5'),
    ('Plume'    , 'Sophie'      , 'sophie.plume@example.com'        , 'CAT2', '26', '34', '3/5'),
    ('Ciel'     , 'Thomas'      , 'thomas.ciel@example.com'         , 'CAT3', '22', '28', '2/5'),
    ('Eau'      , 'Camille'     , 'camille.eau@example.com'         , 'CAT4', '20', '25', '5/5'),
    ('Pierre'   , 'Isabelle'    , 'isabelle.pierre@example.com'     , 'CAT1', '27', '33', '4/5'),
    ('Bois'     , 'Jérôme'      , 'jerome.bois@example.com'         , 'CAT2', '24', '31', '3/5'),
    ('Mouche'   , 'Marie'       , 'marie.mouche@example.com'        , 'CAT3', '18', '24', '2/5'),
    ('Glace'    , 'François'    , 'francois.glace@example.com'      , 'CAT4', '23', '29', '5/5'),
    ('Terre'    , 'Hélène'      , 'helene.terre@example.com'        , 'CAT1', '28', '36', '4/5'),
    ('Rapace'   , 'Jeanne'      , 'jeanne.arcenciel@example.com'    , 'CAT2', '25', '32', '3/5'),
    ('Rocher'   , 'Vincent'     , 'vincent.rocher@example.com'      , 'CAT3', '19', '26', '2/5'),
    ('Étoile'   , 'Julie'       , 'julie.etoile@example.com'        , 'CAT4', '24', '30', '5/5'),
    ('Bulles'   , 'David'       , 'david.bulles@example.com'        , 'CAT1', '26', '34', '4/5'),
    ('Bonbon'   , 'Émilie'      , 'emilie.bonbon@example.com'       , 'CAT2', '23', '29', '3/5'),
    ('Papillon' , 'Guillaume'   , 'guillaume.papillon@example.com'  , 'CAT3', '20', '27', '2/5'),
    ('Brise'    , 'Charlotte'   , 'charlotte.brise@example.com'     , 'CAT4', '22', '28', '5/5');


-- Insertion des données dans la table Attribution
INSERT INTO Attribution (codeModule, numeroSemestreModule, anneeModule, nomCategorieHeure, nbHeure, nbSemaine, nbHeurePN) VALUES
    ('R1.01', 1, '2022-2023', 'CM'      , '30', '12', '5'),
    ('R1.02', 1, '2022-2023', 'TD'      , '15', '12', '5'),
    ('R1.03', 1, '2022-2023', 'TP'      , '20', '12', '5'),
    ('R2.01', 2, '2022-2023', 'CM'      , '25', '10', '4'),
    ('R2.02', 2, '2022-2023', 'TD'      , '18', '10', '4'),
    ('R2.01', 2, '2022-2023', 'TP'      , '22', '10', '4'),
    ('S3.01', 3, '2023-2024', 'H tut'   , '28', '0' , '6'),
    ('S3.01', 3, '2023-2024', 'H Saé'   , '12', '0' , '6'),
    ('PPP'  , 3, '2022-2023', 'H tut'   , '20', '0' , '3'),
    ('PPP'  , 3, '2022-2023', 'H Saé'   , '10', '0' , '3'),
    ('R4.01', 4, '2022-2023', 'CM'      , '24', '10', '4'),
    ('R4.02', 4, '2022-2023', 'TD'      , '16', '10', '4'),
    ('R4.01', 4, '2022-2023', 'TP'      , '20', '10', '4'),
    ('R5.01', 5, '2022-2023', 'CM'      , '27', '11', '5'),
    ('R5.01', 5, '2022-2023', 'TD'      , '14', '11', '5'),
    ('R5.01', 5, '2022-2023', 'TP'      , '18', '11', '5'),
    ('R6.02', 6, '2023-2024', 'CM'      , '29', '12', '6'),
    ('R6.02', 6, '2023-2024', 'TD'      , '15', '12', '6'),
    ('R6.02', 6, '2023-2024', 'TP'      , '20', '12', '6');

-- Insertion des données dans la table Affectation
INSERT INTO Affectation (codeModule, numeroSemestreModule, anneeModule, idInter, typeHeure, nbGroupe, nbSemaine, nbHeure, commentaire) VALUES
    ('R1.01', 1, '2022-2023', 3 , 'CM'      , 1     , 15    , NULL  , 'Affectation pour le semestre 1'),
    ('R1.02', 1, '2022-2023', 4 , 'TD'      , 1     , 13    , NULL  , 'Affectation pour le semestre 1'),
    ('R1.03', 1, '2022-2023', 5 , 'TP'      , 1     , 14    , NULL  , 'Affectation pour le semestre 1'),
    ('R2.01', 2, '2022-2023', 6 , 'CM'      , 1     , 15    , NULL  , 'Affectation pour le semestre 2'),
    ('R2.02', 2, '2022-2023', 7 , 'TD'      , 1     , 13    , NULL  , 'Affectation pour le semestre 2'),
    ('S3.01', 3, '2023-2024', 8 , 'H tut'   , 1     , 10    , NULL  , 'Affectation pour le semestre 3'),
    ('S3.01', 3, '2023-2024', 9 , 'H Saé'   , NULL  , NULL  , 5     , 'Affectation pour le semestre 3'),
    ('PPP'  , 3, '2022-2023', 10, 'H tut'   , 1     , 10    , NULL  , 'Affectation pour le semestre 3'),
    ('PPP'  , 3, '2022-2023', 11, 'H Saé'   , NULL  , NULL  , 4     , 'Affectation pour le semestre 3'),
    ('R4.01', 4, '2022-2023', 12, 'CM'      , 1     , 16    , NULL  , 'Affectation pour le semestre 4'),
    ('R4.02', 4, '2022-2023', 13, 'TD'      , 1     , 12    , NULL  , 'Affectation pour le semestre 4'),
    ('R5.01', 5, '2022-2023', 14, 'CM'      , 1     , 18    , NULL  , 'Affectation pour le semestre 5'),
    ('R5.01', 5, '2022-2023', 15, 'TD'      , 1     , 14    , NULL  , 'Affectation pour le semestre 5'),
    ('R6.02', 6, '2023-2024', 16, 'CM'      , 1     , 20    , NULL  , 'Affectation pour le semestre 6'),
    ('R6.02', 6, '2023-2024', 17, 'TD'      , 1     , 16    , NULL  , 'Affectation pour le semestre 6'),
    ('R6.02', 6, '2023-2024', 18, 'TP'      , 1     , 18    , NULL  , 'Affectation pour le semestre 6'),
    ('R4.01', 4, '2022-2023', 22, 'CM'      , 1     , 14    , NULL  , 'Affectation pour le semestre 4');
