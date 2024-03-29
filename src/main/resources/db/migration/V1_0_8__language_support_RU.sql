INSERT INTO `language` (`id`, `name`, `localized`) VALUE ('ru', 'Russian', 'Русский');

INSERT INTO xomarket.translation (`key`, language_id, value)
VALUES ('embed.footer.developer', 'ru', 'Разработано Akio Nakao#0001'),
       ('embed.footer.pagination', 'ru', 'Страница %1$s/%2$s'),
       ('embed.footer.xodb', 'ru', 'При поддержке сайта CrossoutDB'),
       ('embed.header.full', 'ru', '**%1$s**\n[Смотреть на CrossoutDB](%2$s) • [Сообщить о проблеме](%3$s)'),
       ('embed.header.simple', 'ru', '**%1$s**\n[Сообщить о проблеме](%2$s)'),
       ('embed.invite', 'ru', 'Нажмите здесь, чтобы пригласить бота'),
       ('general.forbidden', 'ru', 'Вам не разрешено это действие.'),
       ('general.xodb.error', 'ru',
        'При получении данных с CrossoutDB произошла ошибка. Пожалуйста, повторите попытку позднее.'),
       ('general.xodb.offline', 'ru', 'В данный момент CrossoutDB недоступен, пожалуйста, повторите попытку позднее.'),
       ('item.removed', 'ru', '> *Данный предмет был убран*'),
       ('language.unsupported', 'ru', 'Данный язык не поддерживается (пока ещё?)'),
       ('language.updated.channel', 'ru', 'Язык канала был обновлён.'),
       ('language.updated.guild', 'ru', 'Язык сервера был обновлён.'),
       ('language.updated.user', 'ru', 'Ваш язык был обновлён.'),
       ('market.buy', 'ru', 'Покупка на рынке'),
       ('market.buyOrders', 'ru', 'Запросы на покупку'),
       ('market.craft.buy', 'ru', 'Рынок Покупка Производство'),
       ('market.craft.sell', 'ru', 'Рынок Продажа Произовдство'),
       ('market.currency', 'ru', 'Монет'),
       ('market.price', 'ru', '%,.2f %s (%s)'),
       ('market.sell', 'ru', 'Продажа на рынке'),
       ('market.sellOffers', 'ru', 'Запросы на продажу'),
       ('market.simplePrice', 'ru', '%,.2f %s'),
       ('pack.price', 'ru', 'Цены на наборы'),
       ('search.empty', 'ru', 'По Вашему поисковому запросу результаты не найдены'),
       ('search.results', 'ru', 'Результаты поиска'),
       ('trigger.buy.over', 'ru', '%1$s когда цена покупки на рынке выше %2$s каждые %3$s'),
       ('trigger.buy.under', 'ru', '%1$s когда цена покупки на рынке ниже %2$s каждые %3$s'),
       ('trigger.everytime', 'ru', '%1$s каждые %2$s'),
       ('trigger.sell.over', 'ru', '%1$s когда цена продажи на рынке выше %2$s каждые %3$s'),
       ('trigger.sell.under', 'ru', '%1$s когда цена продажи на рынке ниже %2$s каждые %3$s'),
       ('watcher.already.paused', 'ru', 'Ваши предметы в списке отслеживания уже поставлены на паузу'),
       ('watcher.already.resumed', 'ru', 'Ваши предметы в списке отслеживания в действии'),
       ('watcher.created', 'ru', 'Предмет для списка отслеживания создан с ID %1$s'),
       ('watcher.deleted', 'ru', 'Предмет из списка отслеживания убран'),
       ('watcher.empty', 'ru', 'У Вас нет предметов в списке отслеживание (пока ещё)'),
       ('watcher.item.none', 'ru', 'Невозможно создать предмет для списка отслеживания: Предмет не найден'),
       ('watcher.item.tooMany', 'ru', 'Невозможно создать предмет для списка отслеживания: Слишком много предметов'),
       ('watcher.notFound', 'ru', 'Не получилось найти предмет из списка отслеживания'),
       ('watcher.ownership', 'ru', 'Вы не являетесь создателем данного предмета из списка отслеживания'),
       ('watcher.params.frequency', 'ru', 'Пожалуйста, поставьте частоту обновления не менее 5 минут'),
       ('watcher.params.name', 'ru', 'Название предмета для списка отслеживания не должно превышать 255 символов'),
       ('watcher.params.price', 'ru', 'Пожалуйста, предоставьте действующую цену'),
       ('watcher.params.priceTrigger', 'ru', 'Пожалуйста, предоставьте цену, используя выбранный триггер'),
       ('watcher.status.paused', 'ru', 'Ваши предметы из списка отслеживания поставлены на паузу'),
       ('watcher.status.resumed', 'ru', 'Ваши предметы из списка отслеживания снова в работе'),
       ('watcher.updated', 'ru', 'Предмет из списка отслеживания обновлён');

