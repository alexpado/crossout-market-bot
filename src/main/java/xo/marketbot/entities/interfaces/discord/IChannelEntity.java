package xo.marketbot.entities.interfaces.discord;

import xo.marketbot.entities.interfaces.common.Identifiable;
import xo.marketbot.entities.interfaces.common.LanguageHolder;
import xo.marketbot.entities.interfaces.common.Nameable;
import xo.marketbot.entities.interfaces.common.Ownable;

public interface IChannelEntity extends Identifiable<Long>, Nameable, Ownable<IGuildEntity>, LanguageHolder {}
