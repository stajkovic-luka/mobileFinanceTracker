-- Demo podaci za testiranje
-- Lozinka za oba naloga je: test123
INSERT INTO users (email, password_hash, name, username) VALUES
    ('ana.demo@example.com', '$2y$10$qpq004x2zjqs1LHsbfXWLebBUvd1B1Z4abrwNAoeVk4W8bhvokc4W', 'Ana Demo', 'demo_ana'),
    ('marko.demo@example.com', '$2y$10$qpq004x2zjqs1LHsbfXWLebBUvd1B1Z4abrwNAoeVk4W8bhvokc4W', 'Marko Demo', 'demo_marko');

SET @ana_id = (SELECT id FROM users WHERE username = 'demo_ana');
SET @marko_id = (SELECT id FROM users WHERE username = 'demo_marko');

INSERT INTO goals (user_id, name, target_amount, current_amount, deadline, status) VALUES
    (@ana_id, 'Novi laptop', 100000.00, 55000.00, '2026-12-31', 'ACTIVE'),
    (@ana_id, 'Letovanje', 60000.00, 60000.00, '2026-08-01', 'COMPLETED'),
    (@marko_id, 'PlayStation 5', 50000.00, 5000.00, '2026-11-30', 'ACTIVE');

SET @ana_laptop_id = (SELECT id FROM goals WHERE user_id = @ana_id AND name = 'Novi laptop');
SET @ana_vacation_id = (SELECT id FROM goals WHERE user_id = @ana_id AND name = 'Letovanje');
SET @marko_console_id = (SELECT id FROM goals WHERE user_id = @marko_id AND name = 'PlayStation 5');

INSERT INTO deposits (goal_id, amount, note, created_at, updated_at) VALUES
    (@ana_laptop_id, 15000.00, 'Prva uplata', '2026-06-12 10:00:00', '2026-06-12 10:00:00'),
    (@ana_laptop_id, 10000.00, 'Dodatna usteda', '2026-06-20 15:30:00', '2026-06-20 15:30:00'),
    (@ana_laptop_id, 30000.00, 'Bonus', '2026-07-01 09:15:00', '2026-07-01 09:15:00'),
    (@ana_vacation_id, 30000.00, 'Prva rata', '2026-05-10 12:00:00', '2026-05-10 12:00:00'),
    (@ana_vacation_id, 30000.00, 'Druga rata', '2026-06-10 12:00:00', '2026-06-10 12:00:00'),
    (@marko_console_id, 5000.00, 'Pocetna uplata', '2026-07-05 18:00:00', '2026-07-05 18:00:00');
