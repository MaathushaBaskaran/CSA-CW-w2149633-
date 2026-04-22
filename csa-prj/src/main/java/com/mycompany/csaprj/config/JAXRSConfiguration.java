package com.mycompany.csaprj.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configures a Jakarta RESTful Web Services for the application.
 * This establishes the base URI for all resource URIs provided by the API.
 */
@ApplicationPath("/api/v1")
public class JAXRSConfiguration extends Application {
    // Nothing else needs to go in here! 
    // This simple class tells GlassFish to activate JAX-RS and route requests here.
}