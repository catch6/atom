/*
 * Copyright (c) 2022-2025 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.web.util;

import jakarta.servlet.ServletResponse;
import lombok.SneakyThrows;
import net.wenzuo.atom.core.util.JsonUtils;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author Catch
 * @since 2022-03-04
 */
public class ResponseUtils {

	private static final String TYPE_JSON = "application/json";


	@SneakyThrows
	public static void renderJson(ServletResponse response, Object object) {
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(TYPE_JSON);
		try (PrintWriter writer = response.getWriter()) {
			String json = JsonUtils.toJson(object);
			response.setContentLength(json.length());
			writer.print(json);
		}
	}

	@SneakyThrows
	public static void renderJson(ServletResponse response, String json) {
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(TYPE_JSON);
		try (PrintWriter writer = response.getWriter()) {
			response.setContentLength(json.length());
			writer.print(json);
		}
	}


}
