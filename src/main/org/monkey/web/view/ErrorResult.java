package org.monkey.web.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.monkey.common.exception.ExceptionUtil;

public class ErrorResult {

    @JsonProperty private int responseCode;
    @JsonProperty private String type;
    @JsonProperty private String message;
    @JsonProperty private String stackTrace;

    public ErrorResult(int responseCode, Throwable cause) {
        this.responseCode = responseCode;
        this.type = cause.getClass().getSimpleName();
        this.message = cause.getMessage();
        this.stackTrace = ExceptionUtil.stackTrace(cause);
    }
}
