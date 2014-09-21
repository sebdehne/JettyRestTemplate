package com.example.template.endpoints;

import java.util.function.BiConsumer;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;

public class CauseServerError implements BiConsumer<Request, Response> {

    @Override
    public void accept(Request request, Response response) {
        throw new RuntimeException("Causing an error");
    }
}
