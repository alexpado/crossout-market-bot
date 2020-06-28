package fr.alexpado.bots.cmb.libs.jda;


import fr.alexpado.bots.cmb.libs.jda.commands.JDACommandExecutor;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Class holding main information about a module.
 *
 * @author alexpado
 * @version 0.1
 * @since 0.1
 */
public abstract class JDAModule {

    private final JDABot bot;

    public JDAModule(JDABot bot) throws RuntimeException {

        this.bot = bot;
    }

    /**
     * Get the bot that uses this module.
     *
     * @return The {@link JDABot} instance.
     */
    public JDABot getBot() {

        return this.bot;
    }

    /**
     * Get this module name that will be also used for the configuration file name. Should be unique or the
     * configuration files will be messed up. Avoid special character as each OS have its own rules for file.
     *
     * @return The module name that will be used in logs when needed.
     */
    public abstract String getName();

    /**
     * Retrieve a list of listener that will be registered when the module get itself registered.
     *
     * @return A list of listener.
     */
    @Nonnull
    public List<ListenerAdapter> getListeners() {

        return new ArrayList<>();
    }

    public List<JDACommandExecutor> getCommandExecutors() {

        return new ArrayList<>();
    }


    public void onCommandExecuted(CommandEvent event) {

    }

}
