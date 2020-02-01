package fr.alexpado.bots.cmb.models.game;

import fr.alexpado.bots.cmb.bot.DiscordBot;
import fr.alexpado.bots.cmb.interfaces.TranslatableJSONModel;
import fr.alexpado.bots.cmb.libs.TKey;
import fr.alexpado.bots.cmb.tools.Utilities;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.Optional;

@Getter
public class Pack extends TranslatableJSONModel {

    private int id;
    private String key;
    private String name;
    private int sellSum;
    private int buySum;

    public Pack(JSONObject source) {
        this.reload(source);
    }

    public static Optional<Pack> from(JSONObject dataSource) {
        try {
            return Optional.of(new Pack(dataSource));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean reload(JSONObject dataSource) {
        try {
            this.id = dataSource.getInt("id");
            this.key = dataSource.getString("key");
            this.name = dataSource.getString("name");
            this.sellSum = dataSource.getInt("sellsum");
            this.buySum = dataSource.getInt("buysum");
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getPackPicture() {
        return String.format("https://crossoutdb.com/img/premiumpackages/%s.jpg", this.getKey());
    }

    public EmbedBuilder getAsEmbed(JDA jda) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setAuthor(this.getTranslation(TKey.DISCORD_INVITE), DiscordBot.INVITE, jda.getSelfUser().getAvatarUrl());
        builder.setTitle(this.name, String.format("https://crossoutdb.com/packs?ref=crossoutmarketbot#pack=%s", this.getKey()));

        builder.addField(this.getTranslation(TKey.ITEM_SELL), Utilities.money(this.buySum, ""), true);
        builder.addField(this.getTranslation(TKey.ITEM_BUY), Utilities.money(this.buySum, ""), true);

        builder.setImage(this.getPackPicture());
        builder.setColor(Color.WHITE);

        return builder;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
