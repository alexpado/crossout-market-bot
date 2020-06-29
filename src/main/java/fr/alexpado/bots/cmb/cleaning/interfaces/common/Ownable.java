package fr.alexpado.bots.cmb.cleaning.interfaces.common;

public interface Ownable<T> {

    T getOwner();

    void setOwner(T owner);

}
