package fr.alexpado.bots.cmb.cleaning.interfaces.discord;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.Identifiable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.LanguageHolder;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Nameable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Ownable;

public interface IChannelEntity extends Identifiable<Long>, Nameable, Ownable<IGuildEntity>, LanguageHolder {}
