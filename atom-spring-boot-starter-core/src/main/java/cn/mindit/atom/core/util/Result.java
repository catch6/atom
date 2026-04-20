package cn.mindit.atom.core.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Catch
 * @since 2023-08-08
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> implements ResultProvider {

    private static final int OK_CODE = 200;
    private static final String OK_MESSAGE = "成功";
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> ok() {
        return new Result<>(OK_CODE, OK_MESSAGE, null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(OK_CODE, OK_MESSAGE, data);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> fail(ResultProvider provider) {
        return new Result<>(provider.getCode(), provider.getMessage(), null);
    }

}
