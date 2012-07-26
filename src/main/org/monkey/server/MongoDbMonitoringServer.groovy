package org.monkey.server


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
        def server = new MongoDbMonitoringServer("Local")

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
