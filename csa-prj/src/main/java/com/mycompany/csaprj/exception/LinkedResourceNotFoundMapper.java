package com.mycompany.csaprj.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LinkedResourceNotFoundMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        // Mapping to 422 Unprocessable Entity as per requirements
        return Response.status(422) 
                .entity("{\"error\": \"" + exception.getMessage() + "\", \"status\": 422}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}