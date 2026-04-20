package cn.mindit.atom.core.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Data
@ConfigurationProperties(prefix = "atom.core")
public class CoreProperties {

    /**
     * 是否启用 core 模块
     */
    private boolean enabled = true;

    /**
     * 是否启用 trace 模块
     */
    private boolean trace = true;

    /**
     * 是否启用json
     */
    private boolean json = true;

}
