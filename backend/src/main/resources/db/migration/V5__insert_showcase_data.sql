-- Podaci za prikaz aplikacije kroz sest meseci koriscenja
-- Prijava: username milos, lozinka milos123
INSERT INTO users (email, password_hash, name, username, created_at, updated_at) VALUES
    ('milos.bikovic@gmail.com', '$2y$10$YyRDPXNKNFEp0fkWxSH1TeioQlXryxKoreQnhpbCMiHRiDBDOWbre', 'Milos Bikovic', 'milos', '2026-01-10 09:00:00', '2026-08-13 18:00:00');

SET @milos_id = (SELECT id FROM users WHERE username = 'milos');

INSERT INTO goals (user_id, name, target_amount, current_amount, deadline, status, created_at, updated_at) VALUES
    (@milos_id, 'Fond za nepredvidjene troskove', 150000.00, 69500.00, NULL, 'ACTIVE', '2026-02-03 10:00:00', '2026-08-04 08:30:00'),
    (@milos_id, 'Letovanje 2026', 90000.00, 90000.00, '2026-08-20', 'COMPLETED', '2026-01-14 11:00:00', '2026-07-07 14:00:00'),
    (@milos_id, 'Novi telefon', 100000.00, 44500.00, '2026-11-15', 'ACTIVE', '2026-03-21 16:00:00', '2026-08-12 17:30:00'),
    (@milos_id, 'Kurs programiranja', 60000.00, 29500.00, '2026-10-01', 'ACTIVE', '2026-02-20 12:00:00', '2026-08-07 09:30:00'),
    (@milos_id, 'Bicikl', 75000.00, 14500.00, '2027-03-31', 'ACTIVE', '2026-05-30 15:00:00', '2026-08-13 18:00:00');

SET @fond_id = (SELECT id FROM goals WHERE user_id = @milos_id AND name = 'Fond za nepredvidjene troskove');
SET @letovanje_id = (SELECT id FROM goals WHERE user_id = @milos_id AND name = 'Letovanje 2026');
SET @telefon_id = (SELECT id FROM goals WHERE user_id = @milos_id AND name = 'Novi telefon');
SET @kurs_id = (SELECT id FROM goals WHERE user_id = @milos_id AND name = 'Kurs programiranja');
SET @bicikl_id = (SELECT id FROM goals WHERE user_id = @milos_id AND name = 'Bicikl');

INSERT INTO deposits (goal_id, amount, note, created_at, updated_at) VALUES
    (@fond_id, 10000.00, 'Pocetna rezerva', '2026-03-03 08:30:00', '2026-03-03 08:30:00'),
    (@fond_id, 6000.00, 'Usteda od troskova', '2026-03-31 18:00:00', '2026-03-31 18:00:00'),
    (@fond_id, 8000.00, 'Dodatna uplata', '2026-04-20 12:15:00', '2026-04-20 12:15:00'),
    (@fond_id, 10000.00, 'Mesecna stednja', '2026-05-17 09:00:00', '2026-05-17 09:00:00'),
    (@fond_id, 8500.00, 'Usteda za maj', '2026-06-05 16:30:00', '2026-06-05 16:30:00'),
    (@fond_id, 12000.00, 'Bonus', '2026-07-10 10:00:00', '2026-07-10 10:00:00'),
    (@fond_id, 15000.00, 'Usteda za avgust', '2026-08-04 08:30:00', '2026-08-04 08:30:00'),

    (@letovanje_id, 20000.00, 'Prva rata', '2026-03-15 12:00:00', '2026-03-15 12:00:00'),
    (@letovanje_id, 15000.00, 'Uplata za april', '2026-04-12 14:00:00', '2026-04-12 14:00:00'),
    (@letovanje_id, 20000.00, 'Uplata za maj', '2026-05-07 11:00:00', '2026-05-07 11:00:00'),
    (@letovanje_id, 25000.00, 'Uplata za jun', '2026-06-15 13:30:00', '2026-06-15 13:30:00'),
    (@letovanje_id, 10000.00, 'Zavrsna uplata', '2026-07-07 14:00:00', '2026-07-07 14:00:00'),

    (@telefon_id, 7000.00, 'Pocetak stednje', '2026-04-08 17:00:00', '2026-04-08 17:00:00'),
    (@telefon_id, 6500.00, 'Usteda za maj', '2026-05-26 18:30:00', '2026-05-26 18:30:00'),
    (@telefon_id, 8000.00, 'Dodatna uplata', '2026-06-24 10:30:00', '2026-06-24 10:30:00'),
    (@telefon_id, 10000.00, 'Bonus', '2026-07-30 15:00:00', '2026-07-30 15:00:00'),
    (@telefon_id, 13000.00, 'Usteda za avgust', '2026-08-12 17:30:00', '2026-08-12 17:30:00'),

    (@kurs_id, 5000.00, 'Prva uplata', '2026-03-20 09:00:00', '2026-03-20 09:00:00'),
    (@kurs_id, 7500.00, 'Usteda za april', '2026-04-30 17:00:00', '2026-04-30 17:00:00'),
    (@kurs_id, 5000.00, 'Uplata za jun', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
    (@kurs_id, 6000.00, 'Uplata za jul', '2026-07-18 12:00:00', '2026-07-18 12:00:00'),
    (@kurs_id, 6000.00, 'Uplata za avgust', '2026-08-07 09:30:00', '2026-08-07 09:30:00'),

    (@bicikl_id, 5000.00, 'Pocetna uplata', '2026-06-18 11:00:00', '2026-06-18 11:00:00'),
    (@bicikl_id, 4500.00, 'Uplata za jul', '2026-07-25 16:00:00', '2026-07-25 16:00:00'),
    (@bicikl_id, 5000.00, 'Uplata za avgust', '2026-08-13 18:00:00', '2026-08-13 18:00:00');
