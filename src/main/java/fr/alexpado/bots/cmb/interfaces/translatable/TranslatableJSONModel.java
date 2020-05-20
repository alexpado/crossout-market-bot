package fr.alexpado.bots.cmb.interfaces.translatable;

import fr.alexpado.bots.cmb.CrossoutConfiguration;
import fr.alexpado.bots.cmb.interfaces.JSONModel;
import fr.alexpado.bots.cmb.modules.crossout.models.Translation;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TranslatableJSONModel extends JSONModel implements ITranslatable {

    private final Map<String, String> translations = new HashMap<>();
    // Fake double extends
    private final Translatable translatable;
    private CrossoutConfiguration config;

    public TranslatableJSONModel(CrossoutConfiguration config, JSONObject dataSource) throws Exception {
        super(dataSource);
        this.config = config;
        this.translatable = new Translatable(config) {
            @Override
            public List<String> getRequiredTranslation() {
                return TranslatableJSONModel.this.getRequiredTranslation();
            }
        };
    }

    public TranslatableJSONModel(CrossoutConfiguration config) {
        super();
        this.translatable = new Translatable(config) {
            @Override
            public List<String> getRequiredTranslation() {
                return TranslatableJSONModel.this.getRequiredTranslation();
            }
        };
    }

    @Override
    public String getTranslation(String key) {
        return translatable.getTranslation(key);
    }

    @Override
    public void fetchTranslations(String language) throws MissingTranslationException {
        translatable.fetchTranslations(language);
    }

    @Override
    public List<Translation> fetch(List<String> keys, String language) throws MissingTranslationException {
        return translatable.fetch(keys, language);
    }

    public CrossoutConfiguration getConfig() {
        return config;
    }

}
