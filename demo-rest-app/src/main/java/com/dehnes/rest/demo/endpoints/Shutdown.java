package com.dehnes.rest.demo.endpoints;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.json.JSONObject;

import com.dehnes.rest.demo.services.ShutdownService;
import com.dehnes.rest.server.AbstractRestHandler;

public class Shutdown extends AbstractRestHandler {

    private final ShutdownService shutdownService;

    public Shutdown(ShutdownService shutdownService) {
        this.shutdownService = shutdownService;
    }

    @Override
    public void handle(String requestURI, List<String> fields, Map<String, String[]> params, JSONObject body, BiConsumer<Integer, Object> onDone) {
        shutdownService.shutdownNow();
        onDone.accept(200, "OK");
    }
}
