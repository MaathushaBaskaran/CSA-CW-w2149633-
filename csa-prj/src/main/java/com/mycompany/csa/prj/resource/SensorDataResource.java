package com.mycompany.csa.prj.resource;

import com.mycompany.csaprj.model.SensorData;
import com.mycompany.csaprj.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class SensorDataResource {

    private String sensorId;

    public SensorDataResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // GET /api/v1/sensors/{id}/data: Fetch history
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorData> getHistoricalReadings() {
        return DataStore.getHistoricalData().getOrDefault(sensorId, new ArrayList<>());
    }

    // POST /api/v1/sensors/{id}/data: Add a new reading
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorData data) {
        // Ensure the sensor exists in the system
        if (!DataStore.getSensors().containsKey(sensorId)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Initialize the list if it's the first reading
        DataStore.getHistoricalData().putIfAbsent(sensorId, new ArrayList<>());
        
        // Add the reading
        DataStore.getHistoricalData().get(sensorId).add(data);
        
        return Response.status(Response.Status.CREATED).entity(data).build();
    }
}