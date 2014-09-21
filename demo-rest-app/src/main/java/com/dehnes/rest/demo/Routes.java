package com.dehnes.rest.demo;

import java.util.List;

import com.dehnes.rest.demo.endpoints.Echo;
import com.dehnes.rest.demo.endpoints.PingGet;
import com.dehnes.rest.demo.endpoints.Shutdown;
import com.dehnes.rest.server.RouteBuilder;
import com.dehnes.rest.server.config.Route;
import com.dehnes.rest.server.config.RoutesFactory;

public class Routes implements RoutesFactory {

    private final List<Route> routes;

    public Routes(PingGet pingGet,
                  Shutdown shutdown,
                  Echo echo) {

        routes = new RouteBuilder()
                .get("^/ping", pingGet)
                .post("^/shutdown", shutdown)
                .post("^/echo", echo)

                .build();
    }

    public List<Route> getRoutes() {
        return routes;
    }
}
