package fr.alexpado.bots.cmb.cleaning.interfaces.crossout;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.*;
import fr.alexpado.bots.cmb.cleaning.interfaces.discord.IUserEntity;

public interface IWatcher extends Identifiable<Integer>, Ownable<IUserEntity>, Marchantable, Updatable<Long>, Repeatable<Long>, Priceable {

    Integer getItemId();

    void setItemId(Integer id);

    String getItemName();

    void setItemName(String name);

    Integer getWatcherType();

    void setWatcherType();

}
