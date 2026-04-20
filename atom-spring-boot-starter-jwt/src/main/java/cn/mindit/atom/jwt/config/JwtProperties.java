package cn.mindit.atom.jwt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Data
@ConfigurationProperties(prefix = "atom.jwt")
public class JwtProperties {

    /**
     * 是否启用 jwt 模块
     */
    private Boolean enabled = true;
    /**
     * jwt secret
     */
    private String secret;

}
