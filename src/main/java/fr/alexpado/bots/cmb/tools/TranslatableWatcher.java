package fr.alexpado.bots.cmb.tools;

import fr.alexpado.bots.cmb.AppConfig;
import fr.alexpado.bots.cmb.enums.WatcherType;
import fr.alexpado.bots.cmb.interfaces.translatable.Translatable;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.Watcher;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;

public class TranslatableWatcher extends Translatable {

    private Watcher watcher;

    public TranslatableWatcher(AppConfig config, Watcher watcher, String language) throws MissingTranslationException {
        super(config);
        this.watcher = watcher;
        this.fetchTranslations(language);
    }

    @Override
    public List<String> getRequiredTranslation() {
        return Arrays.asList(
                Translation.GENERAL_CURRENCY,
                Translation.WATCHERS_OTHER,
                WatcherType.NORMAL.getTranslation(),
                WatcherType.SELL_UNDER.getTranslation(),
                WatcherType.SELL_OVER.getTranslation(),
                WatcherType.BUY_UNDER.getTranslation(),
                WatcherType.BUY_OVER.getTranslation()
        );
    }

    @SneakyThrows
    public String toString() {
        WatcherType type = WatcherType.getFromId(this.watcher.getWatcherType());
        String time = new TimeConverter(this.watcher.getRepeatEvery() / 1000).toString();

        switch (type) {
            case SELL_OVER:
            case SELL_UNDER:
            case BUY_OVER:
            case BUY_UNDER:
                String advancedContent = String.format(this.getTranslation(type.getTranslation()), Utilities.money(this.watcher.getPrice(), ""));
                String watcherTextContent = String.format(this.getTranslation(Translation.WATCHERS_OTHER), this.watcher.getItemName(), advancedContent, time);
                return String.format("[%s] %s", this.watcher.getId(), watcherTextContent);
            case NORMAL:
                return String.format("[%s] %s", this.watcher.getId(), String.format(this.getTranslation(type.getTranslation()), this.watcher.getItemName(), time));
        }

        return String.format("[%s] %s", this.watcher.getId(), this.watcher.getItemName());
    }
}
