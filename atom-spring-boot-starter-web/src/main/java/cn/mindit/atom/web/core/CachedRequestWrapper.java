package cn.mindit.atom.web.core;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Slf4j
public class CachedRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] cachedBody;

    public CachedRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream inputStream = request.getInputStream();
        this.cachedBody = StreamUtils.copyToByteArray(inputStream);
    }

    public byte[] getContentAsByteArray() {
        return this.cachedBody;
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedServletInputStream(this.cachedBody);
    }

    @Override
    public BufferedReader getReader() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }

    @Override
    public String getCharacterEncoding() {
        return StandardCharsets.UTF_8.name();
    }

    private static class CachedServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream cachedInputStream;

        public CachedServletInputStream(byte[] cachedBody) {
            this.cachedInputStream = new ByteArrayInputStream(cachedBody);
        }

        @Override
        public boolean isFinished() {
            return this.cachedInputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException("ReadListener not supported");
        }

        @Override
        public int read() {
            return this.cachedInputStream.read();
        }

        @Override
        public int read(@NonNull byte[] b) throws IOException {
            return this.cachedInputStream.read(b);
        }

        @Override
        public int read(@NonNull byte[] b, int off, int len) {
            return this.cachedInputStream.read(b, off, len);
        }

        @Override
        public void close() throws IOException {
            this.cachedInputStream.close();
        }

    }

}
