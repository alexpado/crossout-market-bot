package fr.alexpado.bots.cmb.cleaning.interfaces.discord;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.Identifiable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Imageable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Nameable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Ownable;

public interface IGuildEntity extends Identifiable<Long>, Ownable<IUserEntity>, Nameable, Imageable {}
