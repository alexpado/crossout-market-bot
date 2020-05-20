package fr.alexpado.bots.cmb.api;

import fr.alexpado.bots.cmb.interfaces.APIEndpoint;
import fr.alexpado.bots.cmb.libs.HttpRequest;
import fr.alexpado.bots.cmb.modules.crossout.models.game.Faction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FactionEndpoint extends APIEndpoint<Faction, Integer> {

    public FactionEndpoint(String apiRoot) {
        super(apiRoot);
    }

    @Override
    public Optional<Faction> getOne(Integer id) {
        List<Faction> factions = this.getAll();
        for (Faction rarity : factions) {
            if (rarity.getId() == id) {
                return Optional.of(rarity);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Faction> getAll() {
        List<Faction> factionList = new ArrayList<>();

        try {
            HttpRequest request = new HttpRequest(String.format("%s/factions", this.getHost()));
            JSONArray array = request.readJsonArray();

            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                Optional<Faction> faction = Faction.from(o);
                faction.ifPresent(factionList::add);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return factionList;
    }

    public boolean exists(String name) {
        for (Faction element : this.getAll()) {
            if (element.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

}
