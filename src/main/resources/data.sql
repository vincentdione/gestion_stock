

INSERT INTO entreprises (nom, description, code_fiscal, site_web, email, num_tel)
VALUES
    ('kaydeco', 'Entreprise de décoration', 'KAY12345', 'www.kaydeco.com', 'contact@kaydeco.com', '123456789'),
    ('ndiolfouta', 'Entreprise de production', 'NDI98765', 'www.ndiolfouta.com', 'contact@ndiolfouta.com', '987654321');


-- Insertion de l'utilisateur ovd
INSERT INTO utilisateurs (username, password, email, nom, prenom, datedenaissance, role, id_entreprise)
VALUES (
    'ovd',
    '$2b$12$wORxF0l.TRsFaiyreOhSgOFtdSedYetyXcX.KFP8L8HyDxNiD0bga',
    'ovd@example.com',
    'OvdNom',
    'OvdPrenom',
    '1990-01-01',
    'ADMIN',
    (SELECT id FROM entreprises WHERE nom = 'kaydeco')
);

-- Insertion de l'utilisateur prince
INSERT INTO utilisateurs (username, password, email, nom, prenom, datedenaissance, role, id_entreprise)
VALUES (
    'prince',
    '$2b$12$wORxF0l.TRsFaiyreOhSgOFtdSedYetyXcX.KFP8L8HyDxNiD0bga',
    'prince@example.com',
    'PrinceNom',
    'PrincePrenom',
    '1992-02-02',
    'MANAGER',
    (SELECT id FROM entreprises WHERE nom = 'ndiolfouta')
);
