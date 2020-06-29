package fr.alexpado.bots.cmb.cleaning.interfaces.web;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.Identifiable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Ownable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Updatable;
import fr.alexpado.bots.cmb.cleaning.interfaces.discord.IUserEntity;

public interface ISession extends Identifiable<String>, Updatable<Long>, Ownable<IUserEntity> {

    Long getCreatedAt();

    void setCreatedAt(Long timestamp);

    String getIpAddress();

    void setIpAddress(String ipAddress);

}
