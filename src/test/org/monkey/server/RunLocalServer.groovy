package org.monkey.server

import org.monkey.common.utils.config.ApplicationStartupUtils

class RunLocalServer {

    private static JettyServer server

    public static void main(String[] args) {
        start()
    }

    public static JettyServer start() {
        ApplicationStartupUtils.initStartupOptions("TESTING")
        // todo - need to figure out a better way to specify the port number
        server = new JettyServer(ApplicationStartupUtils.monkeyHttpPort, "Core Service", "Provides core application features")

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            void run() {
                if (server.isRunning()) {
                    server.stop()
                }
            }
        })

        server.start()
        return server
    }

}
