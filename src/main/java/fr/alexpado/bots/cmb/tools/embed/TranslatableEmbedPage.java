package fr.alexpado.bots.cmb.tools.embed;

import fr.alexpado.bots.cmb.interfaces.translatable.Translatable;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public abstract class TranslatableEmbedPage<T extends Translatable> extends EmbedPage<T> {

    private final String lang;

    protected TranslatableEmbedPage(Message message, List<T> items, int timeout, String lang) {

        super(message, items, timeout);
        this.lang = lang;
        this.reloadList();
        this.refreshEmbed();
    }

    public String getLang() {

        return this.lang;
    }

    @Override
    public String asString(T obj) {

        try {
            obj.fetchTranslations(this.lang);
            return obj.toString();
        } catch (MissingTranslationException e) {
            e.printStackTrace();
        }
        return "{:MTE:}";
    }

}
