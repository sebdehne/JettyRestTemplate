package com.example.template.endpoints;

import java.util.function.BiConsumer;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;

import com.example.template.RestResponseUtils;

public class PingGet implements BiConsumer<Request, Response> {

    @Override
    public void accept(Request request, Response response) {
        RestResponseUtils.jsonResponse(response, 200, "PONG");
    }
}
