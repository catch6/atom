package cn.mindit.atom.core.util;

/**
 * @author Catch
 * @since 2023-08-08
 */
public class BusinessException extends RuntimeException implements ResultProvider {

    public static final int DEFAULT_CODE = 400;
    public static final String DEFAULT_MESSAGE = "请求数据错误";

    private final int code;
    private final String message;

    public BusinessException() {
        this(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    public BusinessException(String message) {
        this(DEFAULT_CODE, message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ResultProvider provider) {
        this(provider.getCode(), provider.getMessage());
    }

    public BusinessException(Throwable cause) {
        this(DEFAULT_CODE, DEFAULT_MESSAGE, cause);
    }

    public BusinessException(String message, Throwable cause) {
        this(DEFAULT_CODE, message, cause);
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ResultProvider provider, Throwable cause) {
        this(provider.getCode(), provider.getMessage(), cause);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
