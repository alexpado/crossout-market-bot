package xo.marketbot.interfaces.discord;

import xo.marketbot.interfaces.common.Identifiable;
import xo.marketbot.interfaces.common.Imageable;
import xo.marketbot.interfaces.common.LanguageHolder;
import xo.marketbot.interfaces.common.Nameable;

public interface IGuildEntity extends Identifiable<Long>, Nameable, Imageable, LanguageHolder {}
