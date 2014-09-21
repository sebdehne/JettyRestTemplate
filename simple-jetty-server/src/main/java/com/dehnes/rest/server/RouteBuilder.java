package com.dehnes.rest.server;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;

import com.dehnes.rest.server.config.Route;
import com.dehnes.rest.server.config.TriConsumer;

public class RouteBuilder {

    private final String prefix;
    private final List<Route> routes;

    public RouteBuilder() {
        this("");
    }

    public RouteBuilder(String prefix) {
        this(Collections.emptyList(), prefix);
    }

    public RouteBuilder(List<Route> routes, String prefix) {
        this.routes = routes;
        this.prefix = prefix;
    }

    public RouteBuilder get(String regex, TriConsumer<Request, Response, List<String>> h) {
        return add(regex, HttpMethod.GET, h);
    }

    public RouteBuilder post(String regex, TriConsumer<Request, Response, List<String>> h) {
        return add(regex, HttpMethod.POST, h);
    }

    public RouteBuilder put(String regex, TriConsumer<Request, Response, List<String>> h) {
        return add(regex, HttpMethod.PUT, h);
    }

    public RouteBuilder delete(String regex, TriConsumer<Request, Response, List<String>> h) {
        return add(regex, HttpMethod.DELETE, h);
    }

    public RouteBuilder any(String regex, TriConsumer<Request, Response, List<String>> h) {
        return add(regex, null, h);
    }

    public RouteBuilder add(String regex, HttpMethod method, TriConsumer<Request, Response, List<String>> h) {
        List<Route> l = new LinkedList<>();
        l.addAll(routes);
        l.add(new Route(prefix + regex, method, h));
        return new RouteBuilder(Collections.unmodifiableList(l), prefix);
    }

    public List<Route> build() {
        return routes;
    }
}
