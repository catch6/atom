package cn.mindit.atom.feign.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * @author Catch
 * @since 2021-06-29
 */
@Import({FeignClientDecoder.class, FeignExceptionHandler.class, FeignLogger.class})
@RequiredArgsConstructor
@EnableConfigurationProperties(FeignProperties.class)
@PropertySource("classpath:application-feign.properties")
@ConditionalOnProperty(value = "atom.feign.enabled", matchIfMissing = true)
@AutoConfiguration
public class FeignAutoConfiguration {

}
