/*
 * Copyright (c) 2022-2023 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.web.core;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Catch
 * @since 2023-06-06
 */
public class CachedResponseWrapper extends HttpServletResponseWrapper {

    private final FastByteArrayOutputStream content = new FastByteArrayOutputStream(1024);

    @Nullable
    private ServletOutputStream outputStream;

    @Nullable
    private PrintWriter writer;

    @Nullable
    private Integer contentLength;

    public CachedResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void sendError(int sc) throws IOException {
        copyBodyToResponse(false);
        try {
            super.sendError(sc);
        } catch (IllegalStateException ex) {
            // Possibly on Tomcat when called too late: fall back to silent setStatus
            super.setStatus(sc);
        }
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        copyBodyToResponse(false);
        try {
            super.sendError(sc, msg);
        } catch (IllegalStateException ex) {
            // Possibly on Tomcat when called too late: fall back to silent setStatus
            super.setStatus(sc);
        }
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        copyBodyToResponse(false);
        super.sendRedirect(location);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (this.outputStream == null) {
            this.outputStream = new ResponseServletOutputStream(getResponse().getOutputStream());
        }
        return this.outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (this.writer == null) {
            String characterEncoding = getCharacterEncoding();
            this.writer = (characterEncoding != null ? new ResponsePrintWriter(characterEncoding) :
                new ResponsePrintWriter(StandardCharsets.UTF_8.name()));
        }
        return this.writer;
    }

    /**
     * This method neither flushes content to the client nor commits the underlying
     * response, since the content has not yet been copied to the response.
     * <p>Invoke {@link #copyBodyToResponse()} to copy the cached body content to
     * the wrapped response object and flush its buffer.
     *
     * @see jakarta.servlet.ServletResponseWrapper#flushBuffer()
     */
    @Override
    public void flushBuffer() {
        // no-op
    }

    @Override
    public void setContentLength(int len) {
        if (len > this.content.size()) {
            this.content.resize(len);
        }
        this.contentLength = len;
    }

    @Override
    public void setContentLengthLong(long len) {
        setContentLength(toContentLengthInt(len));
    }

    private int toContentLengthInt(long contentLength) {
        if (contentLength > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Content-Length exceeds ContentCachingResponseWrapper's maximum (" +
                Integer.MAX_VALUE + "): " + contentLength);
        }
        return (int) contentLength;
    }

    @Override
    public boolean containsHeader(String name) {
        if (this.contentLength != null && HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name)) {
            return true;
        } else {
            return super.containsHeader(name);
        }
    }

    @Override
    public void setHeader(String name, String value) {
        if (HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name)) {
            if (value != null) {
                this.contentLength = toContentLengthInt(Long.parseLong(value));
            } else {
                this.contentLength = null;
                super.setHeader(name, null);
            }
        } else {
            super.setHeader(name, value);
        }
    }

    @Override
    public void addHeader(String name, String value) {
        if (HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name)) {
            this.contentLength = toContentLengthInt(Long.parseLong(value));
        } else {
            super.addHeader(name, value);
        }
    }

    @Override
    public void setIntHeader(String name, int value) {
        if (HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name)) {
            this.contentLength = value;
        } else {
            super.setIntHeader(name, value);
        }
    }

    @Override
    public void addIntHeader(String name, int value) {
        if (HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name)) {
            this.contentLength = value;
        } else {
            super.addIntHeader(name, value);
        }
    }

    @Override
    @Nullable
    public String getHeader(String name) {
        if (this.contentLength != null && HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name)) {
            return this.contentLength.toString();
        } else {
            return super.getHeader(name);
        }
    }

    @Override
    public Collection<String> getHeaders(String name) {
        if (this.contentLength != null && HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name)) {
            return Collections.singleton(this.contentLength.toString());
        } else {
            return super.getHeaders(name);
        }
    }

    @Override
    public Collection<String> getHeaderNames() {
        Collection<String> headerNames = super.getHeaderNames();
        if (this.contentLength != null) {
            Set<String> result = new LinkedHashSet<>(headerNames);
            result.add(HttpHeaders.CONTENT_LENGTH);
            return result;
        } else {
            return headerNames;
        }
    }

    @Override
    public void setBufferSize(int size) {
        if (size > this.content.size()) {
            this.content.resize(size);
        }
    }

    @Override
    public void resetBuffer() {
        this.content.reset();
    }

    @Override
    public void reset() {
        super.reset();
        this.content.reset();
    }

    /**
     * Return the cached response content as a byte array.
     */
    public byte[] getContentAsByteArray() {
        return this.content.toByteArray();
    }

    /**
     * Return an {@link InputStream} to the cached content.
     *
     * @since 4.2
     */
    public InputStream getContentInputStream() {
        return this.content.getInputStream();
    }

    /**
     * Return the current size of the cached content.
     *
     * @since 4.2
     */
    public int getContentSize() {
        return this.content.size();
    }

    /**
     * Copy the complete cached body content to the response.
     *
     * @since 4.2
     */
    public void copyBodyToResponse() throws IOException {
        copyBodyToResponse(true);
    }

    /**
     * Copy the cached body content to the response.
     *
     * @param complete whether to set a corresponding content length
     *                 for the complete cached body content
     * @since 4.2
     */
    protected void copyBodyToResponse(boolean complete) throws IOException {
        if (this.content.size() > 0) {
            HttpServletResponse rawResponse = (HttpServletResponse) getResponse();
            if (!rawResponse.isCommitted()) {
                if (complete || this.contentLength != null) {
                    if (rawResponse.getHeader(HttpHeaders.TRANSFER_ENCODING) == null) {
                        rawResponse.setContentLength(complete ? this.content.size() : this.contentLength);
                    }
                    this.contentLength = null;
                }
            }
            this.content.writeTo(rawResponse.getOutputStream());
            this.content.reset();
            if (complete) {
                super.flushBuffer();
            }
        }
    }

    private class ResponseServletOutputStream extends ServletOutputStream {

        private final ServletOutputStream os;

        public ResponseServletOutputStream(ServletOutputStream os) {
            this.os = os;
        }

        @Override
        public void write(int b) throws IOException {
            content.write(b);
        }

        @Override
        public void write(@NonNull byte[] b, int off, int len) throws IOException {
            content.write(b, off, len);
        }

        @Override
        public boolean isReady() {
            return this.os.isReady();
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            this.os.setWriteListener(writeListener);
        }

    }

    private class ResponsePrintWriter extends PrintWriter {

        public ResponsePrintWriter(String characterEncoding) throws UnsupportedEncodingException {
            super(new OutputStreamWriter(content, characterEncoding));
        }

        @Override
        public void write(@NonNull char[] buf, int off, int len) {
            super.write(buf, off, len);
            super.flush();
        }

        @Override
        public void write(@NonNull String s, int off, int len) {
            super.write(s, off, len);
            super.flush();
        }

        @Override
        public void write(int c) {
            super.write(c);
            super.flush();
        }

    }

}
