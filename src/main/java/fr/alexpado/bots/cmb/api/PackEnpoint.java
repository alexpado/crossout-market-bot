package fr.alexpado.bots.cmb.api;

import fr.alexpado.bots.cmb.models.game.Pack;
import fr.alexpado.bots.cmb.interfaces.APIRepository;
import fr.alexpado.bots.cmb.libs.HttpRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PackEnpoint extends APIRepository<Pack> {

    public PackEnpoint(String apiRoot) {
        super(apiRoot);
    }

    @Override
    public Optional<Pack> getOne(int id) {
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
            HttpRequest request = new HttpRequest(String.format("%s/packs", this.getApiRoot()));
            JSONArray array = request.readJsonArray();
            List<Pack> packList = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                Optional<Pack> item = Pack.from(o);
                item.ifPresent(packList::add);
            }
            return packList;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
