package com.mycompany.csa.prj.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiMetadata() {
        // Creating a map to hold our API metadata as requested in the brief
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("api_version", "v1");
        metadata.put("admin_contact", "admin@smartcampus.ac.uk");
        
        // HATEOAS: Providing a map of primary resource collections
        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        metadata.put("resources", links);

        return Response.ok(metadata).build();
    }
}