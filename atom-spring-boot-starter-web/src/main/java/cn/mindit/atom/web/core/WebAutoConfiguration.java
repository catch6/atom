package cn.mindit.atom.web.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * @author Catch
 * @since 2023-06-06
 */
@ComponentScan("cn.mindit.atom.web")
@EnableConfigurationProperties({CorsProperties.class, LoggingProperties.class, WebProperties.class})
@PropertySource("classpath:application-web.properties")
@ConditionalOnProperty(value = "atom.web.enabled", matchIfMissing = true)
@AutoConfiguration
public class WebAutoConfiguration {

}
