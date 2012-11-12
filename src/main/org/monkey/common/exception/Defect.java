package org.monkey.common.exception;

public class Defect extends CommonRuntimeException{

    public Defect(String message) {
        super(message);
    }

    public Defect(String message, Throwable cause) {
        super(message, cause);
    }
}
