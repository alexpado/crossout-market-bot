package fr.alexpado.bots.cmb.discord;

import fr.alexpado.bots.cmb.AppConfig;
import fr.alexpado.bots.cmb.libs.jdamodules.JDABot;
import net.dv8tion.jda.api.AccountType;

public class DiscordBot extends JDABot {

    public final static String INVITE = "https://discordapp.com/api/oauth2/authorize?client_id=500032551977746453&permissions=59456&scope=bot";

    private AppConfig config;

    public DiscordBot(AppConfig config) {
        super(AccountType.BOT, config);
        this.config = config;

        this.registerModules();
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
}
