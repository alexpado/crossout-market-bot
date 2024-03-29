INSERT INTO `language` (`id`, `name`, `localized`) VALUE ('fr', 'French', 'Français');

INSERT INTO translation (`language_id`, `key`, `value`)
VALUES ('fr', 'embed.header.full', '**%1$s**\n[Voir sur CrossoutDB](%2$s) • [Signaler un problème (Anglais)](%3$s)'),
       ('fr', 'embed.header.simple', '**%1$s**\n[Signaler un problème (Anglais)](%2$s)'),
       ('fr', 'embed.invite', 'Cliquez ici pour inviter le bot'),
       ('fr', 'embed.footer.xodb', 'Propulsé par CrossoutDB'),
       ('fr', 'embed.footer.developer', 'Développé par Akio Nakao#0001'),
       ('fr', 'embed.footer.pagination', 'Page %1$s/%2$s'),
       ('fr', 'item.removed', '> *Cet item a été supprimé*'),
       ('fr', 'pack.price', 'Prix du pack'),
       ('fr', 'market.currency', 'Coins'),
       ('fr', 'market.sell', 'Prix de vente'),
       ('fr', 'market.buy', 'Prix d\'achat'),
       ('fr', 'market.craft.sell', 'Prix de vente des matériaux'),
       ('fr', 'market.craft.buy', 'Prix d\'achat des matériaux'),
       ('fr', 'search.empty', 'Aucun résultat pour votre recherche'),
       ('fr', 'search.results', 'Resultat de la recherche'),
       ('fr', 'watcher.params.frequency', 'Merci de préciser une fréquence d\'au moins 5m'),
       ('fr', 'watcher.params.price', 'Merci de préciser un prix valide'),
       ('fr', 'watcher.params.priceTrigger', 'Merci de préciser un prix avec l\'option "trigger" choisie'),
       ('fr', 'watcher.item.tooMany', 'Impossible de créer le watcher: Trop d\'item.'),
       ('fr', 'watcher.item.none', 'Impossible de créer le watcher: Aucun item trouvé'),
       ('fr', 'watcher.empty', 'Vous n\'avez pas (encore) de watcher'),
       ('fr', 'watcher.notFound', 'Impossible de trouver le watcher'),
       ('fr', 'watcher.ownership', 'Vous n\'êtes pas le propriétaire de ce watcher'),
       ('fr', 'watcher.created', 'Le watcher a été créé avec l\'ID %1$s'),
       ('fr', 'watcher.deleted', 'Le watcher a été supprimé'),
       ('fr', 'watcher.updated', 'Le watcher a été mis à jour'),
       ('fr', 'watcher.status.paused', 'Vos watchers ont été mis en pause'),
       ('fr', 'watcher.status.resumed', 'Vos watchers ne sont plus en pause'),
       ('fr', 'watcher.already.paused', 'Vos watchers sont déjà en pause'),
       ('fr', 'watcher.already.resumed', 'Vos watchers ne sont pas en pause'),
       ('fr', 'trigger.sell.under', '%1$s quand le prix de vente est en dessous de %2$s toutes les %3$s'),
       ('fr', 'trigger.sell.over', '%1$s quand le prix de vente est au dessus de %2$s toutes les %3$s'),
       ('fr', 'trigger.buy.under', '%1$s quand le prix d\'achat est en dessous de %2$s toutes les %3$s'),
       ('fr', 'trigger.buy.over', '%1$s quand le prix d\'achat est au dessus de %2$s toutes les %3$s'),
       ('fr', 'trigger.everytime', '%1$s toutes les %2$s'),
       ('fr', 'language.updated.guild', 'Le langage du serveur a été mis à jour.'),
       ('fr', 'language.updated.channel', 'Le langage du salon a été mis à jour.'),
       ('fr', 'language.updated.user', 'Votre langage a été mis à jour.'),
       ('fr', 'language.unsupported', 'Ce langage n\'est pas (encore?) supporté.'),
       ('fr', 'general.forbidden', 'Vous n\'êtes pas autorisé à faire ceci.'),
       ('fr', 'general.xodb.offline',
        'CrossoutDB semble être indisponible pour le moment, merci de réessayer plus tard.'),
       ('fr', 'general.xodb.error',
        'Une erreur est survenue lors de la récupération des données depuis CrossoutDB. Merci de réessayer plus tard.');
