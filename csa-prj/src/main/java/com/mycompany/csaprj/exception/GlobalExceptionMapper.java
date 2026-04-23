package com.mycompany.csaprj.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        // Log the actual error internally for the developer to see in the console
        LOGGER.log(Level.SEVERE, "Unexpected server error caught by safety net", exception);

        // Return a clean, generic message to the outside world
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR) // 500
                .entity("{\"error\": \"An unexpected internal server error occurred. Please contact the administrator.\", \"status\": 500}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}