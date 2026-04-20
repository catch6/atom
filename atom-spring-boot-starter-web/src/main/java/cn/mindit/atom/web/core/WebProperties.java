package cn.mindit.atom.web.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Data
@ConfigurationProperties(prefix = "atom.web")
public class WebProperties {

    /**
     * 是否启用 web 模块
     */
    private Boolean enabled = true;

    /**
     * 是否启用全局异常处理
     */
    private Boolean exceptionHandler = true;

}
