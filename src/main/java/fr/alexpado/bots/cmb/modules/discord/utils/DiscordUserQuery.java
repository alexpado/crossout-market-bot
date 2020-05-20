package fr.alexpado.bots.cmb.modules.discord.utils;


import fr.alexpado.bots.cmb.modules.discord.models.DiscordUser;
import fr.alexpado.bots.cmb.modules.discord.repositories.DiscordUserRepository;
import fr.alexpado.bots.cmb.modules.discord.web.UserResponse;

public class DiscordUserQuery {

    private final DiscordUserRepository repository;

    public DiscordUserQuery(DiscordUserRepository repository) {
        this.repository = repository;
    }

    public DiscordUser createUpdateUser(UserResponse userResponse) {
        DiscordUser dUser = this.repository.findById(userResponse.getId()).orElse(new DiscordUser());
        return this.repository.save(dUser.refresh(userResponse));
    }

}
