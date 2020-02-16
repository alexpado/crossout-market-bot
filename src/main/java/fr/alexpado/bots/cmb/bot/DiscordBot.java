package fr.alexpado.bots.cmb.bot;

import fr.alexpado.bots.cmb.AppConfig;
import fr.alexpado.bots.cmb.libs.jda.JDABot;
import fr.alexpado.bots.cmb.repositories.DiscordGuildRepository;
import fr.alexpado.bots.cmb.repositories.DiscordUserRepository;
import fr.alexpado.bots.cmb.repositories.TranslationRepository;
import fr.alexpado.bots.cmb.repositories.WatcherRepository;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;

import javax.annotation.Nonnull;

public class DiscordBot extends JDABot {

    public final static String INVITE = "https://discordapp.com/api/oauth2/authorize?client_id=500032551977746453&permissions=59456&scope=bot";
    private static DiscordBot instance;
    public static JDA jda;
    private AppConfig config;

    public DiscordBot(AppConfig config) {
        super(AccountType.BOT, config);
        this.config = config;
        this.registerModules();
        DiscordBot.instance = this;
    }

    @Override
    public void onGenericEvent(@Nonnull GenericEvent event) {
        DiscordBot.jda = event.getJDA();
    }

    public static DiscordBot getInstance() {
        return instance;
    }

    public void registerModules() {
        this.registerModule(new CrossoutModule(this));
    }

    @Override
    public String getDiscordToken() {
        return this.config.getDiscordToken();
    }

    public AppConfig getConfig() {
        return config;
    }


    public TranslationRepository getTranslationRepository() {
        return this.config.translationRepository;
    }

    public WatcherRepository getWatcherRepository() {
        return this.config.watcherRepository;
    }

    public DiscordUserRepository getDiscordUserRepository() {
        return this.config.discordUserRepository;
    }

    public DiscordGuildRepository getDiscordGuildRepository() {
        return this.config.discordGuildRepository;
    }

}
