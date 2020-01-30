package fr.alexpado.bots.cmb.tools.reaction;


import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ReactionListener extends ListenerAdapter {

    private EditorTimer editorTimer;
    private Timer timer = new Timer();
    private Message message;
    private boolean resetTimer = false;
    private HashMap<String, Consumer<ReactionAction>> reactionActions = new HashMap<>();

    public ReactionListener(Message message, int timeout) {
        this.message = message;
        this.editorTimer = new EditorTimer(timeout);
    }

    public void addAction(String emote, Consumer<ReactionAction> action) {
        message.addReaction(emote).queue();
        this.reactionActions.put(emote, action);
    }

    public void resetTimer() {
        this.resetTimer = true;
    }

    public Message getMessage() {
        return message;
    }

    public void start() {
        this.message.getJDA().addEventListener(this);
        this.timer.scheduleAtFixedRate(this.editorTimer, 0, 1000);
    }

    public void timeout(Message message) {
        this.timer.cancel();
        message.clearReactions().queue();
        message.getJDA().removeEventListener(this);
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.getMessageIdLong() != message.getIdLong()) return;
        String emote = event.getReactionEmote().getName();
        if (event.getUser().equals(event.getJDA().getSelfUser())) return;
        ReactionAction action = new ReactionAction(this, this.message, event.getUser(), event.getReaction());
        Consumer<ReactionAction> consumer = this.reactionActions.get(emote);
        if (consumer != null) consumer.accept(action);
    }

    private class EditorTimer extends TimerTask {

        private int timeout;
        private int timeLeft;

        EditorTimer(int timeout) {
            this.timeout = timeout;
            this.timeLeft = timeout;
        }


        @Override
        public void run() {
            if (resetTimer) {
                this.timeLeft = timeout;
                resetTimer = false;
            } else if (this.timeLeft == 0) {
                timeout(message);
            } else {
                this.timeLeft--;
            }

        }
    }

}
