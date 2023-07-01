/*
 * Copyright (c) 2022-2023 Catch
 * [Atom] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.web.config;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.FastByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

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
	public String getCharacterEncoding() {
		return StandardCharsets.UTF_8.name();
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
			this.outputStream = new CachedServletOutputStream(getResponse().getOutputStream());
		}
		return this.outputStream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (this.writer == null) {
			this.writer = new CachedPrintWriter(StandardCharsets.UTF_8.name());
		}
		return this.writer;
	}

	@Override
	public void flushBuffer() throws IOException {
		// do not flush the underlying response as the content has not been copied to it yet
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
		if (len > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Content-Length exceeds ContentCachingResponseWrapper's maximum (" +
				Integer.MAX_VALUE + "): " + len);
		}
		int lenInt = (int) len;
		if (lenInt > this.content.size()) {
			this.content.resize(lenInt);
		}
		this.contentLength = lenInt;
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

	public int getContentSize() {
		return this.content.size();
	}

	public void copyBodyToResponse() throws IOException {
		copyBodyToResponse(true);
	}

	protected void copyBodyToResponse(boolean complete) throws IOException {
		if (this.content.size() > 0) {
			HttpServletResponse rawResponse = (HttpServletResponse) getResponse();
			if ((complete || this.contentLength != null) && !rawResponse.isCommitted()) {
				if (rawResponse.getHeader(HttpHeaders.TRANSFER_ENCODING) == null) {
					rawResponse.setContentLength(complete ? this.content.size() : this.contentLength);
				}
				this.contentLength = null;
			}
			this.content.writeTo(rawResponse.getOutputStream());
			this.content.reset();
			if (complete) {
				super.flushBuffer();
			}
		}
	}

	private class CachedServletOutputStream extends ServletOutputStream {

		private final ServletOutputStream os;

		public CachedServletOutputStream(ServletOutputStream os) {
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

	private class CachedPrintWriter extends PrintWriter {

		public CachedPrintWriter(String characterEncoding) throws UnsupportedEncodingException {
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
