package xo.marketbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JdaStore extends ListenerAdapter {

    private JDA jda;

    @Override
    public void onGenericEvent(@NotNull GenericEvent event) {

        this.jda = event.getJDA();
    }

    public Optional<JDA> getJda() {

        return Optional.ofNullable(this.jda);
    }
}
