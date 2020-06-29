package fr.alexpado.bots.cmb.cleaning.interfaces.api;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.Identifiable;

public interface IdentifiableEndpoint<T extends Identifiable<?>> extends Endpoint {

    T find();

}
