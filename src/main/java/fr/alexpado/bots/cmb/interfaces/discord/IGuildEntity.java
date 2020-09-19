package fr.alexpado.bots.cmb.interfaces.discord;

import fr.alexpado.bots.cmb.interfaces.common.Identifiable;
import fr.alexpado.bots.cmb.interfaces.common.Imageable;
import fr.alexpado.bots.cmb.interfaces.common.LanguageHolder;
import fr.alexpado.bots.cmb.interfaces.common.Nameable;

public interface IGuildEntity extends Identifiable<Long>, Nameable, Imageable, LanguageHolder {}
