package com.dehnes.rest.demo.services;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownService {
    private static final Logger logger = LoggerFactory.getLogger(ShutdownService.class);

    private final AtomicReference<Server> severRef = new AtomicReference<>();

    public void setServer(Server server) {
        severRef.set(server);
    }

    public void shutdownNow() {
        new Thread(() -> {
            logger.info("Shutting down...");
            try {
                severRef.get().setStopTimeout(10000);
                severRef.get().stop();
            } catch (Exception e) {
                logger.error("", e);
            }
            severRef.get().destroy();
            logger.info("Shutting down...done");
        }).start();
    }
}
