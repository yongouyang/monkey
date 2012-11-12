package org.monkey.common.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class CommonRuntimeException extends RuntimeException {

    public CommonRuntimeException() {
    }

    public CommonRuntimeException(String message) {
        super(message);
    }

    public CommonRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonRuntimeException(Throwable cause) {
        super(cause);
    }

    @Override
    public void printStackTrace(PrintWriter writer) {
        super.printStackTrace(writer);
        ExceptionUtil.printCauseStackTraces(this, writer);
    }

    @Override
    public void printStackTrace(PrintStream stream) {
        ExceptionUtil.printStackTraceWithPrintWriter(this, stream);
    }
}
