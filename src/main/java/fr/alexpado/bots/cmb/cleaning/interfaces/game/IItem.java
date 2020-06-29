package fr.alexpado.bots.cmb.cleaning.interfaces.game;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.*;

import java.time.LocalDateTime;

public interface IItem extends Identifiable<Integer>, Nameable, Describable, Updatable<LocalDateTime>, Marchantable, Craftable {}
