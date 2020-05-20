package fr.alexpado.bots.cmb.modules.discord.events;

import fr.alexpado.bots.cmb.modules.discord.models.Session;
import org.springframework.context.ApplicationEvent;

public class DiscordLoginEvent extends ApplicationEvent {

    private final Session session;

    public DiscordLoginEvent(Object source, Session session) {
        super(source);
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

}
