package fr.alexpado.bots.cmb.libs.jda;

import fr.alexpado.bots.cmb.AppConfig;
import fr.alexpado.bots.cmb.libs.jda.commands.JDACommandManager;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Value;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Root class managing the Discord Bot and all its modules.
 *
 * @author alexpado
 * @version 0.1
 * @since 0.1
 */
public abstract class JDABot extends ListenerAdapter {

    private List<JDAModule> modules = new ArrayList<>();
    private JDABuilder jdaBuilder;
    private JDACommandManager commandManager;

    @Value("${discord.prefix}")
    private String prefix;

    public JDABot(AccountType accountType, AppConfig config) {
        this.jdaBuilder = new JDABuilder(accountType);
        this.commandManager = new JDACommandManager(this, config.getDiscordPrefix());

        this.jdaBuilder.addEventListeners(this);
        this.jdaBuilder.addEventListeners(this.commandManager);

    }

    /**
     * Retrieves the discord token which will be used to establish connection. The developer has to manage the way the
     * token is saved.
     *
     * @return The Discord Token to use when establishing a connection.
     */
    public abstract String getDiscordToken();

    /**
     * Runs the login sequence of JDA.
     *
     * @throws LoginException Threw if the login didn't succeed.
     */
    public final void login() throws LoginException {
        this.jdaBuilder.setToken(this.getDiscordToken());
        this.jdaBuilder.build();
    }

    /**
     * Retrieves a module by its class. If the module has not been registered, the result will be empty.
     *
     * @param clazz Class of the module to retrieve.
     * @param <T>   Instance type of the module to retrieve
     * @return An optional module. Empty if no module matches the class provided.
     * @see #registerModule(JDAModule)
     */
    public final <T extends JDAModule> Optional<T> getModule(Class<T> clazz) {
        for (JDAModule module : this.modules) {
            // Does the current module matches the class to retrieve ?
            if (module.getClass() == clazz) {
                // Hell yeah, let's return it.
                return Optional.of(clazz.cast(module));
            }
        }
        // Nothing has been found. Does the module has been registered ?
        return Optional.empty();
    }

    /**
     * Registers a new module in this bot instance.
     *
     * @param module The module to register.
     */
    public final void registerModule(JDAModule module) {
        // Let's check if this module has not been registered yet.
        if (!this.modules.contains(module)) {
            // Registering module.
            this.modules.add(module);

            // Registering listeners & command executors.
            module.getListeners().forEach(this.jdaBuilder::addEventListeners);
            module.getCommandExecutors().forEach(this.commandManager::registerCommand);
        }
    }

    public final void onCommandExecuted(CommandEvent event) {
        this.modules.forEach(module -> module.onCommandExecuted(event));
    }

    public JDACommandManager getCommandManager() {
        return commandManager;
    }

}
