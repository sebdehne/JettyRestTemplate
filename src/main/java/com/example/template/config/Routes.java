package com.example.template.config;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jetty.http.HttpMethod;

import com.example.template.endpoints.CauseServerError;
import com.example.template.endpoints.PingGet;

public class Routes {

    private final List<Route> routes;

    public Routes(PingGet pingGet, CauseServerError causeServerError) {
        routes = Arrays.asList(
                new Route("^/ping", HttpMethod.GET, pingGet),
                new Route("^/error", HttpMethod.GET, causeServerError)
        );
    }

    public List<Route> getRoutes() {
        return routes;
    }
}
