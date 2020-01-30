package fr.alexpado.bots.cmb.interfaces;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TranslatableJSONModel extends JSONModel {

    private Map<String, String> translations = new HashMap<>();

    public TranslatableJSONModel(JSONObject dataSource) throws Exception {
        super(dataSource);
    }

    public TranslatableJSONModel() {
        super();
    }

    public void setTranslations(Map<String, String> translations) {
        this.translations = translations;
    }

    public String getTranslation(String key) {
        return this.translations.get(key);
    }

}
