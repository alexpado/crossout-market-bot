package xo.marketbot.interfaces.discord;

import xo.marketbot.interfaces.common.Identifiable;
import xo.marketbot.interfaces.common.LanguageHolder;
import xo.marketbot.interfaces.common.Nameable;
import xo.marketbot.interfaces.common.Ownable;

public interface IChannelEntity extends Identifiable<Long>, Nameable, Ownable<IGuildEntity>, LanguageHolder {}
