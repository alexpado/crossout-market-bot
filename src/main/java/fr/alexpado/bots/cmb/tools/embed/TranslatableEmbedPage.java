package fr.alexpado.bots.cmb.tools.embed;

import fr.alexpado.bots.cmb.interfaces.TranslatableObject;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public abstract class TranslatableEmbedPage<T extends TranslatableObject> extends EmbedPage<T> {

    private String lang;

    protected TranslatableEmbedPage(Message message, List<T> items, int timeout, String lang) {
        super(message, items, timeout);
        this.lang = lang;
        this.reloadList();
        this.refreshEmbed();
    }

    public String getLang() {
        return lang;
    }

    @Override
    public String asString(T obj) {
        return obj.toString(this.lang);
    }
}
