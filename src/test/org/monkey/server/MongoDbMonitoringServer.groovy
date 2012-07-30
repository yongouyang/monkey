package org.monkey.server

import org.monkey.common.utils.config.SystemPreferencesImpl


class MongoDbMonitoringServer implements InertialComponent {

    JettyServer jettyServer
    String environmentName

    MongoDbMonitoringServer(String environmentName) {
        this.environmentName = environmentName
    }

    @Override
    void start() {
        setupServer()
        jettyServer.start()
    }

    @Override
    void stop() {
        jettyServer.stop()
    }

    @Override
    boolean isRunning() {
        return jettyServer != null && jettyServer.isRunning()
    }

    def setupServer() {
        jettyServer = new JettyServer(30000, 30001, "MongoDBMonitoringServer", "Keep an eye on all the mongos")
    }

    public static void main(String[] args) {
        def server = new MongoDbMonitoringServer("TESTING")
        System.setProperty(SystemPreferencesImpl.MONKEY_ENV, "TESTING")

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            void run() {
                if (server.isRunning()) {
                    server.stop()
                }
            }
        })

        server.start()

        while (server.isRunning()) {
            try {
                Thread.sleep(100000)
            } catch (InterruptedException e) {
                e.printStackTrace()
                break;
            }
        }
    }
}
