package org.monkey.system

import org.monkey.server.JettyServer
import org.monkey.server.RunLocalServer

class TestingSystem {

    private static boolean isRunning = false
    private static JettyServer coreApplication;

    public static void init() {
        if (!isRunning) {
            coreApplication = RunLocalServer.start()
            isRunning = true
        }
    }


}
