package cn.mindit.atom.web.util;

import jakarta.servlet.ServletResponse;
import lombok.SneakyThrows;
import cn.mindit.atom.core.util.JsonUtils;
import org.springframework.http.MediaType;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Catch
 * @since 2022-03-04
 */
public abstract class ResponseUtils {

    @SneakyThrows
    public static void renderJson(ServletResponse response, Object object) {
        renderJson(response, JsonUtils.toJson(object));
    }

    @SneakyThrows
    public static void renderJson(ServletResponse response, String json) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        response.setContentLength(bytes.length);
        try (OutputStream out = response.getOutputStream()) {
            out.write(bytes);
        }
    }

}
