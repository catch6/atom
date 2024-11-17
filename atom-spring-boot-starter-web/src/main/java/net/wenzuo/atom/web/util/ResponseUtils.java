/*
 * Copyright (c) 2022-2024 Catch(catchlife6@163.com).
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

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import net.wenzuo.atom.core.util.JsonUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpHeaders;

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
	private static final String TYPE_XLS = "application/vnd.ms-excel";
	private static final String TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

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

	@SuppressWarnings("rawtypes")
	@SneakyThrows
	public static void renderXlsx(HttpServletResponse response, String filename, Class head, Collection<?> data) {
		renderXlsx(response, filename, head, () -> data);
	}

	@SuppressWarnings("rawtypes")
	@SneakyThrows
	public static void renderXlsx(HttpServletResponse response, String filename, Class head, Supplier<Collection> supplier) {
		setXlsxHeader(response, filename);
		Collection data = supplier.get();
		try (ServletOutputStream out = response.getOutputStream()) {
			EasyExcel.write(out)
					 .sheet("sheet1")
					 .head(head)
					 .registerWriteHandler(horizontalCellStyleStrategy())
					 .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 32, (short) 26)) // 头行高32，内容行高26
					 .registerWriteHandler(new SimpleColumnWidthStyleStrategy(20)) // 列宽20
					 .doWrite(data);
			EasyExcel.write(out, head).sheet("sheet1").doWrite(data);
		}
	}

	private static void setXlsxHeader(HttpServletResponse response, String filename) {
		filename = (StrUtil.isEmpty(filename) ? "表格" : filename) + ".xlsx";
		filename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(TYPE_XLSX);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename + ";filename*=UTF-8''" + filename);
	}

	public static HorizontalCellStyleStrategy horizontalCellStyleStrategy() {
		WriteCellStyle headCellStyle = new WriteCellStyle();
		headCellStyle.setWrapped(true);
		headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
		headCellStyle.setLocked(true);
		headCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
		headCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headCellStyle.setBorderTop(BorderStyle.THIN);
		headCellStyle.setBorderBottom(BorderStyle.THIN);
		headCellStyle.setBorderLeft(BorderStyle.THIN);
		headCellStyle.setBorderRight(BorderStyle.THIN);
		WriteFont headFont = new WriteFont();
		headFont.setFontHeightInPoints((short) 16);
		headFont.setBold(true);
		headCellStyle.setWriteFont(headFont);

		WriteCellStyle contentCellStyle = new WriteCellStyle();
		contentCellStyle.setBorderTop(BorderStyle.THIN);
		contentCellStyle.setBorderBottom(BorderStyle.THIN);
		contentCellStyle.setBorderLeft(BorderStyle.THIN);
		contentCellStyle.setBorderRight(BorderStyle.THIN);
		WriteFont contentFont = new WriteFont();
		contentFont.setFontHeightInPoints((short) 12);
		contentCellStyle.setWriteFont(contentFont);

		return new HorizontalCellStyleStrategy(headCellStyle, contentCellStyle);
	}

}
