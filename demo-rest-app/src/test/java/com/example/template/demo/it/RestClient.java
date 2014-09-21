package com.example.template.demo.it;

import java.io.IOException;
import java.util.function.BiConsumer;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.jayway.jsonpath.JsonPath;

public class RestClient {

    private final String url;

    public RestClient(String url) {
        this.url = url;
    }

    public  void executeGet(String target, String jsonPath, BiConsumer<Integer, String> onReady) throws IOException {
        execute(HttpGet.METHOD_NAME, target, null, jsonPath, onReady);
    }

    public void executePut(String target, String body, String jsonPath, BiConsumer<Integer, String> onReady) throws IOException {
        execute(HttpPut.METHOD_NAME, target, body, jsonPath, onReady);
    }

    public void execute(String m, String target, String body, String jsonPath, BiConsumer<Integer, String> onReady) throws IOException {
        Request r;
        switch (m) {
            case "GET":
                r = Request.Get(url + target);
                break;
            case "PUT":
                r = Request.Put(url + target);
                break;
            default:
                throw new RuntimeException("Unknown method " + m);
        }
        if (body != null) {
            r = r.bodyString(body, ContentType.APPLICATION_JSON);
        }
        HttpResponse response = r.execute().returnResponse();
        String json = EntityUtils.toString(response.getEntity());
        onReady.accept(
                response.getStatusLine().getStatusCode(),
                jsonPath == null ? json : JsonPath.read(json, jsonPath));
    }
}
