package fr.alexpado.bots.cmb.tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JSONConfiguration extends JSONObject {

    private File file;

    public JSONConfiguration(JSONObject object) {
        super(object);
    }

    public JSONConfiguration(File file) throws Exception {
        super(file.exists() ? new String(Files.readAllBytes(file.toPath())) : "{}");
        this.file = file;
        this.save();
    }

    public JSONConfiguration(String content) throws Exception {
        super(content);
    }

    public void save() throws IOException {
        FileWriter writer = new FileWriter(this.file);
        writer.write(this.toString(2));
        writer.close();
    }

    public File getFile() {
        return file;
    }

    private JSONObject putInPath(String key, Object value) throws JSONException {
        List<String> pathToConfig = Arrays.asList(key.split("\\."));

        JSONObject object = this;
        for (int i = 0; i < pathToConfig.size() - 1; i++) {
            if (!object.has(pathToConfig.get(i))) {
                object.put(pathToConfig.get(i), new JSONObject());
            }
            object = object.getJSONObject(pathToConfig.get(i));
        }
        return object.put(pathToConfig.get(pathToConfig.size() - 1), value);
    }

    @Override
    public JSONObject put(String key, boolean value) throws JSONException {
        if (key.contains(".")) {
            return this.putInPath(key, value);
        }
        return super.put(key, value);
    }

    @Override
    public JSONObject put(String key, Collection<?> value) throws JSONException {
        if (key.contains(".")) {
            return this.putInPath(key, value);
        }
        return super.put(key, value);
    }

    @Override
    public JSONObject put(String key, double value) throws JSONException {
        if (key.contains(".")) {
            return this.putInPath(key, value);
        }
        return super.put(key, value);
    }

    @Override
    public JSONObject put(String key, float value) throws JSONException {
        if (key.contains(".")) {
            return this.putInPath(key, value);
        }
        return super.put(key, value);
    }

    @Override
    public JSONObject put(String key, int value) throws JSONException {
        if (key.contains(".")) {
            return this.putInPath(key, value);
        }
        return super.put(key, value);
    }

    @Override
    public JSONObject put(String key, long value) throws JSONException {
        if (key.contains(".")) {
            return this.putInPath(key, value);
        }
        return super.put(key, value);
    }

    @Override
    public JSONObject put(String key, Map<?, ?> value) throws JSONException {
        if (key.contains(".")) {
            return this.putInPath(key, value);
        }
        return super.put(key, value);
    }

    @Override
    public JSONObject put(String key, Object value) throws JSONException {
        if (key.contains(".")) {
            return this.putInPath(key, value);
        }
        return super.put(key, value);
    }

    private Object getInPath(String key) throws JSONException {
        List<String> pathToConfig = Arrays.asList(key.split("\\."));

        JSONObject object = this;
        for (int i = 0; i < pathToConfig.size(); i++) {
            if (i == pathToConfig.size() - 1) {
                return object.get(pathToConfig.get(i));
            } else if (object.has(pathToConfig.get(i))) {
                object = object.getJSONObject(pathToConfig.get(i));
            }
        }
        return null;
    }

    @Override
    public Object get(String key) throws JSONException {
        if (key.contains(".")) {
            return this.getInPath(key);
        }
        return super.get(key);
    }

    @Override
    public boolean getBoolean(String key) throws JSONException {
        if (key.contains(".")) {
            return ((boolean) this.getInPath(key));
        }
        return super.getBoolean(key);
    }

    @Override
    public double getDouble(String key) throws JSONException {
        if (key.contains(".")) {
            return ((double) this.getInPath(key));
        }
        return super.getDouble(key);
    }

    @Override
    public float getFloat(String key) throws JSONException {
        if (key.contains(".")) {
            return ((float) this.getInPath(key));
        }
        return super.getFloat(key);
    }

    @Override
    public int getInt(String key) throws JSONException {
        if (key.contains(".")) {
            return ((int) this.getInPath(key));
        }
        return super.getInt(key);
    }

    @Override
    public JSONArray getJSONArray(String key) throws JSONException {
        if (key.contains(".")) {
            return ((JSONArray) this.getInPath(key));
        }
        return super.getJSONArray(key);
    }

    @Override
    public JSONObject getJSONObject(String key) throws JSONException {
        if (key.contains(".")) {
            return ((JSONObject) this.getInPath(key));
        }
        return super.getJSONObject(key);
    }

    @Override
    public long getLong(String key) throws JSONException {
        if (key.contains(".")) {
            return ((long) this.getInPath(key));
        }
        return super.getLong(key);
    }

    @Override
    public String getString(String key) throws JSONException {
        if (key.contains(".")) {
            return ((String) this.getInPath(key));
        }
        return super.getString(key);
    }

    private boolean hasInPath(String key) throws JSONException {
        List<String> pathToConfig = Arrays.asList(key.split("\\."));

        JSONObject object = this;
        for (int i = 0; i < pathToConfig.size(); i++) {
            if (i == pathToConfig.size() - 1) {
                return object.has(pathToConfig.get(i));
            }
            if (!object.has(pathToConfig.get(i))) {
                return false;
            }
            object = object.getJSONObject(pathToConfig.get(i));
        }
        return false;
    }

    @Override
    public boolean has(String key) {
        if (key.contains(".")) {
            return this.hasInPath(key);
        }
        return super.has(key);
    }

    private Object removePath(String key) {
        List<String> pathToConfig = Arrays.asList(key.split("\\."));

        JSONObject object = this;
        for (int i = 0; i < pathToConfig.size(); i++) {
            if (i == pathToConfig.size() - 1) {
                return object.remove(pathToConfig.get(i));
            }
            object = object.getJSONObject(pathToConfig.get(i));
        }
        return null;
    }

    @Override
    public Object remove(String key) {
        if (key.contains(".")) {
            return this.removePath(key);
        }
        return super.remove(key);
    }


}