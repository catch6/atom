package cn.mindit.atom.websocket.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * WebSocket自动配置类
 *
 * @author Catch
 * @since 2024-12-17
 */
@Import({WebSocketConfiguration.class})
@EnableConfigurationProperties(WebSocketProperties.class)
@ConditionalOnProperty(value = "atom.websocket.enabled", matchIfMissing = true)
@AutoConfiguration
public class WebSocketAutoConfiguration {

}