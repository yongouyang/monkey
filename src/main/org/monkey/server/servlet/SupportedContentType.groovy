package org.monkey.server.servlet


public enum SupportedContentType {

    TEXT_PLAIN("text/plain"),
    APPLICATION_JSON("application/json"),
    TEXT_JSON("text/json");

    String mimeType;

    SupportedContentType(String mimeType) {
        this.mimeType = mimeType
    }


}