package cn.mindit.atom.test.websocket.config;

import cn.mindit.atom.websocket.WebSocketClientManager;
import cn.mindit.atom.websocket.config.WebSocketAutoConfiguration;
import cn.mindit.atom.websocket.config.WebSocketConfiguration;
import cn.mindit.atom.websocket.config.WebSocketProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class WebSocketAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(WebSocketAutoConfiguration.class));

    @Test
    void loadsAllBeansByDefault() {
        contextRunner
            .withBean("webSocketProperties", WebSocketProperties.class, WebSocketProperties::new)
            .run(context -> {
                assertThat(context).hasSingleBean(WebSocketAutoConfiguration.class);
                assertThat(context).hasSingleBean(WebSocketConfiguration.class);
                assertThat(context).hasSingleBean(WebSocketClientManager.class);
                assertThat(context).hasBean("webSocketProperties");
            });
    }

    @Test
    void disabledByProperty() {
        contextRunner
            .withPropertyValues("atom.websocket.enabled=false")
            .run(context -> {
                assertThat(context).doesNotHaveBean(WebSocketAutoConfiguration.class);
                assertThat(context).doesNotHaveBean(WebSocketClientManager.class);
            });
    }

    @Test
    void propertiesAreBound() {
        contextRunner
            .withBean("webSocketProperties", WebSocketProperties.class, WebSocketProperties::new)
            .withPropertyValues(
                "atom.websocket.id=test-client",
                "atom.websocket.auto-reconnect=false",
                "atom.websocket.reconnect-strategy=EXPONENTIAL_BACKOFF",
                "atom.websocket.reconnect-delay=10s",
                "atom.websocket.max-reconnect-attempts=3",
                "atom.websocket.heartbeat-interval=15s"
            )
            .run(context -> {
                assertThat(context).hasBean("webSocketProperties");
                WebSocketProperties props = context.getBean(
                    "atom.websocket-cn.mindit.atom.websocket.config.WebSocketProperties",
                    WebSocketProperties.class);
                assertThat(props.getId()).isEqualTo("test-client");
                assertThat(props.getAutoReconnect()).isFalse();
                assertThat(props.getReconnectStrategy())
                    .isEqualTo(WebSocketProperties.ReconnectStrategy.EXPONENTIAL_BACKOFF);
                assertThat(props.getReconnectDelay().getSeconds()).isEqualTo(10);
                assertThat(props.getMaxReconnectAttempts()).isEqualTo(3);
                assertThat(props.getHeartbeatInterval().getSeconds()).isEqualTo(15);
            });
    }

}
