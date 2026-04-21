package cn.mindit.atom.websocket.config;

import cn.mindit.atom.websocket.WebSocketClientManager;
import cn.mindit.atom.websocket.WebSocketClientManagerImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * WebSocket自动配置类
 *
 * @author Catch
 * @since 2024-12-17
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableScheduling
public class WebSocketConfiguration {

    @Bean
    public WebSocketClientManager webSocketClientManager(ApplicationEventPublisher eventPublisher,
                                                         WebSocketProperties webSocketProperties) {
        return new WebSocketClientManagerImpl(eventPublisher, webSocketProperties);
    }

}
