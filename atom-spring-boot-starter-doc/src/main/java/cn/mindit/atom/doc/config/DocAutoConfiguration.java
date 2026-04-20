package cn.mindit.atom.doc.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * @author Catch
 * @since 2023-11-05
 */
@EnableConfigurationProperties(DocProperties.class)
@PropertySource("classpath:application-doc.properties")
@ConditionalOnProperty(value = "atom.doc.enabled", matchIfMissing = true)
@AutoConfiguration
public class DocAutoConfiguration {

}
