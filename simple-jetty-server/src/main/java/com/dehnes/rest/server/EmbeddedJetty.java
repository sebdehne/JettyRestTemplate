package com.dehnes.rest.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dehnes.rest.server.config.Route;
import com.dehnes.rest.server.config.RoutesFactory;

public class EmbeddedJetty {
    private static final Logger logger = LoggerFactory.getLogger(EmbeddedJetty.class);

    public Server start(int port, RoutesFactory routesFactory) throws Exception {
        // gets the routes
        List<Route> routes = routesFactory.getRoutes();

        // setup the server
        Server server = new Server(port);
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                try {
                    HttpMethod httpMethod = HttpMethod.fromString(baseRequest.getMethod());
                    routes.stream()
                            .filter(r -> !response.isCommitted())
                            .filter(r -> r.test(httpMethod, target))
                            .forEach(r -> r.execute((Request) request, (Response) response));

                    if (!response.isCommitted()) {
                        RestResponseUtils.setJsonResponse((Response) response, 404, "No handler found for " + httpMethod + " " + target);
                    }
                } catch (Exception e) {
                    RestResponseUtils.internalServer((Response) response, e);
                    logger.error("", e);
                }
            }
        });

        // start it
        try {
            server.start();
        } catch (Exception e) {
            logger.error("", e);
            try {
                server.stop();
            } catch (Exception e1) {
                logger.error("", e1);
            }
            server.destroy();
            throw new RuntimeException("Could not start server", e);
        }
        return server;
    }

}
