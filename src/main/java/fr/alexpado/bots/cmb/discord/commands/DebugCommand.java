package fr.alexpado.bots.cmb.discord.commands;

import fr.alexpado.bots.cmb.crossout.models.Translation;
import fr.alexpado.bots.cmb.discord.BotCommand;
import fr.alexpado.bots.cmb.libs.HttpRequest;
import fr.alexpado.bots.cmb.libs.jdamodules.JDAModule;
import fr.alexpado.bots.cmb.libs.jdamodules.events.CommandEvent;
import fr.alexpado.bots.cmb.tools.JSONConfiguration;
import org.json.JSONObject;

/**
 * TODO: 1/30/2020
 * This need to be deleted before the release.
 */
public class DebugCommand extends BotCommand {

    public DebugCommand(JDAModule module) {
        super(module, "translate");
    }

    @Override
    public void runCommand(CommandEvent event) {
        try {
            this.downloadLanguage("en");
            this.downloadLanguage("fr");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadLanguage(String lang) throws Exception {
        HttpRequest request = new HttpRequest(String.format("https://gitlab.com/alexpado/crossout-market-bot/raw/2.0.4/src/main/resources/configs/locale/%s.json", lang));

        JSONConfiguration object = request.readJsonConfiguration();

        for (String key : object.keySet()) {
            this.addPath(lang, key, object);
        }
    }

    public void addPath(String lang, String path, JSONConfiguration translation) {
        if (translation.get(path) instanceof JSONObject) {
            JSONObject subobject = translation.getJSONObject(path);
            for (String key : subobject.keySet()) {
                this.addPath(lang, path + "." + key, translation);
            }
        } else {
            Translation t = new Translation();
            t.setLanguage(lang);
            t.setText(translation.getString(path));
            t.setTranslationKey(path);
            this.getConfig().translationRepository.save(t);
        }
    }
}
