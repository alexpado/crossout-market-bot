package fr.alexpado.bots.cmb.cleaning.interfaces.common;

public interface Repeatable<UNIT> {

    UNIT getRepeatEvery();

    void setRepeatEvery(UNIT unit);

}
