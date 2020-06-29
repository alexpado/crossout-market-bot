package fr.alexpado.bots.cmb.cleaning.interfaces.common;

public interface Craftable {

    boolean isCraftable();

    double getBuyCraftPrice();

    void setBuyCraftPrice(double price);

    double getSellCraftPrice();

    void setSellCraftPrice(double price);

}
