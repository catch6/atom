package cn.mindit.atom.web.util;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.fesod.sheet.ExcelWriter;
import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.context.AnalysisContext;
import org.apache.fesod.sheet.event.AnalysisEventListener;
import org.apache.fesod.sheet.read.listener.ReadListener;
import org.apache.fesod.sheet.write.handler.CellWriteHandler;
import org.apache.fesod.sheet.write.handler.context.CellWriteHandlerContext;
import org.apache.fesod.sheet.write.metadata.WriteSheet;
import org.apache.fesod.sheet.write.metadata.fill.FillConfig;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.http.HttpHeaders;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Excel 工具类，基于 FesodSheet 进行封装，提供简化的读写和模板填充操作。
 * <p>
 * 主要简化了 FesodSheet 的链式调用，提供更直观的静态方法。
 *
 * @author Catch
 * @since 2025-03-02
 */
@SuppressWarnings("rawtypes")
public abstract class ExcelUtils {

    /**
     * 设置 Excel 文件下载的响应头。
     * <p>
     * 包括设置内容类型、字符编码以及文件名（处理中文乱码）。
     *
     * @param response HttpServletResponse 对象，用于设置响应头。
     * @param filename 导出文件名，不包含后缀名，如 "电费统计报表"。如果为空，默认为 "表格"。
     */
    public static void setDownloadFilename(HttpServletResponse response, String filename) {
        filename = (StrUtil.isEmpty(filename) ? "表格" : filename) + ".xlsx";
        filename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename + ";filename*=UTF-8''" + filename);
    }

    public static <T> List<T> read(File file, Class<T> head) {
        return FesodSheet.read(file, head, null).sheet().doReadSync();
    }

    public static <T> List<T> read(File file, Class<T> head, String sheetName) {
        return FesodSheet.read(file, head, null).sheet(sheetName).doReadSync();
    }

    public static <T> List<T> read(File file, Class<T> head, Integer headRowNumber) {
        return FesodSheet.read(file, head, null).headRowNumber(headRowNumber).sheet().doReadSync();
    }

    public static <T> List<T> read(File file, Class<T> head, Integer headRowNumber, Integer sheetNo) {
        return FesodSheet.read(file, head, null).headRowNumber(headRowNumber).sheet(sheetNo).doReadSync();
    }

    public static <T> List<T> read(File file, Class<T> head, Integer headRowNumber, String sheetName) {
        return FesodSheet.read(file, head, null).headRowNumber(headRowNumber).sheet(sheetName).doReadSync();
    }

    public static <T> void read(File file, Class<T> head, ReadListener readListener) {
        FesodSheet.read(file, head, readListener).sheet().doRead();
    }

    public static <T> void read(File file, Class<T> head, ReadListener readListener, String sheetName) {
        FesodSheet.read(file, head, readListener).sheet(sheetName).doRead();
    }

    public static <T> void read(File file, Class<T> head, ReadListener readListener, Integer headRowNumber) {
        FesodSheet.read(file, head, readListener).headRowNumber(headRowNumber).sheet().doRead();
    }

    public static <T> void read(File file, Class<T> head, ReadListener readListener, Integer headRowNumber, Integer sheetNo) {
        FesodSheet.read(file, head, readListener).headRowNumber(headRowNumber).sheet(sheetNo).doRead();
    }

    public static <T> void read(File file, Class<T> head, ReadListener readListener, Integer headRowNumber, String sheetName) {
        FesodSheet.read(file, head, readListener).headRowNumber(headRowNumber).sheet(sheetName).doRead();
    }

    public static <T> void read(File file, Class<T> head, BiConsumer<T, AnalysisContext> consumer) {
        FesodSheet.read(file, head, new ConsumerReadListener<T>(consumer)).sheet().doRead();
    }

    public static <T> void read(File file, Class<T> head, BiConsumer<T, AnalysisContext> consumer, String sheetName) {
        FesodSheet.read(file, head, new ConsumerReadListener<T>(consumer)).sheet(sheetName).doRead();
    }

    public static <T> void read(File file, Class<T> head, BiConsumer<T, AnalysisContext> consumer, Integer headRowNumber) {
        FesodSheet.read(file, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet().doRead();
    }

    public static <T> void read(File file, Class<T> head, BiConsumer<T, AnalysisContext> consumer, Integer headRowNumber, Integer sheetNo) {
        FesodSheet.read(file, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet(sheetNo).doRead();
    }

    public static <T> void read(File file, Class<T> head, BiConsumer<T, AnalysisContext> consumer, Integer headRowNumber, String sheetName) {
        FesodSheet.read(file, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet(sheetName).doRead();
    }

    public static <T> void read(File file, Class<T> head, Consumer<T> consumer) {
        FesodSheet.read(file, head, new ConsumerReadListener<T>(consumer)).sheet().doRead();
    }

    public static <T> void read(File file, Class<T> head, Consumer<T> consumer, String sheetName) {
        FesodSheet.read(file, head, new ConsumerReadListener<T>(consumer)).sheet(sheetName).doRead();
    }

    public static <T> void read(File file, Class<T> head, Consumer<T> consumer, Integer headRowNumber) {
        FesodSheet.read(file, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet().doRead();
    }

    public static <T> void read(File file, Class<T> head, Consumer<T> consumer, Integer headRowNumber, Integer sheetNo) {
        FesodSheet.read(file, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet(sheetNo).doRead();
    }

    public static <T> void read(File file, Class<T> head, Consumer<T> consumer, Integer headRowNumber, String sheetName) {
        FesodSheet.read(file, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet(sheetName).doRead();
    }

    public static <T> List<T> read(InputStream inputStream, Class<T> head) {
        return FesodSheet.read(inputStream, head, null).sheet().doReadSync();
    }

    public static <T> List<T> read(InputStream inputStream, Class<T> head, String sheetName) {
        return FesodSheet.read(inputStream, head, null).sheet(sheetName).doReadSync();
    }

    public static <T> List<T> read(InputStream inputStream, Class<T> head, Integer headRowNumber) {
        return FesodSheet.read(inputStream, head, null).headRowNumber(headRowNumber).sheet().doReadSync();
    }

    public static <T> List<T> read(InputStream inputStream, Class<T> head, Integer headRowNumber, Integer sheetNo) {
        return FesodSheet.read(inputStream, head, null).headRowNumber(headRowNumber).sheet(sheetNo).doReadSync();
    }

    public static <T> List<T> read(InputStream inputStream, Class<T> head, Integer headRowNumber, String sheetName) {
        return FesodSheet.read(inputStream, head, null).headRowNumber(headRowNumber).sheet(sheetName).doReadSync();
    }

    public static <T> void read(InputStream inputStream, Class<T> head, ReadListener readListener) {
        FesodSheet.read(inputStream, head, readListener).sheet().doRead();
    }

    public static <T> void read(InputStream inputStream, Class<T> head, ReadListener readListener, String sheetName) {
        FesodSheet.read(inputStream, head, readListener).sheet(sheetName).doRead();
    }

    public static <T> void read(InputStream inputStream, Class<T> head, ReadListener readListener, Integer headRowNumber) {
        FesodSheet.read(inputStream, head, readListener).headRowNumber(headRowNumber).sheet().doRead();
    }

    public static <T> void read(InputStream inputStream, Class<T> head, ReadListener readListener, Integer headRowNumber, Integer sheetNo) {
        FesodSheet.read(inputStream, head, readListener).headRowNumber(headRowNumber).sheet(sheetNo).doRead();
    }

    public static <T> void read(InputStream inputStream, Class<T> head, ReadListener readListener, Integer headRowNumber, String sheetName) {
        FesodSheet.read(inputStream, head, readListener).headRowNumber(headRowNumber).sheet(sheetName).doRead();
    }

    public static <T> void read(InputStream inputStream, Class<T> head, BiConsumer<T, AnalysisContext> consumer) {
        FesodSheet.read(inputStream, head, new ConsumerReadListener<T>(consumer)).sheet().doRead();
    }

    public static <T> void read(InputStream inputStream, Class<T> head, BiConsumer<T, AnalysisContext> consumer, String sheetName) {
        FesodSheet.read(inputStream, head, new ConsumerReadListener<T>(consumer)).sheet(sheetName).doRead();
    }

    public static <T> void read(InputStream inputStream, Class<T> head, BiConsumer<T, AnalysisContext> consumer, Integer headRowNumber) {
        FesodSheet.read(inputStream, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet().doRead();
    }

    public static <T> void read(InputStream inputStream, Class<T> head, BiConsumer<T, AnalysisContext> consumer, Integer headRowNumber, Integer sheetNo) {
        FesodSheet.read(inputStream, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet(sheetNo).doRead();
    }

    public static <T> void read(InputStream inputStream, Class<T> head, BiConsumer<T, AnalysisContext> consumer, Integer headRowNumber, String sheetName) {
        FesodSheet.read(inputStream, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet(sheetName).doRead();
    }

    public static <T> void read(InputStream inputStream, Class<T> head, Consumer<T> consumer) {
        FesodSheet.read(inputStream, head, new ConsumerReadListener<T>(consumer)).sheet().doRead();
    }

    public static <T> void read(InputStream inputStream, Class<T> head, Consumer<T> consumer, String sheetName) {
        FesodSheet.read(inputStream, head, new ConsumerReadListener<T>(consumer)).sheet(sheetName).doRead();
    }

    public static <T> void read(InputStream inputStream, Class<T> head, Consumer<T> consumer, Integer headRowNumber) {
        FesodSheet.read(inputStream, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet().doRead();
    }

    public static <T> void read(InputStream inputStream, Class<T> head, Consumer<T> consumer, Integer headRowNumber, Integer sheetNo) {
        FesodSheet.read(inputStream, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet(sheetNo).doRead();
    }

    public static <T> void read(InputStream inputStream, Class<T> head, Consumer<T> consumer, Integer headRowNumber, String sheetName) {
        FesodSheet.read(inputStream, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet(sheetName).doRead();
    }

    public static <T> List<T> read(String path, Class<T> head) {
        return FesodSheet.read(path, head, null).sheet().doReadSync();
    }

    public static <T> List<T> read(String path, Class<T> head, String sheetName) {
        return FesodSheet.read(path, head, null).sheet(sheetName).doReadSync();
    }

    public static <T> List<T> read(String path, Class<T> head, Integer headRowNumber) {
        return FesodSheet.read(path, head, null).headRowNumber(headRowNumber).sheet().doReadSync();
    }

    public static <T> List<T> read(String path, Class<T> head, Integer headRowNumber, Integer sheetNo) {
        return FesodSheet.read(path, head, null).headRowNumber(headRowNumber).sheet(sheetNo).doReadSync();
    }

    public static <T> List<T> read(String path, Class<T> head, Integer headRowNumber, String sheetName) {
        return FesodSheet.read(path, head, null).headRowNumber(headRowNumber).sheet(sheetName).doReadSync();
    }

    public static <T> void read(String path, Class<T> head, ReadListener readListener) {
        FesodSheet.read(path, head, readListener).sheet().doRead();
    }

    public static <T> void read(String path, Class<T> head, ReadListener readListener, String sheetName) {
        FesodSheet.read(path, head, readListener).sheet(sheetName).doRead();
    }

    public static <T> void read(String path, Class<T> head, ReadListener readListener, Integer headRowNumber) {
        FesodSheet.read(path, head, readListener).headRowNumber(headRowNumber).sheet().doRead();
    }

    public static <T> void read(String path, Class<T> head, ReadListener readListener, Integer headRowNumber, Integer sheetNo) {
        FesodSheet.read(path, head, readListener).headRowNumber(headRowNumber).sheet(sheetNo).doRead();
    }

    public static <T> void read(String path, Class<T> head, ReadListener readListener, Integer headRowNumber, String sheetName) {
        FesodSheet.read(path, head, readListener).headRowNumber(headRowNumber).sheet(sheetName).doRead();
    }

    public static <T> void read(String path, Class<T> head, BiConsumer<T, AnalysisContext> consumer) {
        FesodSheet.read(path, head, new ConsumerReadListener<T>(consumer)).sheet().doRead();
    }

    public static <T> void read(String path, Class<T> head, BiConsumer<T, AnalysisContext> consumer, String sheetName) {
        FesodSheet.read(path, head, new ConsumerReadListener<T>(consumer)).sheet(sheetName).doRead();
    }

    public static <T> void read(String path, Class<T> head, BiConsumer<T, AnalysisContext> consumer, Integer headRowNumber) {
        FesodSheet.read(path, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet().doRead();
    }

    public static <T> void read(String path, Class<T> head, BiConsumer<T, AnalysisContext> consumer, Integer headRowNumber, Integer sheetNo) {
        FesodSheet.read(path, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet(sheetNo).doRead();
    }

    public static <T> void read(String path, Class<T> head, BiConsumer<T, AnalysisContext> consumer, Integer headRowNumber, String sheetName) {
        FesodSheet.read(path, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet(sheetName).doRead();
    }

    public static <T> void read(String path, Class<T> head, Consumer<T> consumer) {
        FesodSheet.read(path, head, new ConsumerReadListener<T>(consumer)).sheet().doRead();
    }

    public static <T> void read(String path, Class<T> head, Consumer<T> consumer, String sheetName) {
        FesodSheet.read(path, head, new ConsumerReadListener<T>(consumer)).sheet(sheetName).doRead();
    }

    public static <T> void read(String path, Class<T> head, Consumer<T> consumer, Integer headRowNumber) {
        FesodSheet.read(path, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet().doRead();
    }

    public static <T> void read(String path, Class<T> head, Consumer<T> consumer, Integer headRowNumber, Integer sheetNo) {
        FesodSheet.read(path, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet(sheetNo).doRead();
    }

    public static <T> void read(String path, Class<T> head, Consumer<T> consumer, Integer headRowNumber, String sheetName) {
        FesodSheet.read(path, head, new ConsumerReadListener<T>(consumer)).headRowNumber(headRowNumber).sheet(sheetName).doRead();
    }

    public static void write(File file, Collection<?> data) {
        FesodSheet.write(file).sheet().registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(File file, Collection<?> data, Integer sheetNo) {
        FesodSheet.write(file).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(File file, Collection<?> data, String sheetName) {
        FesodSheet.write(file).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(File file, List<List<String>> head, Collection<?> data) {
        FesodSheet.write(file).head(head).sheet().registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(File file, List<List<String>> head, Collection<?> data, Integer sheetNo) {
        FesodSheet.write(file).head(head).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(File file, List<List<String>> head, Collection<?> data, String sheetName) {
        FesodSheet.write(file).head(head).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(File file, Class<?> head, Collection<?> data) {
        FesodSheet.write(file).head(head).sheet().registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(File file, Class<?> head, Collection<?> data, Integer sheetNo) {
        FesodSheet.write(file).head(head).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(File file, Class<?> head, Collection<?> data, String sheetName) {
        FesodSheet.write(file).head(head).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(File file, Supplier<Collection<?>> supplier) {
        FesodSheet.write(file).sheet().registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(File file, Supplier<Collection<?>> supplier, Integer sheetNo) {
        FesodSheet.write(file).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(File file, Supplier<Collection<?>> supplier, String sheetName) {
        FesodSheet.write(file).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(File file, List<List<String>> head, Supplier<Collection<?>> supplier) {
        FesodSheet.write(file).head(head).sheet().registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(File file, List<List<String>> head, Supplier<Collection<?>> supplier, Integer sheetNo) {
        FesodSheet.write(file).head(head).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(File file, List<List<String>> head, Supplier<Collection<?>> supplier, String sheetName) {
        FesodSheet.write(file).head(head).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(File file, Class<?> head, Supplier<Collection<?>> supplier) {
        FesodSheet.write(file).head(head).sheet().registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(File file, Class<?> head, Supplier<Collection<?>> supplier, Integer sheetNo) {
        FesodSheet.write(file).head(head).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(File file, Class<?> head, Supplier<Collection<?>> supplier, String sheetName) {
        FesodSheet.write(file).head(head).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(OutputStream outputStream, Collection<?> data) {
        FesodSheet.write(outputStream).sheet().registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(OutputStream outputStream, Collection<?> data, Integer sheetNo) {
        FesodSheet.write(outputStream).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(OutputStream outputStream, Collection<?> data, String sheetName) {
        FesodSheet.write(outputStream).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(OutputStream outputStream, List<List<String>> head, Collection<?> data) {
        FesodSheet.write(outputStream).head(head).sheet().registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(OutputStream outputStream, List<List<String>> head, Collection<?> data, Integer sheetNo) {
        FesodSheet.write(outputStream).head(head).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(OutputStream outputStream, List<List<String>> head, Collection<?> data, String sheetName) {
        FesodSheet.write(outputStream).head(head).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(OutputStream outputStream, Class<?> head, Collection<?> data) {
        FesodSheet.write(outputStream).head(head).sheet().registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(OutputStream outputStream, Class<?> head, Collection<?> data, Integer sheetNo) {
        FesodSheet.write(outputStream).head(head).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(OutputStream outputStream, Class<?> head, Collection<?> data, String sheetName) {
        FesodSheet.write(outputStream).head(head).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(OutputStream outputStream, Supplier<Collection<?>> supplier) {
        FesodSheet.write(outputStream).sheet().registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(OutputStream outputStream, Supplier<Collection<?>> supplier, Integer sheetNo) {
        FesodSheet.write(outputStream).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(OutputStream outputStream, Supplier<Collection<?>> supplier, String sheetName) {
        FesodSheet.write(outputStream).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(OutputStream outputStream, List<List<String>> head, Supplier<Collection<?>> supplier) {
        FesodSheet.write(outputStream).head(head).sheet().registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(OutputStream outputStream, List<List<String>> head, Supplier<Collection<?>> supplier, Integer sheetNo) {
        FesodSheet.write(outputStream).head(head).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(OutputStream outputStream, List<List<String>> head, Supplier<Collection<?>> supplier, String sheetName) {
        FesodSheet.write(outputStream).head(head).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(OutputStream outputStream, Class<?> head, Supplier<Collection<?>> supplier) {
        FesodSheet.write(outputStream).head(head).sheet().registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(OutputStream outputStream, Class<?> head, Supplier<Collection<?>> supplier, Integer sheetNo) {
        FesodSheet.write(outputStream).head(head).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(OutputStream outputStream, Class<?> head, Supplier<Collection<?>> supplier, String sheetName) {
        FesodSheet.write(outputStream).head(head).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(String path, Collection<?> data) {
        FesodSheet.write(path).sheet().registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(String path, Collection<?> data, Integer sheetNo) {
        FesodSheet.write(path).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(String path, Collection<?> data, String sheetName) {
        FesodSheet.write(path).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(String path, List<List<String>> head, Collection<?> data) {
        FesodSheet.write(path).head(head).sheet().registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(String path, List<List<String>> head, Collection<?> data, Integer sheetNo) {
        FesodSheet.write(path).head(head).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(String path, List<List<String>> head, Collection<?> data, String sheetName) {
        FesodSheet.write(path).head(head).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(String path, Class<?> head, Collection<?> data) {
        FesodSheet.write(path).head(head).sheet().registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(String path, Class<?> head, Collection<?> data, Integer sheetNo) {
        FesodSheet.write(path).head(head).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(String path, Class<?> head, Collection<?> data, String sheetName) {
        FesodSheet.write(path).head(head).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(data);
    }

    public static void write(String path, Supplier<Collection<?>> supplier) {
        FesodSheet.write(path).sheet().registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(String path, Supplier<Collection<?>> supplier, Integer sheetNo) {
        FesodSheet.write(path).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(String path, Supplier<Collection<?>> supplier, String sheetName) {
        FesodSheet.write(path).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(String path, List<List<String>> head, Supplier<Collection<?>> supplier) {
        FesodSheet.write(path).head(head).sheet().registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(String path, List<List<String>> head, Supplier<Collection<?>> supplier, Integer sheetNo) {
        FesodSheet.write(path).head(head).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(String path, List<List<String>> head, Supplier<Collection<?>> supplier, String sheetName) {
        FesodSheet.write(path).head(head).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(String path, Class<?> head, Supplier<Collection<?>> supplier) {
        FesodSheet.write(path).head(head).sheet().registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(String path, Class<?> head, Supplier<Collection<?>> supplier, Integer sheetNo) {
        FesodSheet.write(path).head(head).sheet(sheetNo).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void write(String path, Class<?> head, Supplier<Collection<?>> supplier, String sheetName) {
        FesodSheet.write(path).head(head).sheet(sheetName).registerWriteHandler(defaultCellStyle()).doWrite(supplier);
    }

    public static void fill(File file, File template, Collection<?> list) {
        FesodSheet.write(file)
                  .withTemplate(template)
                  .sheet()
                  .doFill(list);
    }

    public static void fill(File file, File template, Collection<?> list, Integer sheetNo) {
        FesodSheet.write(file)
                  .withTemplate(template)
                  .sheet(sheetNo)
                  .doFill(list);
    }

    public static void fill(File file, File template, Collection<?> list, String sheetName) {
        FesodSheet.write(file)
                  .withTemplate(template)
                  .sheet(sheetName)
                  .doFill(list);
    }

    public static void fill(File file, File template, Map<String, Object> map) {
        FesodSheet.write(file)
                  .withTemplate(template)
                  .sheet()
                  .doFill(map);
    }

    public static void fill(File file, File template, Map<String, Object> map, Integer sheetNo) {
        FesodSheet.write(file)
                  .withTemplate(template)
                  .sheet(sheetNo)
                  .doFill(map);
    }

    public static void fill(File file, File template, Map<String, Object> map, String sheetName) {
        FesodSheet.write(file)
                  .withTemplate(template)
                  .sheet(sheetName)
                  .doFill(map);
    }

    public static void fill(File file, File template, Object object) {
        FesodSheet.write(file)
                  .withTemplate(template)
                  .sheet()
                  .doFill(object);
    }

    public static void fill(File file, File template, Object object, Integer sheetNo) {
        FesodSheet.write(file)
                  .withTemplate(template)
                  .sheet(sheetNo)
                  .doFill(object);
    }

    public static void fill(File file, File template, Object object, String sheetName) {
        FesodSheet.write(file)
                  .withTemplate(template)
                  .sheet(sheetName)
                  .doFill(object);
    }

    public static void fill(File file, File template, Collection<?> list, Map<String, Object> map) {
        try (ExcelWriter writer = FesodSheet.write(file).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet().build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(map, writeSheet);
        }
    }

    public static void fill(File file, File template, Collection<?> list, Map<String, Object> map, Integer sheetNo) {
        try (ExcelWriter writer = FesodSheet.write(file).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet(sheetNo).build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(map, writeSheet);
        }
    }

    public static void fill(File file, File template, Collection<?> list, Map<String, Object> map, String sheetName) {
        try (ExcelWriter writer = FesodSheet.write(file).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet(sheetName).build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(map, writeSheet);
        }
    }

    public static void fill(File file, File template, Collection<?> list, Object object) {
        try (ExcelWriter writer = FesodSheet.write(file).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet().build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(object, writeSheet);
        }
    }

    public static void fill(File file, File template, Collection<?> list, Object object, Integer sheetNo) {
        try (ExcelWriter writer = FesodSheet.write(file).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet(sheetNo).build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(object, writeSheet);
        }
    }

    public static void fill(File file, File template, Collection<?> list, Object object, String sheetName) {
        try (ExcelWriter writer = FesodSheet.write(file).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet(sheetName).build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(object, writeSheet);
        }
    }

    public static void fill(OutputStream outputStream, File template, Collection<?> list) {
        FesodSheet.write(outputStream)
                  .withTemplate(template)
                  .sheet()
                  .doFill(list);
    }

    public static void fill(OutputStream outputStream, File template, Collection<?> list, Integer sheetNo) {
        FesodSheet.write(outputStream)
                  .withTemplate(template)
                  .sheet(sheetNo)
                  .doFill(list);
    }

    public static void fill(OutputStream outputStream, File template, Collection<?> list, String sheetName) {
        FesodSheet.write(outputStream)
                  .withTemplate(template)
                  .sheet(sheetName)
                  .doFill(list);
    }

    public static void fill(OutputStream outputStream, File template, Map<String, Object> map) {
        FesodSheet.write(outputStream)
                  .withTemplate(template)
                  .sheet()
                  .doFill(map);
    }

    public static void fill(OutputStream outputStream, File template, Map<String, Object> map, Integer sheetNo) {
        FesodSheet.write(outputStream)
                  .withTemplate(template)
                  .sheet(sheetNo)
                  .doFill(map);
    }

    public static void fill(OutputStream outputStream, File template, Map<String, Object> map, String sheetName) {
        FesodSheet.write(outputStream)
                  .withTemplate(template)
                  .sheet(sheetName)
                  .doFill(map);
    }

    public static void fill(OutputStream outputStream, File template, Object object) {
        FesodSheet.write(outputStream)
                  .withTemplate(template)
                  .sheet()
                  .doFill(object);
    }

    public static void fill(OutputStream outputStream, File template, Object object, Integer sheetNo) {
        FesodSheet.write(outputStream)
                  .withTemplate(template)
                  .sheet(sheetNo)
                  .doFill(object);
    }

    public static void fill(OutputStream outputStream, File template, Object object, String sheetName) {
        FesodSheet.write(outputStream)
                  .withTemplate(template)
                  .sheet(sheetName)
                  .doFill(object);
    }

    public static void fill(OutputStream outputStream, File template, Collection<?> list, Map<String, Object> map) {
        try (ExcelWriter writer = FesodSheet.write(outputStream).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet().build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(map, writeSheet);
        }
    }

    public static void fill(OutputStream outputStream, File template, Collection<?> list, Map<String, Object> map, Integer sheetNo) {
        try (ExcelWriter writer = FesodSheet.write(outputStream).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet(sheetNo).build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(map, writeSheet);
        }
    }

    public static void fill(OutputStream outputStream, File template, Collection<?> list, Map<String, Object> map, String sheetName) {
        try (ExcelWriter writer = FesodSheet.write(outputStream).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet(sheetName).build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(map, writeSheet);
        }
    }

    public static void fill(OutputStream outputStream, File template, Collection<?> list, Object object) {
        try (ExcelWriter writer = FesodSheet.write(outputStream).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet().build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(object, writeSheet);
        }
    }

    public static void fill(OutputStream outputStream, File template, Collection<?> list, Object object, Integer sheetNo) {
        try (ExcelWriter writer = FesodSheet.write(outputStream).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet(sheetNo).build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(object, writeSheet);
        }
    }

    public static void fill(OutputStream outputStream, File template, Collection<?> list, Object object, String sheetName) {
        try (ExcelWriter writer = FesodSheet.write(outputStream).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet(sheetName).build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(object, writeSheet);
        }
    }

    public static void fill(String path, File template, Collection<?> list) {
        FesodSheet.write(path)
                  .withTemplate(template)
                  .sheet()
                  .doFill(list);
    }

    public static void fill(String path, File template, Collection<?> list, Integer sheetNo) {
        FesodSheet.write(path)
                  .withTemplate(template)
                  .sheet(sheetNo)
                  .doFill(list);
    }

    public static void fill(String path, File template, Collection<?> list, String sheetName) {
        FesodSheet.write(path)
                  .withTemplate(template)
                  .sheet(sheetName)
                  .doFill(list);
    }

    public static void fill(String path, File template, Map<String, Object> map) {
        FesodSheet.write(path)
                  .withTemplate(template)
                  .sheet()
                  .doFill(map);
    }

    public static void fill(String path, File template, Map<String, Object> map, Integer sheetNo) {
        FesodSheet.write(path)
                  .withTemplate(template)
                  .sheet(sheetNo)
                  .doFill(map);
    }

    public static void fill(String path, File template, Map<String, Object> map, String sheetName) {
        FesodSheet.write(path)
                  .withTemplate(template)
                  .sheet(sheetName)
                  .doFill(map);
    }

    public static void fill(String path, File template, Object object) {
        FesodSheet.write(path)
                  .withTemplate(template)
                  .sheet()
                  .doFill(object);
    }

    public static void fill(String path, File template, Object object, Integer sheetNo) {
        FesodSheet.write(path)
                  .withTemplate(template)
                  .sheet(sheetNo)
                  .doFill(object);
    }

    public static void fill(String path, File template, Object object, String sheetName) {
        FesodSheet.write(path)
                  .withTemplate(template)
                  .sheet(sheetName)
                  .doFill(object);
    }

    public static void fill(String path, File template, Collection<?> list, Map<String, Object> map) {
        try (ExcelWriter writer = FesodSheet.write(path).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet().build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(map, writeSheet);
        }
    }

    public static void fill(String path, File template, Collection<?> list, Map<String, Object> map, Integer sheetNo) {
        try (ExcelWriter writer = FesodSheet.write(path).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet(sheetNo).build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(map, writeSheet);
        }
    }

    public static void fill(String path, File template, Collection<?> list, Map<String, Object> map, String sheetName) {
        try (ExcelWriter writer = FesodSheet.write(path).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet(sheetName).build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(map, writeSheet);
        }
    }

    public static void fill(String path, File template, Collection<?> list, Object object) {
        try (ExcelWriter writer = FesodSheet.write(path).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet().build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(object, writeSheet);
        }
    }

    public static void fill(String path, File template, Collection<?> list, Object object, Integer sheetNo) {
        try (ExcelWriter writer = FesodSheet.write(path).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet(sheetNo).build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(object, writeSheet);
        }
    }

    public static void fill(String path, File template, Collection<?> list, Object object, String sheetName) {
        try (ExcelWriter writer = FesodSheet.write(path).withTemplate(template).build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet(sheetName).build();
            FillConfig config = FillConfig.builder().forceNewRow(true).build();
            writer.fill(list, config, writeSheet);
            writer.fill(object, writeSheet);
        }
    }

    private static CellWriteHandler defaultCellStyle() {
        return new CellWriteHandler() {
            @Override
            public void afterCellDispose(CellWriteHandlerContext context) {
                Cell cell = context.getCell();
                Row row = context.getRow();
                Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
                Sheet sheet = context.getWriteSheetHolder().getSheet();
                Boolean isHead = context.getHead();

                // 设置列宽
                sheet.setDefaultColumnWidth(22);

                // 居中
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                // 边框样式
                XSSFColor borderColor = new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255});
                cellStyle.setTopBorderColor(borderColor);
                cellStyle.setBottomBorderColor(borderColor);
                cellStyle.setLeftBorderColor(borderColor);
                cellStyle.setRightBorderColor(borderColor);
                cellStyle.setBorderTop(BorderStyle.THIN);
                cellStyle.setBorderBottom(BorderStyle.THIN);
                cellStyle.setBorderLeft(BorderStyle.THIN);
                cellStyle.setBorderRight(BorderStyle.THIN);

                // 背景颜色
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                XSSFColor bgColor, textColor;
                XSSFFont font = (XSSFFont) workbook.createFont();
                if (isHead) { // 标题
                    row.setHeightInPoints(35);
                    bgColor = new XSSFColor(new byte[]{(byte) 72, (byte) 85, (byte) 106});
                    textColor = new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255});
                    font.setFontName("宋体");
                    font.setBold(true);
                    font.setColor(textColor);
                    font.setFontHeightInPoints((short) 14);
                } else { // 内容
                    row.setHeightInPoints(30);
                    // // 斑马线
                    // if (row.getRowNum() % 2 == 0) {
                    //     bgColor = new XSSFColor(new byte[]{(byte) 242, (byte) 245, (byte) 249});
                    // } else {
                    //     bgColor = new XSSFColor(new byte[]{(byte) 227, (byte) 232, (byte) 240});
                    // }
                    bgColor = new XSSFColor(new byte[]{(byte) 227, (byte) 232, (byte) 240});
                    textColor = new XSSFColor(new byte[]{(byte) 0, (byte) 0, (byte) 0});
                    font.setFontName("宋体");
                    font.setColor(textColor);
                    font.setFontHeightInPoints((short) 12);
                }
                cellStyle.setFillForegroundColor(bgColor);
                cellStyle.setFont(font);

                cell.setCellStyle(cellStyle);
                context.getFirstCellData().setWriteCellStyle(null);
            }
        };
    }

    private static class ConsumerReadListener<T> extends AnalysisEventListener<T> {

        private final BiConsumer<T, AnalysisContext> consumer;

        public ConsumerReadListener(BiConsumer<T, AnalysisContext> consumer) {
            this.consumer = consumer;
        }

        public ConsumerReadListener(Consumer<T> consumer) {
            this.consumer = (data, context) -> consumer.accept(data);
        }

        @Override
        public void invoke(T data, AnalysisContext context) {
            this.consumer.accept(data, context);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

    }

}
