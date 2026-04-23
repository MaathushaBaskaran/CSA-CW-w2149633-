package com.mycompany.csaprj.resource;

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
        // 1. Check if sensor exists
        com.mycompany.csaprj.model.Sensor sensor = DataStore.getSensors().get(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // 2. Part 5.3 Logic: Check for MAINTENANCE status
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new com.mycompany.csaprj.exception.SensorUnavailableException(
                "Sensor " + sensorId + " is currently in MAINTENANCE mode and cannot accept readings."
            );
        }

        // 3. Proceed as normal
        DataStore.getHistoricalData().putIfAbsent(sensorId, new java.util.ArrayList<>());
        DataStore.getHistoricalData().get(sensorId).add(data);
        
        return Response.status(Response.Status.CREATED).entity(data).build();
    }
}