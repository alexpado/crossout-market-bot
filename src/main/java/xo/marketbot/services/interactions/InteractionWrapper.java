package xo.marketbot.services.interactions;

import fr.alexpado.jda.interactions.InteractionExtension;
import fr.alexpado.jda.interactions.annotations.Interact;
import fr.alexpado.jda.interactions.entities.DispatchEvent;
import fr.alexpado.jda.interactions.impl.interactions.autocomplete.AutocompleteInteractionTargetImpl;
import fr.alexpado.jda.interactions.impl.interactions.button.ButtonInteractionTargetImpl;
import fr.alexpado.jda.interactions.impl.interactions.slash.SlashInteractionTargetImpl;
import fr.alexpado.jda.interactions.interfaces.interactions.autocomplete.AutocompleteInteractionTarget;
import fr.alexpado.jda.interactions.interfaces.interactions.button.ButtonInteractionTarget;
import fr.alexpado.jda.interactions.interfaces.interactions.slash.SlashInteractionTarget;
import fr.alexpado.jda.interactions.meta.InteractionMeta;
import fr.alexpado.jda.interactions.meta.OptionMeta;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.CommandAutoCompleteInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Service;
import xo.marketbot.entities.discord.CachedItem;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.GuildEntity;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.repositories.CachedItemRepository;
import xo.marketbot.services.EntitySynchronization;
import xo.marketbot.services.interactions.pagination.PaginationHandler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class InteractionWrapper {

    private final ListableBeanFactory  beanFactory;
    private final InteractionExtension extension;
    private final CachedItemRepository cacheRepository;

    public InteractionWrapper(ListableBeanFactory beanFactory, EntitySynchronization entitySynchronization, CachedItemRepository cacheRepository) {

        this.beanFactory     = beanFactory;
        this.cacheRepository = cacheRepository;
        this.extension       = new InteractionExtension();

        PaginationHandler paginationHandler = new PaginationHandler();
        this.extension.registerContainer(ButtonInteraction.class, paginationHandler);
        this.extension.registerHandler(ButtonInteraction.class, paginationHandler);
        this.extension.registerResponseHandler(paginationHandler);

        this.extension.useDefaultMapping();

        this.extension.getSlashContainer()
                .addClassMapping(GuildEntity.class, (ev) -> () -> entitySynchronization.mapGuild(ev.getInteraction()));
        this.extension.getSlashContainer()
                .addClassMapping(ChannelEntity.class, (ev) -> () -> entitySynchronization.mapChannel(ev.getInteraction()));
        this.extension.getSlashContainer()
                .addClassMapping(UserEntity.class, (ev) -> () -> entitySynchronization.mapUser(ev.getInteraction()));

        this.extension.getButtonContainer()
                .addClassMapping(GuildEntity.class, (ev) -> () -> entitySynchronization.mapGuild(ev.getInteraction()));
        this.extension.getButtonContainer()
                .addClassMapping(ChannelEntity.class, (ev) -> () -> entitySynchronization.mapChannel(ev.getInteraction()));
        this.extension.getButtonContainer()
                .addClassMapping(UserEntity.class, (ev) -> () -> entitySynchronization.mapUser(ev.getInteraction()));

        this.extension.getAutocompleteContainer()
                .addClassMapping(GuildEntity.class, (ev) -> () -> entitySynchronization.mapGuild(ev.getInteraction()));
        this.extension.getAutocompleteContainer()
                .addClassMapping(ChannelEntity.class, (ev) -> () -> entitySynchronization.mapChannel(ev.getInteraction()));
        this.extension.getAutocompleteContainer()
                .addClassMapping(UserEntity.class, (ev) -> () -> entitySynchronization.mapUser(ev.getInteraction()));
    }

    private void hook(JDA jda, Supplier<CommandListUpdateAction> action) {

        this.beanFactory.getBeansWithAnnotation(InteractionBean.class)
                .values()
                .forEach(this::register);

        jda.addEventListener(this.extension);
        this.extension.useDefaultMapping();
        this.extension.getSlashContainer().upsertCommands(action.get()).complete();
    }

    public void hook(JDA jda) {

        this.hook(jda, jda::updateCommands);
    }

    public void hook(Guild guild) {

        this.hook(guild.getJDA(), guild::updateCommands);
    }

    public void register(Object obj) {

        for (Method method : obj.getClass().getMethods()) {
            if (method.isAnnotationPresent(Interact.class)) {
                Interact annotation = method.getAnnotation(Interact.class);

                List<OptionMeta> options = Arrays.stream(annotation.options()).map(OptionMeta::new).toList();

                InteractionMeta slashMeta = new InteractionMeta(
                        annotation.name(),
                        annotation.description(),
                        annotation.target(),
                        options,
                        annotation.hideAsSlash(),
                        false
                );

                InteractionMeta buttonMeta = new InteractionMeta(
                        annotation.name(),
                        annotation.description(),
                        annotation.target(),
                        options,
                        annotation.hideAsButton(),
                        false
                );

                SlashInteractionTarget        slash      = new SlashInteractionTargetImpl(obj, method, slashMeta);
                ButtonInteractionTarget       button     = new ButtonInteractionTargetImpl(obj, method, buttonMeta);
                AutocompleteInteractionTarget completion = new AutocompleteInteractionTargetImpl(slashMeta);

                completion.addCompletionProvider("rarity", this::completeItemSearch);
                completion.addCompletionProvider("faction", this::completeItemSearch);
                completion.addCompletionProvider("type", this::completeItemSearch);
                completion.addCompletionProvider("category", this::completeItemSearch);
                completion.addCompletionProvider("item", this::completeItemSearch);

                this.extension.getSlashContainer().register(slash);
                this.extension.getButtonContainer().register(button);
                this.extension.getAutocompleteContainer().register(completion);
            }
        }
    }

    public List<Command.Choice> completeItemSearch(DispatchEvent<CommandAutoCompleteInteraction> event, String name, String value) {

        CommandAutoCompleteInteraction interaction = event.getInteraction();
        List<CachedItem>               items       = this.cacheRepository.findAll();
        Stream<CachedItem>             stream      = items.stream();

        for (OptionMapping option : interaction.getOptions()) {
            String searchValue = option.getName().equals(name) ? value.toLowerCase() : option.getAsString()
                    .toLowerCase();

            switch (option.getName().toLowerCase()) {
                case "category" -> stream = stream.filter(item -> item.getCategory().toLowerCase()
                        .contains(searchValue));
                case "rarity" -> stream = stream.filter(item -> item.getRarity().toLowerCase().contains(searchValue));
                case "faction" -> stream = stream.filter(item -> item.getFaction().toLowerCase().contains(searchValue));
                case "item" -> stream = stream.filter(item -> item.getName().toLowerCase().contains(searchValue));
            }
        }

        return switch (name.toLowerCase()) {
            case "category" -> stream.map(CachedItem::getCategory).distinct().map(str -> new Command.Choice(str, str))
                    .toList();
            case "rarity" -> stream.map(CachedItem::getRarity).distinct().map(str -> new Command.Choice(str, str))
                    .toList();
            case "faction" -> stream.map(CachedItem::getFaction).distinct().map(str -> new Command.Choice(str, str))
                    .toList();
            case "item" -> stream.map(item -> new Command.Choice(item.getDisplayName(), item.getId())).toList();
            default -> Collections.emptyList();
        };
    }

}
