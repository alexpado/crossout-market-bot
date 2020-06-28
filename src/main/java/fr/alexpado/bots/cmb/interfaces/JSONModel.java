package fr.alexpado.bots.cmb.interfaces;

import org.json.JSONObject;

public abstract class JSONModel {

    public JSONModel() {

    }

    public JSONModel(JSONObject dataSource) throws Exception {

        if (!this.reload(dataSource)) {
            throw new Exception("Couldn't load item with JSON " + dataSource.toString());
        }
    }

    public abstract boolean reload(JSONObject dataSource);

}
