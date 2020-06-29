package fr.alexpado.bots.cmb.cleaning.interfaces.discord;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.Identifiable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Imageable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Nameable;

public interface IUserEntity extends Identifiable<Long>, Nameable, Imageable {

    String getDiscriminator();

    void setDiscriminator(String discriminator);

}
