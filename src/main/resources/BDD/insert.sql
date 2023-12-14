-- Sample data for CategorieIntervenant table
INSERT INTO CategorieIntervenant (code, nom, nbHeureMax, service, ratioTP, estCompleter) VALUES
('C1', 'Catégorie 1', 30, 1, 0.5, true),
('C2', 'Catégorie 2', 25, 2, 0.4, false),
('C3', 'Catégorie 3', 35, 3, 0.6, true),
('C4', 'Catégorie 4', 28, 2, 0.3, false),
('C5', 'Catégorie 5', 40, 1, 0.7, true);

-- Sample data for CategorieHeure table
INSERT INTO CategorieHeure (nom, nbHeureMax, service, ratioTP, estCompleter) VALUES
('HeureCat1', 30, 1, 0.5, true),
('HeureCat2', 25, 2, 0.4, false),
('HeureCat3', 35, 3, 0.6, true),
('HeureCat4', 28, 2, 0.3, false),
('HeureCat5', 40, 1, 0.7, true);

-- Sample data for Intervenant table
INSERT INTO Intervenant (nom, prenom, mail, statut, service, total) VALUES
('Smith', 'John', 'john.smith@email.com', 'Professeur', 'Informatique', 150),
('Doe', 'Jane', 'jane.doe@email.com', 'Maître de Conférences', 'Mathématiques', 120),
('Johnson', 'Bob', 'bob.johnson@email.com', 'Assistant', 'Physique', 90),
('Taylor', 'Alice', 'alice.taylor@email.com', 'Professeur', 'Chimie', 110),
('Brown', 'David', 'david.brown@email.com', 'Maître de Conférences', 'Biologie', 130);

-- Sample data for Intervenant_CategorieIntervenant table
INSERT INTO Intervenant_CategorieIntervenant (intervenant_nom, intervenant_prenom, categorieIntervenant_code) VALUES
('Smith', 'John', 'C1'),
('Doe', 'Jane', 'C2'),
('Johnson', 'Bob', 'C3'),
('Taylor', 'Alice', 'C4'),
('Brown', 'David', 'C5');

-- Sample data for Semestre table
INSERT INTO Semestre (numero, estPair, service) VALUES
(1, true, 1),
(2, false, 2),
(3, true, 3),
(4, false, 1),
(5, true, 2);

-- Sample data for Module table
INSERT INTO Module (code, nom, commentaire, nb_heure_pn_sem, nb_heure_tut, nb_heure) VALUES
('M1', 'Module 1', 'Comment 1', 30, 10, 40),
('M2', 'Module 2', 'Comment 2', 25, 15, 40),
('M3', 'Module 3', 'Comment 3', 35, 8, 43),
('M4', 'Module 4', 'Comment 4', 28, 12, 40),
('M5', 'Module 5', 'Comment 5', 40, 20, 60);

-- Sample data for Semestre_Module table
INSERT INTO Semestre_Module (semestre_id, module_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- Sample data for Module_CategorieHeure table
INSERT INTO Module_CategorieHeure (module_id, categorieHeure_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- Sample data for SAE table
INSERT INTO SAE (code, nom, commentaire, nb_heure_pn_sem, nb_heure_tut, nb_heure) VALUES
('SAE1', 'SAE 1', 'Comment SAE 1', 30, 10, 40),
('SAE2', 'SAE 2', 'Comment SAE 2', 25, 15, 40),
('SAE3', 'SAE 3', 'Comment SAE 3', 35, 8, 43),
('SAE4', 'SAE 4', 'Comment SAE 4', 28, 12, 40),
('SAE5', 'SAE 5', 'Comment SAE 5', 40, 20, 60);

-- Sample data for Stage table
INSERT INTO Stage (code, nom, commentaire, nb_heure_pn_sem, nb_heure_tut, nb_heure) VALUES
('Stage1', 'Stage 1', 'Comment Stage 1', 30, 10, 40),
('Stage2', 'Stage 2', 'Comment Stage 2', 25, 15, 40),
('Stage3', 'Stage 3', 'Comment Stage 3', 35, 8, 43),
('Stage4', 'Stage 4', 'Comment Stage 4', 28, 12, 40),
('Stage5', 'Stage 5', 'Comment Stage 5', 40, 20, 60);

-- Sample data for PPP table
INSERT INTO PPP (code, nom, commentaire, nbHeureCM, nbHeureTD, nbHeureTP, nbHeureTuto, nbHeurePonct) VALUES
('PPP1', 'PPP 1', 'Comment PPP 1', 10, 15, 8, 5, 2),
('PPP2', 'PPP 2', 'Comment PPP 2', 8, 12, 10, 7, 3),
('PPP3', 'PPP 3', 'Comment PPP 3', 12, 18, 6, 4, 1),
('PPP4', 'PPP 4', 'Comment PPP 4', 15, 20, 5, 3, 2),
('PPP5', 'PPP 5', 'Comment PPP 5', 20, 25, 4, 2, 1);

-- Sample data for Ressource table
INSERT INTO Ressource (code, nom, commentaire, nb_heure_sem, semestre, nb_heure_tl, nb_grp, nb_semaine) VALUES
('R1', 'Ressource 1', 'Comment Ressource 1', 30, 1, 15, 3, 10),
('R2', 'Ressource 2', 'Comment Ressource 2', 25, 2, 12, 2, 8),
('R3', 'Ressource 3', 'Comment Ressource 3', 35, 3, 18, 4, 12),
('R4', 'Ressource 4', 'Comment Ressource 4', 28, 4, 14, 3, 9),
('R5', 'Ressource 5', 'Comment Ressource 5', 40, 5, 20, 5, 14);