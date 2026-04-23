package com.mycompany.csaprj.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider // This tells JAX-RS to use this class to catch errors
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        // Returns a structured JSON body with a 409 Conflict status
        return Response.status(Response.Status.CONFLICT)
                .entity("{\"error\": \"" + exception.getMessage() + "\", \"code\": 409}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}