package org.example.jakarthaeedemo.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;import jakarta.ws.rs.ApplicationPath;


@ApplicationPath("/api")
public class JaxrsConfig extends Application {
    // Empty: classpath scanning picks up resources/providers
}