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
import fr.alexpado.xodb4j.XoDB;
import fr.alexpado.xodb4j.interfaces.IItem;
import fr.alexpado.xodb4j.interfaces.IRarity;
import fr.alexpado.xodb4j.interfaces.common.Nameable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.CommandAutoCompleteInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Service;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.GuildEntity;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.repositories.WatcherRepository;
import xo.marketbot.services.EntitySynchronization;
import xo.marketbot.services.interactions.pagination.PaginationHandler;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InteractionWrapper {

    private final ListableBeanFactory  beanFactory;
    private final XoDB                 xoDB;
    private final InteractionExtension extension;
    private final WatcherRepository    watcherRepository;

    public InteractionWrapper(ListableBeanFactory beanFactory, EntitySynchronization entitySynchronization, XoDB xoDB, WatcherRepository watcherRepository, CommandPreprocessor preprocessor) throws Exception {

        this.beanFactory       = beanFactory;
        this.xoDB              = xoDB;
        this.watcherRepository = watcherRepository;
        this.extension         = new InteractionExtension();
        this.extension.registerPreprocessor(preprocessor);

        this.xoDB.buildCaches(true);

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
                        annotation.defer(),
                        true
                );

                InteractionMeta buttonMeta = new InteractionMeta(
                        annotation.name(),
                        annotation.description(),
                        annotation.target(),
                        options,
                        annotation.hideAsButton(),
                        annotation.defer(),
                        annotation.shouldReply()
                );

                SlashInteractionTarget        slash      = new SlashInteractionTargetImpl(obj, method, slashMeta);
                ButtonInteractionTarget       button     = new ButtonInteractionTargetImpl(obj, method, buttonMeta);
                AutocompleteInteractionTarget completion = new AutocompleteInteractionTargetImpl(slashMeta);

                completion.addCompletionProvider("rarity", this::completeItemSearch);
                completion.addCompletionProvider("faction", this::completeItemSearch);
                completion.addCompletionProvider("type", this::completeItemSearch);
                completion.addCompletionProvider("category", this::completeItemSearch);
                completion.addCompletionProvider("item", this::completeItemSearch);
                completion.addCompletionProvider("pack", this::completePackSearch);
                completion.addCompletionProvider("watcher", this::completeWatcherSearch);

                this.extension.getSlashContainer().register(slash);
                this.extension.getButtonContainer().register(button);
                this.extension.getAutocompleteContainer().register(completion);
            }
        }
    }

    public List<Command.Choice> completeItemSearch(DispatchEvent<CommandAutoCompleteInteraction> event, String name, String value) {

        CommandAutoCompleteInteraction interaction = event.getInteraction();
        Collection<IItem>              items       = this.xoDB.getItemCache().values();
        Stream<IItem>                  stream      = items.stream();

        for (OptionMapping option : interaction.getOptions()) {
            String searchValue = option.getName().equals(name) ? value.toLowerCase() : option.getAsString()
                                                                                             .toLowerCase();

            switch (option.getName().toLowerCase()) {
                case "category" -> stream = stream
                        .filter(item -> item.getCategory() != null)
                        .filter(item -> item.getCategory().getName().toLowerCase().contains(searchValue));
                case "rarity" -> stream = stream
                        .filter(item -> item.getRarity() != null)
                        .filter(item -> item.getRarity().getName().toLowerCase().contains(searchValue));
                case "faction" -> stream = stream
                        .filter(item -> item.getFaction() != null)
                        .filter(item -> item.getFaction().getName().toLowerCase().contains(searchValue));
                case "item" -> stream = stream
                        .filter(item -> item.getName().toLowerCase().contains(searchValue));
            }
        }

        List<IItem> elements = stream.toList();
        Set<String> names    = new HashSet<>();
        List<String> dupeNames = elements.stream()
                                         .map(Nameable::getName)
                                         .filter(str -> !names.add(str)).toList();

        stream = elements.stream();

        return switch (name.toLowerCase()) {
            case "category" -> stream
                    .filter(item -> item.getCategory() != null)
                    .map(item -> item.getCategory().getName()).distinct()
                    .map(str -> new Command.Choice(str, str)).toList();
            case "rarity" -> stream
                    .filter(item -> item.getRarity() != null)
                    .map(item -> item.getRarity().getName()).distinct()
                    .map(str -> new Command.Choice(str, str)).toList();
            case "faction" -> stream
                    .filter(item -> item.getFaction() != null)
                    .map(item -> item.getFaction().getName()).distinct()
                    .map(str -> new Command.Choice(str, str)).toList();
            case "item" -> stream
                    .map(item -> {
                        if (names.contains(item.getName())) {
                            return new Command.Choice(
                                    String.format(
                                            "%s (%s)",
                                            item.getName(),
                                            Optional.ofNullable(item.getRarity()).orElse(IRarity.DEFAULT).getName()
                                    ),
                                    item.getId()
                            );
                        } else {
                            return new Command.Choice(
                                    item.getName(),
                                    item.getId()
                            );
                        }
                    }).toList();
            default -> Collections.emptyList();
        };
    }

    public List<Command.Choice> completePackSearch(DispatchEvent<CommandAutoCompleteInteraction> event, String name, String value) {

        return this.xoDB.getPackCache().values().stream()
                        .filter(pack -> pack.getName().toLowerCase().contains(value.toLowerCase()))
                        .map(pack -> new Command.Choice(pack.getName(), pack.getName()))
                        .collect(Collectors.toList());
    }

    public List<Command.Choice> completeWatcherSearch(DispatchEvent<CommandAutoCompleteInteraction> event, String name, String value) {

        return this.watcherRepository.findAllByOwnerId(event.getInteraction().getUser().getIdLong())
                                     .stream()
                                     .filter(watcher -> watcher.getName().toLowerCase().contains(value.toLowerCase()))
                                     .map(watcher -> new Command.Choice(watcher.getName(), watcher.getId()))
                                     .collect(Collectors.toList());
    }

}
