package com.example.template.demo.unit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.dehnes.rest.demo.endpoints.PingGet;
import com.example.template.demo.category.UnitTest;

@Category(UnitTest.class)
public class PingTest {

    @Test
    public void testPing() {
        new PingGet().handle(null, null, null, null, (code, o) -> {
            Assert.assertEquals("PONG", o);
            Assert.assertEquals(200, code.intValue());
        });
    }
}
