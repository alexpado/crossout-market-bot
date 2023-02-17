UPDATE translation
SET `value` = '%,.2f %s (%s)'
WHERE `key` = 'market.price'
  AND language_id = 'en';

UPDATE translation
SET `value` = '%.2f %s (%s)'
WHERE `key` = 'market.price'
  AND language_id = 'fr';

INSERT INTO translation (`key`, language_id, value)
VALUES ('market.simplePrice', 'fr', '%.2f %s'),
       ('market.simplePrice', 'en', '%,.2f %s');
