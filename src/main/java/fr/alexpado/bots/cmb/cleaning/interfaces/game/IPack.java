package fr.alexpado.bots.cmb.cleaning.interfaces.game;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.Identifiable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Marchantable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Nameable;

public interface IPack extends Identifiable<Integer>, Nameable, Marchantable {

    String getKey();

    void setKey();

    double getPriceUSD();

    void setPriceUSD(double price);

    double getPriceEUR();

    void setPriceEUR(double price);

    double getPriceGBP();

    void setPriceGBP(double price);

    double getPriceRUB();

    void setPriceRUB(double price);

    double getRawCoins();

    void setRawCoins(double coins);

}
