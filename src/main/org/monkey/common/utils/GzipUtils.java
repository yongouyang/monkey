package org.monkey.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.google.common.io.Closeables.closeQuietly;


public class GZipUtils {

    public static final Charset UTF8 = Charset.forName("UTF-8");

    public static byte[] compress(String input) {
        return compress(input, UTF8);
    }

    public static byte[] compress(String input, Charset charset) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(buffer);
            gzip.write(input.getBytes(charset));
        } catch (IOException e) {
            throw new RuntimeException("Something unexpected happened", e);
        } finally {
            closeQuietly(gzip);
        }

        return buffer.toByteArray();
    }

    public static String decompress(byte[] data) {
        return decompress(data, UTF8);
    }

    public static String decompress(byte[] data, Charset charset) {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        return decompress(in, charset);
    }

    public static String decompress(InputStream in, Charset charset) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            InputStream gzip = wrap(in);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = gzip.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new RuntimeException("Something unexpected happened", e);
        }

        return new String(out.toByteArray(), charset);
    }

    public static InputStream wrap(InputStream in) {
        try {
            return new GZIPInputStream(in);
        } catch (IOException e) {
            throw new RuntimeException("Something unexpected happened", e);
        }
    }

}
