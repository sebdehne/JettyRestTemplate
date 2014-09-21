package com.dehnes.rest.server.config;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;

public class Route {
    private final Pattern targetRegex;
    private final TriConsumer<Request, Response, List<String>> handler;
    private final HttpMethod httpMethod;

    public Route(String targetRegex, HttpMethod httpMethod, TriConsumer<Request, Response, List<String>> handler) {
        this.targetRegex = Pattern.compile(targetRegex);
        this.handler = handler;
        this.httpMethod = httpMethod;
    }

    public boolean test(HttpMethod httpMethod, String s) {
        return (this.httpMethod == null || this.httpMethod == httpMethod) && targetRegex.matcher(s).matches();
    }

    public void execute(Request req, Response resp) {
        Matcher matcher = targetRegex.matcher(req.getRequestURI());
        List<String> fields = new LinkedList<>();
        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                fields.add(matcher.group(i));
            }
        }

        handler.accept(req, resp, fields);
    }
}
