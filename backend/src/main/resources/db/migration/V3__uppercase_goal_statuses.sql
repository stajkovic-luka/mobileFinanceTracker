-- Enum u kodu koristi velika slova, pa mora da se uskladi u bazi, kako ne bi doslo do problema kasnije
ALTER TABLE goals
    MODIFY COLUMN status VARCHAR(10) NOT NULL DEFAULT 'active';

UPDATE goals
SET status = UPPER(status);

ALTER TABLE goals
    MODIFY COLUMN status ENUM('ACTIVE', 'COMPLETED', 'ARCHIVED') NOT NULL DEFAULT 'ACTIVE';
