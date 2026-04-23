package com.mycompany.csa.prj.resource;

import com.mycompany.csaprj.model.Room;
import com.mycompany.csaprj.model.Sensor;
import com.mycompany.csaprj.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/sensors")
public class SensorResource {

    // GET /api/v1/sensors: Retrieve sensors with optional type filtering [cite: 133-134]
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensors(@QueryParam("type") String type) {
        List<Sensor> allSensors = new ArrayList<>(DataStore.getSensors().values());

        // Logic: If type is provided, filter the list; otherwise return all [cite: 135]
        if (type != null && !type.trim().isEmpty()) {
            List<Sensor> filtered = allSensors.stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
            return Response.ok(filtered).build();
        }

        return Response.ok(allSensors).build();
    }

    // POST /api/v1/sensors: Register a new sensor and link it to a room [cite: 128]
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerSensor(Sensor sensor) {
        // Business Logic: Verify that the specified roomId exists [cite: 129]
        Room room = DataStore.getRooms().get(sensor.getRoomId());
        if (room == null) {
            // We return 400 Bad Request now; we will upgrade to 422 in Part 5 [cite: 156]
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"Cannot register sensor. Linked Room ID does not exist.\"}")
                           .build();
        }

        // Prevent duplicate IDs
        if (DataStore.getSensors().containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                           .entity("{\"error\":\"Sensor with this ID already exists.\"}")
                           .build();
        }

        // Save sensor to the store
        DataStore.getSensors().put(sensor.getId(), sensor);

        // Update the Room's internal list to ensure data integrity [cite: 58-59]
        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }
    
    // Add this to SensorResource.java
@Path("/{sensorId}/data")
public SensorDataResource getSensorDataResource(@PathParam("sensorId") String sensorId) {
    return new SensorDataResource(sensorId);
}
}