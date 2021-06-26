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
    `key`        VARCHAR(255)  NOT NULL,
    `language`   VARCHAR(3)    NOT NULL,
    `value`      VARCHAR(2048) NOT NULL,
    `created_at` DATETIME      NOT NULL DEFAULT NOW(),
    `updated_at` DATETIME      NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    PRIMARY KEY (`key`, `language`),
    CONSTRAINT `FK_TRANSLATION_LANGUAGE` FOREIGN KEY (`language`) REFERENCES `language` (`id`) ON DELETE CASCADE
);


CREATE TABLE `user`
(
    `id`             BIGINT       NOT NULL PRIMARY KEY,
    `username`       VARCHAR(255) NOT NULL,
    `discriminator`  VARCHAR(255) NOT NULL,
    `avatar`         VARCHAR(255),
    `watcher_paused` BOOLEAN      NOT NULL,
    `language`       VARCHAR(3)   NOT NULL,
    `created_at`     DATETIME     NOT NULL DEFAULT NOW(),
    `updated_at`     DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT `FK_USER_LANGUAGE` FOREIGN KEY (`language`) REFERENCES `language` (`id`) ON DELETE RESTRICT
);

CREATE TABLE `role`
(
    `id`         BIGINT       NOT NULL PRIMARY KEY,
    `color`      VARCHAR(6)   NULL,
    `name`       VARCHAR(255) NOT NULL,
    `display`    BOOLEAN      NOT NULL DEFAULT FALSE,
    `created_at` DATETIME     NOT NULL DEFAULT NOW(),
    `updated_at` DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW()
);

CREATE TABLE `user_roles`
(
    `user_id`  BIGINT NOT NULL,
    `roles_id` BIGINT NOT NULL,
    CONSTRAINT `UNIQUE_USER_ROLE` UNIQUE (`user_id`, `roles_id`),
    CONSTRAINT `FK_ASSOC_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_ASSOC_ROLE_ID` FOREIGN KEY (`roles_id`) REFERENCES `role` (`id`) ON DELETE CASCADE
);


CREATE TABLE `guild`
(
    `id`         BIGINT       NOT NULL PRIMARY KEY,
    `name`       VARCHAR(255) NOT NULL,
    `icon`       VARCHAR(255) NULL,
    `owner_id`   BIGINT       NOT NULL,
    `language`   VARCHAR(3)   NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT NOW(),
    `updated_at` DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT `FK_GUILD_OWNER` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_GUILD_LANGUAGE` FOREIGN KEY (`language`) REFERENCES `language` (`id`) ON DELETE RESTRICT
);

CREATE TABLE `channel`
(
    `id`         BIGINT       NOT NULL PRIMARY KEY,
    `name`       VARCHAR(255) NOT NULL,
    `guild_id`   BIGINT       NOT NULL,
    `language`   VARCHAR(3)   NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT NOW(),
    `updated_at` DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT `FK_CHANNEL_GUILD` FOREIGN KEY (`guild_id`) REFERENCES `guild` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_CHANNEL_LANGUAGE` FOREIGN KEY (`language`) REFERENCES `language` (`id`) ON DELETE RESTRICT
);

CREATE TABLE `health`
(
    `run_at`        DATETIME NOT NULL DEFAULT NOW() PRIMARY KEY,
    `response_time` INTEGER  NOT NULL
);

CREATE TABLE `watcher`
(
    `id`             INTEGER      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`           VARCHAR(50)  NOT NULL,
    `price`          DOUBLE       NULL,
    `type`           VARCHAR(20)  NOT NULL,
    `item_id`        INTEGER      NOT NULL,
    `item_name`      VARCHAR(255) NOT NULL,
    `sell_price`     DOUBLE       NULL,
    `buy_price`      DOUBLE       NULL,
    `owner_id`       BIGINT       NOT NULL,
    `regular`        BOOLEAN      NOT NULL,
    `timing`         BIGINT       NOT NULL,
    `last_execution` DATETIME     NULL     DEFAULT NULL,
    `created_at`     DATETIME     NOT NULL DEFAULT NOW(),
    `updated_at`     DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT `FK_WATCHER_OWNER` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
);
