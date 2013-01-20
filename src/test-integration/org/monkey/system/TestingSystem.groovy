package org.monkey.system

import org.monkey.server.JettyServer
import org.monkey.server.RunLocalServer

class TestingSystem {

    private static boolean isRunning = false
    private static JettyServer coreApplication;

    public synchronized static void init() {
        if (!isRunning) {
            coreApplication = RunLocalServer.start()
            isRunning = true
        }
    }


}
