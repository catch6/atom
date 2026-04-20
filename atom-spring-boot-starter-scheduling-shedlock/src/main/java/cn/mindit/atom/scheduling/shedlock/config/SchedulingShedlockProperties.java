package cn.mindit.atom.scheduling.shedlock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Data
@ConfigurationProperties(prefix = "atom.scheduling.shedlock")
public class SchedulingShedlockProperties {

    /**
     * 是否启用 scheduling shedlock 模块
     */
    private Boolean enabled = true;

}
