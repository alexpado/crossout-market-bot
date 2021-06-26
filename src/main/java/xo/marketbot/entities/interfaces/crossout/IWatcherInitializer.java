package xo.marketbot.entities.interfaces.crossout;

import xo.marketbot.entities.discord.Watcher;

public interface IWatcherInitializer {

    void initialize(Watcher watcher) throws Exception;

}
