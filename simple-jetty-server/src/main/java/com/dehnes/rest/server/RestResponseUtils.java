package com.dehnes.rest.server;

import java.io.IOException;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RestResponseUtils {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void setJsonResponse(Response r, Object body) {
        setJsonResponse(r, 200, body);
    }

    public static void setJsonResponse(Response r, int statusCode, Object body) {
        try {
            r.reset();
            r.setStatus(statusCode);
            r.setContentType(MimeTypes.Type.APPLICATION_JSON.asString());
            String bodyStr;
            if (body instanceof String) {
                bodyStr = new JSONObject().put("response", body.toString()).toString();
            } else if (body instanceof JSONObject || body instanceof JSONArray) {
                bodyStr = body.toString();
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
        setJsonResponse(r, 500, t.getMessage());
    }

}
