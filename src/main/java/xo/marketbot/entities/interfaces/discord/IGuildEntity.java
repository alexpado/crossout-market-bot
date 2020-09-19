package xo.marketbot.entities.interfaces.discord;

import xo.marketbot.entities.interfaces.common.Identifiable;
import xo.marketbot.entities.interfaces.common.Imageable;
import xo.marketbot.entities.interfaces.common.LanguageHolder;
import xo.marketbot.entities.interfaces.common.Nameable;

public interface IGuildEntity extends Identifiable<Long>, Nameable, Imageable, LanguageHolder {}
