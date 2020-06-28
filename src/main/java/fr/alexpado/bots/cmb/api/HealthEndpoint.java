package fr.alexpado.bots.cmb.api;

import fr.alexpado.bots.cmb.interfaces.APIEndpoint;
import fr.alexpado.bots.cmb.libs.HttpRequest;
import fr.alexpado.bots.cmb.modules.crossout.models.db.HealthStat;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;

public class HealthEndpoint extends APIEndpoint<HealthStat, Void> {

    public HealthEndpoint(String apiRoot) {

        super(apiRoot);
    }

    @Override
    public Optional<HealthStat> getOne(Void identifier) {

        try {
            HttpRequest request = new HttpRequest(String.format("%s/health", this.getHost()));
            JSONObject  ignored = request.readJsonObject();

            return Optional.of(HealthStat.create(request.getLastRequestDuration()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<HealthStat> getAll() {

        throw new RuntimeException("getAll() is not compatible with this endpoint.");
    }


}
