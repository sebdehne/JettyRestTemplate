package com.dehnes.rest.demo;

import org.eclipse.jetty.server.Server;

import com.dehnes.rest.demo.services.ShutdownService;
import com.dehnes.rest.server.EmbeddedJetty;
import com.dehnes.rest.server.config.AppContext;

public class Main {

    public static void main(String[] args) throws Exception {

        AppContext config = new AppContext();

        Server server = new EmbeddedJetty().start(
                Integer.parseInt(System.getProperty("JETTY_PORT", "9090")),
                config.getInstance(Routes.class)
        );

        config.getInstance(ShutdownService.class).setServer(server);
        server.join();
    }
}
