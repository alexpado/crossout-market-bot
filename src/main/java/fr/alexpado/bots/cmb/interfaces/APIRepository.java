package fr.alexpado.bots.cmb.interfaces;

import java.util.List;
import java.util.Optional;

public abstract class APIRepository<T> {

    private String apiRoot;

    public APIRepository(String apiRoot) {
        this.apiRoot = apiRoot;
    }

    public String getApiRoot() {
        return apiRoot;
    }

    public abstract Optional<T> getOne(int id);

    public abstract List<T> getAll();

}
