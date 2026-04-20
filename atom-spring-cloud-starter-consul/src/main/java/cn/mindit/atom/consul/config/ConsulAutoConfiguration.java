package cn.mindit.atom.consul.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * @author Catch
 * @since 2023-06-06
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(ConsulProperties.class)
@PropertySource("classpath:application-consul.properties")
@ConditionalOnProperty(value = "atom.consul.enabled", matchIfMissing = true)
@AutoConfiguration
public class ConsulAutoConfiguration {

}
