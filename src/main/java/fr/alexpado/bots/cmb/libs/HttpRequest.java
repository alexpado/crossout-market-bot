package fr.alexpado.bots.cmb.libs;

import fr.alexpado.bots.cmb.tools.JSONConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequest {

    private final URLConnection connection;
    private       long          lastRequestDuration;

    public HttpRequest(String url) throws IOException {

        URL urlInstance = new URL(url);
        this.connection = urlInstance.openConnection();

        Logger log = LoggerFactory.getLogger(HttpRequest.class);
        log.debug(url);

        this.connection.setRequestProperty("X-RequestedBy", "DiscordBot");
        this.connection.setRequestProperty("X-DiscordBot", "CrossoutMarketBot");
    }

    private String readOutput() throws IOException {

        long start = System.currentTimeMillis();
        this.connection.connect();
        long end = System.currentTimeMillis();
        this.lastRequestDuration = end - start;
        InputStreamReader isr = new InputStreamReader(this.connection.getInputStream());

        BufferedReader reader  = new BufferedReader(isr);
        StringBuilder  builder = new StringBuilder();
        String         line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        return builder.toString();
    }

    public JSONArray readJsonArray() throws IOException {

        return new JSONArray(this.readOutput());
    }

    public JSONObject readJsonObject() throws IOException {

        return new JSONObject(this.readOutput());
    }

    public long getLastRequestDuration() {

        return this.lastRequestDuration;
    }

    public JSONConfiguration readJsonConfiguration() throws Exception {

        return new JSONConfiguration(this.readOutput());
    }

}
