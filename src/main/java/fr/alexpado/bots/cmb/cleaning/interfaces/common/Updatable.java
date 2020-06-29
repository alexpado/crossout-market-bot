package fr.alexpado.bots.cmb.cleaning.interfaces.common;

public interface Updatable<UNIT> {

    UNIT getLastUpdate();

    void setLastUpdate(UNIT time);

}
