package cn.mindit.atom.feign.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Catch
 * @since 2021-06-29
 */
@Data
@ConfigurationProperties(prefix = "atom.feign")
public class FeignProperties {

    /**
     * 是否启用 Feign 模块功能
     */
    private Boolean enabled = true;
    /**
     * 是否启用 Feign 请求响应日志记录
     */
    private Boolean logging = true;
    /**
     * 是否启用 Feign 第三方响应结果异常处理
     */
    private Boolean exceptionHandler = true;
    /**
     * 是否启用Feign的解码器, 解码响应结果,针对小于 400 的状态码抛出异常
     */
    private Boolean decode = true;

}
