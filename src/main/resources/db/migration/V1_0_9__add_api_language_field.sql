ALTER TABLE language
    ADD COLUMN `api_language` BOOL DEFAULT FALSE;

UPDATE language
SET api_language = true
WHERE id IN ('en', 'ru');
