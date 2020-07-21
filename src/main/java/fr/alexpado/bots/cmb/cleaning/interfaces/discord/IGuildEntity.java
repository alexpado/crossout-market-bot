package fr.alexpado.bots.cmb.cleaning.interfaces.discord;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.*;

public interface IGuildEntity extends Identifiable<Long>, Ownable<IUserEntity>, Nameable, Imageable, LanguageHolder {}
