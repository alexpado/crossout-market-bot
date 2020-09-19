package fr.alexpado.bots.cmb.interfaces.discord;

import fr.alexpado.bots.cmb.interfaces.common.Identifiable;
import fr.alexpado.bots.cmb.interfaces.common.LanguageHolder;
import fr.alexpado.bots.cmb.interfaces.common.Nameable;
import fr.alexpado.bots.cmb.interfaces.common.Ownable;

public interface IChannelEntity extends Identifiable<Long>, Nameable, Ownable<IGuildEntity>, LanguageHolder {}
