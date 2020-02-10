package fr.alexpado.bots.cmb.api;

import fr.alexpado.bots.cmb.interfaces.APIEndpoint;
import fr.alexpado.bots.cmb.libs.HttpRequest;
import fr.alexpado.bots.cmb.models.game.Type;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TypeEndpoint extends APIEndpoint<Type, Integer> {

    public TypeEndpoint(String apiRoot) {
        super(apiRoot);
    }

    @Override
    public Optional<Type> getOne(Integer id) {
        List<Type> types = this.getAll();
        for (Type type : types) {
            if (type.getId() == id) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Type> getAll() {
        List<Type> typeList = new ArrayList<>();

        try {
            HttpRequest request = new HttpRequest(String.format("%s/types", this.getHost()));
            JSONArray array = request.readJsonArray();

            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                Optional<Type> type = Type.from(o);
                type.ifPresent(typeList::add);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return typeList;
    }
}
