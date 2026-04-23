package com.mycompany.csaprj.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException exception) {
        return Response.status(Response.Status.FORBIDDEN) // 403
                .entity("{\"error\": \"" + exception.getMessage() + "\", \"status\": 403}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}