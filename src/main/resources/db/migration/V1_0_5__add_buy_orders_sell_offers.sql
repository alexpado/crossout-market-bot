ALTER TABLE watcher
    ADD buy_orders INT NOT NULL DEFAULT 0;

ALTER TABLE watcher
    ADD sell_offers INT NOT NULL DEFAULT 0;

INSERT INTO translation (`key`, language_id, value)
VALUES ('market.price', 'fr', '%,.2f %s (%s)'),
       ('market.price', 'en', '%.2f %s (%s)'),
       ('market.sellOffers', 'fr', 'Offre'),
       ('market.buyOrders', 'fr', 'Demande'),
       ('market.sellOffers', 'en', 'Sell Offers'),
       ('market.buyOrders', 'en', 'Buy Orders');
