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

import cn.hutool.core.util.StrUtil;
import cn.idev.excel.EasyExcel;
import cn.idev.excel.write.metadata.style.WriteCellStyle;
import cn.idev.excel.write.metadata.style.WriteFont;
import cn.idev.excel.write.style.HorizontalCellStyleStrategy;
import cn.idev.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import cn.idev.excel.write.style.row.SimpleRowHeightStyleStrategy;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Catch
 * @since 2025-03-02
 */
public abstract class ExcelUtils {

    private static final String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private static final Short HEAD_ROW_HEIGHT = 32;
    private static final Short CONTENT_ROW_HEIGHT = 26;
    private static final Integer COLUMN_WIDTH = 20;

    @SuppressWarnings("rawtypes")
    public static void renderXlsx(HttpServletResponse response, String filename, Class head, Collection<?> data) {
        renderXlsx(response, filename, head, () -> data);
    }

    @SuppressWarnings("rawtypes")
    public static void renderXlsx(HttpServletResponse response, String filename, Class head, Supplier<Collection> supplier) {
        setXlsxHeader(response, filename);
        Collection data = supplier.get();
        try (ServletOutputStream out = response.getOutputStream()) {
            EasyExcel.write(out)
                     .sheet("sheet1")
                     .head(head)
                     .registerWriteHandler(horizontalCellStyleStrategy())
                     .registerWriteHandler(new SimpleRowHeightStyleStrategy(HEAD_ROW_HEIGHT, CONTENT_ROW_HEIGHT))
                     .registerWriteHandler(new SimpleColumnWidthStyleStrategy(COLUMN_WIDTH))
                     .doWrite(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void renderXlsx(HttpServletResponse response, String filename, List<List<String>> header, Collection<?> data) {
        setXlsxHeader(response, filename);
        try (ServletOutputStream out = response.getOutputStream()) {
            EasyExcel.write(out)
                     .sheet("sheet1")
                     .head(header)
                     .registerWriteHandler(horizontalCellStyleStrategy())
                     .registerWriteHandler(new SimpleRowHeightStyleStrategy(HEAD_ROW_HEIGHT, CONTENT_ROW_HEIGHT))
                     .registerWriteHandler(new SimpleColumnWidthStyleStrategy(COLUMN_WIDTH))
                     .doWrite(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setXlsxHeader(HttpServletResponse response, String filename) {
        filename = (StrUtil.isEmpty(filename) ? "表格" : filename) + ".xlsx";
        filename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(CONTENT_TYPE_XLSX);
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
