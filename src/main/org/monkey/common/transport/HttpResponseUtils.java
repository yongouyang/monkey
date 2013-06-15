package org.monkey.common.transport;

import com.google.common.collect.Maps;
import org.apache.http.*;
import org.apache.http.util.EntityUtils;
import org.monkey.common.utils.GZipUtils2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpResponseUtils {

    public static String toString(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null) return "";

        Header contentEncoding = entity.getContentEncoding();
        if (contentEncoding != null) {
            HeaderElement[] headerElements = contentEncoding.getElements();
            for (HeaderElement headerElement : headerElements) {
                if ("gzip".equalsIgnoreCase(headerElement.getName())) {
                    return handleGZipResponse(entity);
                }
            }
        }

        return EntityUtils.toString(entity);
    }

    public static InputStream toStream(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null) return null;

        Header contentEncoding = entity.getContentEncoding();
        if (contentEncoding != null) {
            HeaderElement[] headerElements = contentEncoding.getElements();
            for (HeaderElement headerElement : headerElements) {
                if ("gzip".equalsIgnoreCase(headerElement.getName())) {
                    return GZipUtils2.wrap(entity.getContent());
                }
            }
        }

        return entity.getContent();
    }

    private static String handleGZipResponse(HttpEntity entity) throws IOException {
        Charset charset = responseCharset(entity);
        return GZipUtils2.decompress(entity.getContent(), charset);
    }

    private static Charset responseCharset(HttpEntity entity) {
        Header contentType = entity.getContentType();
        Charset charset = GZipUtils2.UTF8;
        if (contentType != null) {
            String value = contentType.getValue(); // sample content-type: text/plain; charset=iso-8859-1
            Pattern pattern = Pattern.compile("(.*)charset=([a-zA-Z0-9-]*?)");
            Matcher matcher = pattern.matcher(value);
            if (matcher.matches()) {
                try {
                    charset = Charset.forName(matcher.group(2));
                } catch (RuntimeException e) {
                    // ignore illegal charset, as we shall return default charset
                }
            }
        }

        return charset;
    }

    public static Map<String, String> getAllHeaders(HttpResponse response) {
        Map<String, String> headers = Maps.newHashMap();
        for (Header header : response.getAllHeaders()) {
            headers.put(header.getName(), header.getValue());
        }
        return headers;
    }

    public static int getResponseCode(HttpResponse response) {
        StatusLine statusLine = response.getStatusLine();
        if (statusLine != null) {
            return statusLine.getStatusCode();
        }
        throw new IllegalArgumentException("Status line from httpResponse is not supposed to be null!");
    }
}
