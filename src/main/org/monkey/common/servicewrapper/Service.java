package org.monkey.common.servicewrapper;

public interface Service {

    void start() throws Exception;
    void stop() throws Exception;
    String name();
}
