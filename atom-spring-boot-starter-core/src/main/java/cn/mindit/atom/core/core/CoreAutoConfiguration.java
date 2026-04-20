package cn.mindit.atom.core.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * @author Catch
 * @since 2023-06-05
 */
@Import({
    CoreJsonConfiguration.class,
    TraceIdTaskDecorator.class,
    CoreTraceConfiguration.class
})
@EnableConfigurationProperties(CoreProperties.class)
@PropertySource("classpath:application-core.properties")
@ConditionalOnProperty(value = "atom.core.enabled", matchIfMissing = true)
@AutoConfiguration
public class CoreAutoConfiguration {

}
