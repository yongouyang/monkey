package org.monkey.common.exception;

import java.io.IOException;

public class MarshallException extends IOException {

    public MarshallException(String message, Throwable cause) {
        super(message, cause);
    }

    public MarshallException(String message) {
        super(message);
    }
}
