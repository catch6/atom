package cn.mindit.atom.doc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Data
@ConfigurationProperties(prefix = "atom.doc")
public class DocProperties {

    /**
     * 是否启用 doc 模块
     */
    private boolean enabled = true;

}
