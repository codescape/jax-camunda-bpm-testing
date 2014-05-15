package com.opitzconsulting.pizza.process;

import com.opitzconsulting.pizza.Order;
import lombok.extern.java.Log;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.impl.util.json.JSONArray;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import javax.inject.Inject;
import javax.inject.Named;

@Log
@Named("geoAdapter")
public class GeoAdapter {

    @Inject
    @Named
    private Order order;

    public void retrieveCity() throws Exception {
        log.info("Ermittlung der Geodaten anhand der Postleitzahl...");

        ClientRequest request = new ClientRequest("http://api.geonames.org/postalCodeSearchJSON");
        request.queryParameter("postalcode", order.getZip());
        request.queryParameter("country", "DE");
        request.queryParameter("username", "opitz");
        request.accept("application/json");
        ClientResponse<String> response = request.get(String.class);

        log.info("\n\n" + response.getEntity() + "\n\n");

        JSONObject jsonObject = new JSONObject(response.getEntity());
        JSONArray jsonArray = jsonObject.getJSONArray("postalCodes");

        if (jsonArray.length() == 0) {
            throw new BpmnError("CITY_NOT_FOUND");
        }

        String city = jsonArray.getJSONObject(0).getString("placeName");

        order.setCity(city);
        log.info("Ermittelte Stadt: " + city);
    }

}
