package org.monkey.web.controller;

import org.monkey.common.exception.ResourceNotFoundException;
import org.monkey.web.view.ErrorResult;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExceptionAwareController {

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ErrorResult handleException(Throwable e, HttpServletRequest request, HttpServletResponse response) {

        // Because ExceptionHandlerExceptionResolver is always the first to be invoked to handle an exception,
        // using @ResponseStatus becomes useless

        // default status code is 500 for unmatched exception
        int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

        // spring mvc exception-status code mappings
        if (e instanceof BindException) { status = HttpServletResponse.SC_BAD_REQUEST; }
        else if (e instanceof ConversionNotSupportedException) { status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR; }
        else if (e instanceof HttpMediaTypeNotAcceptableException) { status = HttpServletResponse.SC_NOT_ACCEPTABLE; }
        else if (e instanceof HttpMediaTypeNotSupportedException) { status = HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE; }
        else if (e instanceof HttpMessageNotReadableException) { status = HttpServletResponse.SC_BAD_REQUEST; }
        else if (e instanceof HttpMessageNotWritableException) { status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR; }
        else if (e instanceof HttpRequestMethodNotSupportedException) { status = HttpServletResponse.SC_METHOD_NOT_ALLOWED; }
        else if (e instanceof MethodArgumentNotValidException) { status = HttpServletResponse.SC_BAD_REQUEST; }
        else if (e instanceof MissingServletRequestParameterException) { status = HttpServletResponse.SC_BAD_REQUEST; }
        else if (e instanceof MissingServletRequestPartException) { status = HttpServletResponse.SC_BAD_REQUEST; }
        else if (e instanceof NoSuchRequestHandlingMethodException) { status = HttpServletResponse.SC_NOT_FOUND; }
        else if (e instanceof TypeMismatchException) { status = HttpServletResponse.SC_BAD_REQUEST; }

        // application specific exception--status code mappings
        else if (e instanceof ResourceNotFoundException) { status = HttpServletResponse.SC_NOT_FOUND; }

        // we might want to log the exception here

        response.setStatus(status);
        return new ErrorResult(status, e);
    }
}
