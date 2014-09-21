package com.dehnes.rest.demo.endpoints;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.json.JSONObject;

import com.dehnes.rest.server.AbstractRestHandler;

public class Echo extends AbstractRestHandler {

    @Override
    public void handle(String requestURI, List<String> fields, Map<String, String[]> params, JSONObject body, BiConsumer<Integer, Object> onDone) {
        onDone.accept(200, body);
    }
}
