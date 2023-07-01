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

package net.wenzuo.atom.web.utils;

import com.alibaba.excel.EasyExcel;
import lombok.SneakyThrows;
import net.wenzuo.atom.core.utils.JsonUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Catch
 * @since 2022-03-04
 */
public class ResponseUtils {

	private static final String TYPE_JSON = "application/json";
	private static final String TYPE_EXCEL = "application/vnd.ms-excel";

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
	public static void renderJson(ServletResponse response, String jsonString) {
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(TYPE_JSON);
		try (PrintWriter writer = response.getWriter()) {
			response.setContentLength(jsonString.length());
			writer.print(jsonString);
		}
	}

	@SuppressWarnings("rawtypes")
	@SneakyThrows
	public static void renderXlsx(HttpServletResponse response, String filename, Class head, Supplier<Collection> supplier) {
		filename = (filename == null ? "表格" : filename) + ".xlsx";
		filename = URLEncoder.encode(filename, StandardCharsets.UTF_8.name());
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(TYPE_EXCEL);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename + ";filename*=UTF-8''" + filename);
		Collection data = supplier.get();
		try (ServletOutputStream out = response.getOutputStream()) {
			EasyExcel.write(out, head).sheet("sheet1").doWrite(data);
		}
	}

	@SuppressWarnings("rawtypes")
	@SneakyThrows
	public static void renderXlsx(HttpServletResponse response, String filename, Class head, Collection<?> data) {
		renderXlsx(response, filename, head, () -> data);
	}

}
