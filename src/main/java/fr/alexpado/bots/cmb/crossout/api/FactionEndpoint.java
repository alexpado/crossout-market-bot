package fr.alexpado.bots.cmb.crossout.api;

import fr.alexpado.bots.cmb.crossout.models.game.Faction;
import fr.alexpado.bots.cmb.interfaces.APIRepository;
import fr.alexpado.bots.cmb.libs.HttpRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FactionEndpoint extends APIRepository<Faction> {

    public FactionEndpoint(String apiRoot) {
        super(apiRoot);
    }

    @Override
    public Optional<Faction> getOne(int id) {
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
            HttpRequest request = new HttpRequest(String.format("%s/factions", this.getApiRoot()));
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
}
