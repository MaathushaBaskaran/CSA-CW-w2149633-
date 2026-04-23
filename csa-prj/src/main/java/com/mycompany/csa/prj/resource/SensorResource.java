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

    // GET /api/v1/sensors: Supports optional query parameter 'type' for filtering 
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensors(@QueryParam("type") String type) {
        List<Sensor> allSensors = new ArrayList<>(DataStore.getSensors().values());

        if (type != null && !type.trim().isEmpty()) {
            // Filter the list to only include matching sensor types [cite: 135]
            List<Sensor> filtered = allSensors.stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
            return Response.ok(filtered).build();
        }

        return Response.ok(allSensors).build();
    }

    // POST /api/v1/sensors: Registers a new sensor with a room existence check [cite: 128-129]
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerSensor(Sensor sensor) {
        // 1. Integrity Check: Verify the roomId exists in our system 
        Room room = DataStore.getRooms().get(sensor.getRoomId());
        if (room == null) {
            // We'll return 400 for now; Part 5 will upgrade this to a custom Exception [cite: 156]
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"The specified Room ID does not exist.\"}")
                           .build();
        }

        // 2. Prevent duplicate Sensor IDs
        if (DataStore.getSensors().containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                           .entity("{\"error\":\"Sensor with this ID already exists.\"}")
                           .build();
        }

        // 3. Save the Sensor
        DataStore.getSensors().put(sensor.getId(), sensor);

        // 4. Update the Room: Add this sensor ID to the room's internal list [cite: 58-59]
        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }
}