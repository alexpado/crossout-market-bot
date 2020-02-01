package fr.alexpado.bots.cmb.bot;

import fr.alexpado.bots.cmb.AppConfig;
import fr.alexpado.bots.cmb.libs.jda.JDABot;
import fr.alexpado.bots.cmb.repositories.*;
import net.dv8tion.jda.api.AccountType;

public class DiscordBot extends JDABot {

    private static DiscordBot instance;

    public static DiscordBot getInstance() {
        return instance;
    }

    public final static String INVITE = "https://discordapp.com/api/oauth2/authorize?client_id=500032551977746453&permissions=59456&scope=bot";

    private AppConfig config;

    public DiscordBot(AppConfig config) {
        super(AccountType.BOT, config);
        this.config = config;
        this.registerModules();
        DiscordBot.instance = this;
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
