package com.example.template;

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

import com.example.template.config.MutableConfig;
import com.example.template.config.Route;
import com.example.template.config.Routes;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        // instantiate all beans
        MutableConfig config = new MutableConfig();

        // gets the routes
        List<Route> routes = config.getInstance(Routes.class).getRoutes();

        // setup the server
        Server server = new Server(Integer.parseInt(System.getProperty("JETTY_PORT", "8080")));
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                try {
                    HttpMethod httpMethod = HttpMethod.fromString(baseRequest.getMethod());
                    routes.stream()
                            .filter(r -> !response.isCommitted())
                            .filter(r -> r.test(httpMethod, target))
                            .forEach(r -> r.getHandler().accept((Request) request, (Response) response));

                    if (!response.isCommitted()) {
                        RestResponseUtils.jsonResponse((Response) response, 404, "No handler found for " + httpMethod + " " + target);
                    }
                } catch (Exception e) {
                    RestResponseUtils.internalServer((Response) response, e);
                    logger.error("", e);
                }
            }
        });

        // start it
        server.start();
        server.join();
    }

}
