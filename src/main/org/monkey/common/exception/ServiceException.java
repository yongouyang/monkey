package org.monkey.common.exception;

import com.google.common.base.Function;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang.StringUtils.join;
import static org.monkey.common.utils.CollectionUtils.transformNow;

public class ServiceException extends CommonRuntimeException implements HasMultipleCauses{

    private final List<Throwable> causes = newArrayList();

    public ServiceException(String message, List<Exception> causes) {
        super(extractMessage(message, causes));
        this.causes.addAll(causes);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    private static String extractMessage(String message, List<Exception> causes) {
        return String.format("%s. Failure messages [%s]", message, extractFailureMessage(causes));
    }

    private static String extractFailureMessage(List<Exception> causes) {
        List<String> errorMessages = transformNow(causes, new Function<Exception, String>() {
            @Override
            public String apply(Exception e) {
                return e.getMessage();
            }
        });
        return join(errorMessages, ", ");
    }

    public List<Throwable> getCauses() {
        return causes;
    }
}
