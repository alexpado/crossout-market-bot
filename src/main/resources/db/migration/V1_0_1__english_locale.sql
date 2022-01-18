INSERT INTO `language` (`id`, `name`, `localized`) VALUE ('en', 'English', 'English');

INSERT INTO `translation` (`language_id`, `key`, `value`)
VALUES ('en', 'embed.header.full', '**%1$s**\n[View on CrossoutDB](%2$s) • [Report an issue](%3$s)'),
       ('en', 'embed.header.simple', '**%1$s**\n[Report an issue](%2$s)'),
       ('en', 'embed.invite', 'Click here to invite the bot'),
       ('en', 'embed.footer.xodb', 'Powered by CrossoutDB'),
       ('en', 'embed.footer.developer', 'Developed by Akio Nakao#0001'),
       ('en', 'embed.footer.pagination', 'Page %1$s/%2$s'),
       ('en', 'item.removed', '> *This item has been removed*'),
       ('en', 'pack.price', 'Pack prices'),
       ('en', 'market.currency', 'Coins'),
       ('en', 'market.sell', 'Market Sell'),
       ('en', 'market.buy', 'Market Buy'),
       ('en', 'market.craft.sell', 'Market Sell Craft'),
       ('en', 'market.craft.buy', 'Market Buy Craft'),
       ('en', 'search.empty', 'There is no results for your search'),
       ('en', 'search.results', 'Search Results'),
       ('en', 'watcher.params.frequency', 'Please provide a frequency of at least 5m'),
       ('en', 'watcher.params.price', 'Please provide a valid price'),
       ('en', 'watcher.params.priceTrigger', 'Please provide a price using the selected trigger'),
       ('en', 'watcher.item.tooMany', 'Unable to create watcher: Too many items'),
       ('en', 'watcher.item.none', 'Unable to create watcher: No item found'),
       ('en', 'watcher.empty', 'You don\'t have any watchers (yet)'),
       ('en', 'watcher.notFound', 'Unable to find the watcher'),
       ('en', 'watcher.ownership', 'You\'re not the owner of this watcher'),
       ('en', 'watcher.created', 'The watcher has been created with the ID %1$s'),
       ('en', 'watcher.deleted', 'The watcher has been removed'),
       ('en', 'watcher.updated', 'The watcher has been updated'),
       ('en', 'watcher.status.paused', 'Your watchers has been paused'),
       ('en', 'watcher.status.resumed', 'Your watchers has been resumed'),
       ('en', 'watcher.already.paused', 'Your watchers are already paused'),
       ('en', 'watcher.already.resumed', 'Your watchers are already running'),
       ('en', 'trigger.sell.under', '%1$s when market sell price is under %2$s every %3$s'),
       ('en', 'trigger.sell.over', '%1$s when market sell price is over %2$s every %3$s'),
       ('en', 'trigger.buy.under', '%1$s when market buy price is under %2$s every %3$s'),
       ('en', 'trigger.buy.over', '%1$s when market buy price is over %2$s every %3$s'),
       ('en', 'trigger.everytime', '%1$s every %2$s');
