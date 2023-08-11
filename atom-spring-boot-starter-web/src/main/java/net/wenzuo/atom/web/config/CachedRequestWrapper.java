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

package net.wenzuo.atom.web.config;

import org.springframework.lang.NonNull;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author Catch
 * @since 2023-06-06
 */
public class CachedRequestWrapper extends HttpServletRequestWrapper {

	private final byte[] cachedBody;

	public CachedRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		InputStream inputStream = request.getInputStream();
		this.cachedBody = StreamUtils.copyToByteArray(inputStream);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new CachedServletInputStream(this.cachedBody);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
		return new BufferedReader(new InputStreamReader(byteArrayInputStream));
	}

	@NonNull
	@Override
	public String getCharacterEncoding() {
		return StandardCharsets.UTF_8.name();
	}

	private static class CachedServletInputStream extends ServletInputStream {

		private final InputStream cachedInputStream;

		public CachedServletInputStream(byte[] cachedBody) {
			this.cachedInputStream = new ByteArrayInputStream(cachedBody);
		}

		@Override
		public boolean isFinished() {
			try {
				return this.cachedInputStream.available() == 0;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int read() throws IOException {
			return this.cachedInputStream.read();
		}

	}

}
