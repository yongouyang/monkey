package org.monkey.server.servlet

import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.mortbay.jetty.HttpHeaders


class ResponseDecorator {

    private static final long TEN_YEARS_IN_SEC = 315360000L
    HttpServletResponse response

    private ResponseDecorator(HttpServletResponse response) {
        this.response = response
    }

    static ResponseDecorator responseDecorator(HttpServletResponse response) {
        return new ResponseDecorator(response)
    }

    static ResponseDecorator responseDecorator(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseDecorator(response)
    }

    public ResponseDecorator withDefaultResponseHeaders() {
        return withUnCachableResponse().withOpenAccessControl()
    }

    public ResponseDecorator withUnCachableResponse() {
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0, public")
        response.setHeader(HttpHeaders.PRAGMA, "no-cache")
        response.setHeader(HttpHeaders.EXPIRES, "-1")
        return this
    }

    public ResponseDecorator withOpenAccessControl() {
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Expose-Headers", HttpHeaders.ETAG)
        return this
    }

    public ResponseDecorator withCachableResponse() {
        def now = System.currentTimeMillis()
        response.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=${TEN_YEARS_IN_SEC}, public")
        response.setDateHeader(HttpHeaders.EXPIRES, now + (1000 * TEN_YEARS_IN_SEC))
        response.setDateHeader(HttpHeaders.DATE, now)
        return this
    }


}
