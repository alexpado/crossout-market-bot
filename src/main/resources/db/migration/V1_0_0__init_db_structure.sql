CREATE TABLE `language`
(
    `id`         VARCHAR(3)  NOT NULL PRIMARY KEY,
    `name`       VARCHAR(20) NOT NULL,
    `localized`  VARCHAR(40) NOT NULL,
    `created_at` DATETIME    NOT NULL DEFAULT NOW(),
    `updated_at` DATETIME    NOT NULL DEFAULT NOW() ON UPDATE NOW()
);

CREATE TABLE `translation`
(
    `key`         VARCHAR(255)  NOT NULL,
    `language_id` VARCHAR(3)    NOT NULL,
    `value`       VARCHAR(2048) NOT NULL,
    `created_at`  DATETIME      NOT NULL DEFAULT NOW(),
    `updated_at`  DATETIME      NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    PRIMARY KEY (`key`, `language_id`),
    CONSTRAINT `FK_TRANSLATION_LANGUAGE` FOREIGN KEY (`language_id`) REFERENCES `language` (`id`) ON DELETE CASCADE
);

CREATE TABLE `user`
(
    `id`             BIGINT       NOT NULL PRIMARY KEY,
    `username`       VARCHAR(255) NOT NULL,
    `discriminator`  VARCHAR(255) NOT NULL,
    `avatar`         VARCHAR(255),
    `watcher_paused` BOOLEAN      NOT NULL,
    `language_id`    VARCHAR(3)   NOT NULL DEFAULT 'en',
    `created_at`     DATETIME     NOT NULL DEFAULT NOW(),
    `updated_at`     DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT `FK_USER_LANGUAGE` FOREIGN KEY (`language_id`) REFERENCES `language` (`id`) ON DELETE RESTRICT
);

CREATE TABLE `guild`
(
    `id`          BIGINT       NOT NULL PRIMARY KEY,
    `name`        VARCHAR(255) NOT NULL,
    `icon`        VARCHAR(255) NULL,
    `language_id` VARCHAR(3)   NOT NULL DEFAULT 'en',
    `created_at`  DATETIME     NOT NULL DEFAULT NOW(),
    `updated_at`  DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT `FK_GUILD_LANGUAGE` FOREIGN KEY (`language_id`) REFERENCES `language` (`id`) ON DELETE RESTRICT
);

CREATE TABLE `channel`
(
    `id`          BIGINT       NOT NULL PRIMARY KEY,
    `name`        VARCHAR(255) NOT NULL,
    `guild_id`    BIGINT       NOT NULL,
    `language_id` VARCHAR(3)   NULL,
    `created_at`  DATETIME     NOT NULL DEFAULT NOW(),
    `updated_at`  DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT `FK_CHANNEL_GUILD` FOREIGN KEY (`guild_id`) REFERENCES `guild` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_CHANNEL_LANGUAGE` FOREIGN KEY (`language_id`) REFERENCES `language` (`id`) ON DELETE RESTRICT
);

CREATE TABLE `watcher`
(
    `id`              INTEGER      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`            VARCHAR(255) NOT NULL,
    `trigger`         VARCHAR(20)  NOT NULL,
    `item_id`         INTEGER      NOT NULL,
    `price_reference` DOUBLE       NULL,
    `market_buy`      DOUBLE       NULL,
    `market_sell`     DOUBLE       NULL,
    `owner_id`        BIGINT       NOT NULL,
    `regular`         BOOLEAN      NOT NULL,
    `timing`          BIGINT       NOT NULL,
    `last_execution`  DATETIME     NULL     DEFAULT NULL,
    `failure_count`   INTEGER      NOT NULL DEFAULT 0,
    `created_at`      DATETIME     NOT NULL DEFAULT NOW(),
    `updated_at`      DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT `FK_WATCHER_OWNER` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
);
