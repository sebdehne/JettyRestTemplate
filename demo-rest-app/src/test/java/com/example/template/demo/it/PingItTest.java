package com.example.template.demo.it;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.example.template.demo.category.IntegrationTest;

@Category(IntegrationTest.class)
public class PingItTest {

    private static String url = "http://"
            + System.getProperty("PING_APP_HOSTNAME", "localhost") + ":"
            + System.getProperty("PING_APP_PORT", "9090");

    private static final RestClient restClient = new RestClient(url);

    @Test
    public void testPing() throws IOException {
        restClient.executeGet("/ping", "$.response", (code, response) -> {
            Assert.assertEquals(200, code.intValue());
            Assert.assertEquals("PONG", response);
        });
    }

    @Test
    public void testNotFound() throws IOException {
        restClient.executeGet("/not_there", "$.response", (code, response) -> {
            Assert.assertEquals(404, code.intValue());
            Assert.assertTrue(response.startsWith("No handler found for "));
        });
    }
}
