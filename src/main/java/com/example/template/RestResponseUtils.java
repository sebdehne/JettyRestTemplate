package com.example.template;

import java.io.IOException;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RestResponseUtils {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void jsonResponse(Response r, int statusCode, Object body) {
        try {
            r.reset();
            r.setStatus(statusCode);
            r.setContentType(MimeTypes.Type.APPLICATION_JSON.asString());
            String bodyStr;
            if (body instanceof String) {
                bodyStr = gson.toJson(new JsonStringResponse((String) body));
            } else {
                bodyStr = gson.toJson(body);
            }
            r.getHttpOutput().print(bodyStr);
            r.getHttpOutput().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void internalServer(Response r, Throwable t) {
        jsonResponse(r, 500, t.getMessage());
    }

    public static class JsonStringResponse {
        private final String response;

        public JsonStringResponse(String response) {
            this.response = response;
        }
    }

}
