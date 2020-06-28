package fr.alexpado.bots.cmb.throwables;

import fr.alexpado.bots.cmb.modules.crossout.models.Translation;

import java.util.List;

public class MissingTranslationException extends Exception {

    private final List<String>      missingTranslations;
    private final List<Translation> loadedTranslations;

    public MissingTranslationException(List<String> missingTranslations, List<Translation> loadedTranslations) {

        super(String.format("Missing translations : %s", String.join(", ", missingTranslations)));
        this.missingTranslations = missingTranslations;
        this.loadedTranslations  = loadedTranslations;
    }

    public int getMissingTranslationsCount() {

        return this.missingTranslations.size();
    }

    public List<String> getMissingTranslations() {

        return this.missingTranslations;
    }

    public List<Translation> getLoadedTranslations() {

        return this.loadedTranslations;
    }

}
