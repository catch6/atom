package cn.mindit.atom.consul.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Catch
 * @since 2021-06-29
 */
@Data
@ConfigurationProperties(prefix = "atom.consul")
public class ConsulProperties {

    /**
     * 是否启用 Mono Consul 模块功能
     */
    private Boolean enabled = true;

}
