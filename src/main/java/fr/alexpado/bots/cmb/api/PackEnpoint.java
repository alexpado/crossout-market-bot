package fr.alexpado.bots.cmb.api;

import fr.alexpado.bots.cmb.CrossoutConfiguration;
import fr.alexpado.bots.cmb.interfaces.APIEndpoint;
import fr.alexpado.bots.cmb.libs.HttpRequest;
import fr.alexpado.bots.cmb.modules.crossout.models.game.Pack;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PackEnpoint extends APIEndpoint<Pack, Integer> {

    private final CrossoutConfiguration config;

    public PackEnpoint(CrossoutConfiguration config) {

        super(config.getApiHost());
        this.config = config;
    }

    @Override
    public Optional<Pack> getOne(Integer id) {

        List<Pack> packs = this.getAll();

        for (Pack pack : packs) {
            if (pack.getId() == id) {
                return Optional.of(pack);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Pack> getAll() {

        try {
            HttpRequest request  = new HttpRequest(String.format("%s/packs", this.getHost()));
            JSONArray   array    = request.readJsonArray();
            List<Pack>  packList = new ArrayList<>();

            for (int i = 0 ; i < array.length() ; i++) {
                JSONObject     o    = array.getJSONObject(i);
                Optional<Pack> item = Pack.from(this.config, o);
                item.ifPresent(packList::add);
            }
            return packList;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
