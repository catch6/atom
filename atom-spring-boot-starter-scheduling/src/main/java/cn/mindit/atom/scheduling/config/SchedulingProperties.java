package cn.mindit.atom.scheduling.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Data
@ConfigurationProperties(prefix = "atom.scheduling")
public class SchedulingProperties {

    /**
     * 是否启用 scheduling 模块
     */
    private Boolean enabled = true;

}
