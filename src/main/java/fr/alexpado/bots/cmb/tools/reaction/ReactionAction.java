package fr.alexpado.bots.cmb.tools.reaction;


import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

public class ReactionAction {

    private final ReactionListener listener;
    private final Message message;
    private final User user;
    private final MessageReaction reaction;

    ReactionAction(ReactionListener listener, Message message, User user, MessageReaction reaction) {
        this.listener = listener;
        this.message = message;
        this.user = user;
        this.reaction = reaction;
    }

    public ReactionListener getListener() {
        return listener;
    }

    public Message getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public MessageReaction getReaction() {
        return reaction;
    }

    public void removeReaction() {
        this.reaction.removeReaction(this.user).queue();
    }

}
