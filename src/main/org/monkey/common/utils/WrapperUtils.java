package org.monkey.common.utils;

import org.tanukisoftware.wrapper.WrapperListener;
import org.tanukisoftware.wrapper.WrapperManager;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class WrapperUtils {

    public static void startApplication(final WrapperListener listener, String[] args) {
        if (WrapperManager.isControlledByNativeWrapper()) {
            WrapperManager.start(listener, args);
        } else {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    listener.stop(0);
                }
            });
            listener.start(args);
        }
    }

    public static void registerServiceWrapperMBean() {
        try {
            org.tanukisoftware.wrapper.jmx.WrapperManager managerBean = new org.tanukisoftware.wrapper.jmx.WrapperManager();
            ObjectName name = new ObjectName("wrapper:type=ServiceWrapper");
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            server.registerMBean(managerBean, name);
        } catch (Exception e) {
            System.err.println("Failed to register ServiceWrapper MBean");
            e.printStackTrace();
        }
    }

    public static void controlEvent(int event) {
        if (WrapperManager.isControlledByNativeWrapper()) {
            WrapperManager.log(WrapperManager.WRAPPER_LOG_LEVEL_INFO, "Control Event: to be handled by Native Wrapper");
        } else {
            if ((event == WrapperManager.WRAPPER_CTRL_C_EVENT) ||
                    (event == WrapperManager.WRAPPER_CTRL_CLOSE_EVENT) ||
                    (event == WrapperManager.WRAPPER_CTRL_SHUTDOWN_EVENT)) {
                WrapperManager.stop(0);
            }
        }
    }
}
