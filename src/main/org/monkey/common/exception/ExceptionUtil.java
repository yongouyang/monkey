package org.monkey.common.exception;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.apache.commons.lang.SystemUtils.LINE_SEPARATOR;

public class ExceptionUtil {
    private static final int MAX_LENGTH = 10 * 1024;

    public static String stackTrace(Throwable e) {
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer);
        try {
            e.printStackTrace(printer);
        } finally {
            printer.flush();
        }
        return writer.toString();
    }

    public static void printStackTraceWithPrintWriter(Throwable throwable, PrintStream printStream) {
        PrintWriter writer = new PrintWriter(printStream);
        try {
            throwable.printStackTrace(writer);
        } finally {
            writer.flush();
        }
    }

    public static void printCauseStackTraces(Throwable throwable, PrintWriter writer) {
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause instanceof HasHttpExceptionBody) {
            printServerExceptionStackTrace(writer, (HasHttpExceptionBody) rootCause);
        } else if (throwable instanceof HasHttpExceptionBody) {
            printServerExceptionStackTrace(writer, (HasHttpExceptionBody) throwable);
        } else if (rootCause instanceof HasMultipleCauses) {
            printMultipleCauseStackTraces(writer, rootCause);
        } else if (throwable instanceof HasMultipleCauses) {
            printMultipleCauseStackTraces(writer, throwable);
        } else if (findCause(throwable, HasLargeCauseDetails.class) != null) {
            printJson(writer, findCause(throwable, HasLargeCauseDetails.class));
        } else if (throwable instanceof HasLargeCauseDetails) {
            printJson(writer, (HasLargeCauseDetails) throwable);
        }
    }

    private static HasLargeCauseDetails findCause(Throwable throwable, Class<HasLargeCauseDetails> type) {
        int index = ExceptionUtils.indexOfType(throwable, type);
        if (index < 0) {
            return null;
        }
        return (HasLargeCauseDetails) ExceptionUtils.getThrowableList(throwable).get(index);
    }

    private static void printJson(PrintWriter writer, HasLargeCauseDetails largeCauseDetails) {
        writer.print(indent("Caused by " + largeCauseDetails.label() + ": \"" + largeCauseDetails.causeDetails() + "\""));
    }

    private static void printServerExceptionStackTrace(PrintWriter writer, HasHttpExceptionBody hasHttpExceptionBody) {
        writer.print(indent("Caused by Server Error: " + hasHttpExceptionBody.body().trim()));
    }

    private static void printMultipleCauseStackTraces(PrintWriter writer, Throwable rootCause) {
        List<Throwable> causes = ((HasMultipleCauses) rootCause).getCauses();
        if (CollectionUtils.isNotEmpty(causes)) {
            writer.println("Caused by the following " + causes.size() + " exception(s): ");
            for (int i = 0; i < causes.size(); i++) {
                Throwable cause = causes.get(i);
                writer.println(indent("-----------------"));
                writer.print(indent("[" + i + "] " + stackTrace(cause)));
            }
            writer.println();
            writer.println(indent("-----------------"));
        }
    }

    public static String indent(String message) {
        return "\t" + (message.replaceAll(LINE_SEPARATOR, LINE_SEPARATOR + "\t").trim());
    }

    public static String truncate(String message) {
        return truncate(message, MAX_LENGTH);
    }

    public static String truncate(String message, int maxLength) {
        return message.length() <= maxLength ? message : (message.substring(0, maxLength) + " ... (message has been truncated)");
    }

    public static Throwable getOriginalCause(Throwable throwable) {
        Throwable cause = throwable;
        while (throwable.getCause() != null) {
            cause = throwable.getCause();
            throwable = cause;
        }
        return cause;
    }

    public static String chainedCauseMessage(Exception exception) {
        StringBuilder builder = new StringBuilder(exception.getMessage());
        for (Throwable e = exception.getCause(); e != null; e.getCause()) {
            builder.append(", caused by: ").append(e.getMessage());
        }
        return builder.toString();
    }
}
