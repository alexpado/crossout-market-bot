package fr.alexpado.bots.cmb.throwables;

import fr.alexpado.bots.cmb.modules.crossout.models.OldTranslation;

import java.util.List;

public class MissingTranslationException extends Exception {

    private final List<String>         missingTranslations;
    private final List<OldTranslation> loadedTranslations;

    public MissingTranslationException(List<String> missingTranslations, List<OldTranslation> loadedTranslations) {

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

    public List<OldTranslation> getLoadedTranslations() {

        return this.loadedTranslations;
    }

}
