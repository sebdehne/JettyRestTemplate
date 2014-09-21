package com.example.template.config;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;

public class Route implements BiPredicate<HttpMethod, String> {
    private final String targetRegex;
    private final BiConsumer<Request, Response> handler;
    private final HttpMethod httpMethod;

    public Route(String targetRegex, HttpMethod httpMethod, BiConsumer<Request, Response> handler) {
        this.targetRegex = targetRegex;
        this.handler = handler;
        this.httpMethod = httpMethod;
    }

    public BiConsumer<Request, Response> getHandler() {
        return handler;
    }

    @Override
    public boolean test(HttpMethod httpMethod, String s) {
        return this.httpMethod == httpMethod && s.matches(targetRegex);
    }
}
