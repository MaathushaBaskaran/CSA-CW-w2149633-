package com.mycompany.csaprj.resource;

import com.mycompany.csaprj.model.Room;
import com.mycompany.csaprj.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/rooms")
public class SensorRoomResource {

    // GET /api/v1/rooms: Provide a comprehensive list of all rooms [cite: 115]
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRooms() {
        return Response.ok(DataStore.getRooms().values()).build();
    }

    // GET /api/v1/rooms/{roomId}: Fetch detailed metadata for a specific room [cite: 117]
    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.getRooms().get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\":\"Room not found\"}")
                           .build();
        }
        return Response.ok(room).build();
    }

    // POST /api/v1/rooms: Enable the creation of new rooms [cite: 116]
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"Room ID is required\"}")
                           .build();
        }
        
        if (DataStore.getRooms().containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                           .entity("{\"error\":\"Room already exists\"}")
                           .build();
        }
        
        DataStore.getRooms().put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    // DELETE /api/v1/rooms/{roomId}: Allow room decommissioning with safety constraint [cite: 121-122]
    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.getRooms().get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\":\"Room not found\"}")
                           .build();
        }
        
        // Safety constraint: block deletion if sensors are assigned [cite: 122-123]
        if (!room.getSensorIds().isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                           .entity("{\"error\":\"Cannot delete room. Active sensors are still assigned to it.\"}")
                           .build();
        }

        DataStore.getRooms().remove(roomId);
        return Response.noContent().build(); 
    }
}