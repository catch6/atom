package cn.mindit.atom.test.websocket.config;

import cn.mindit.atom.websocket.config.WebSocketProperties;
import cn.mindit.atom.websocket.config.WebSocketProperties.ReconnectStrategy;
import cn.mindit.atom.websocket.config.WebSocketProperties.WebSocketInstance;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WebSocketPropertiesTest {

    @Test
    void defaultValues() {
        WebSocketProperties props = new WebSocketProperties();
        assertThat(props.getEnabled()).isTrue();
        assertThat(props.getId()).isEqualTo("default");
        assertThat(props.getUrl()).isNull();
        assertThat(props.getAutoReconnect()).isTrue();
        assertThat(props.getReconnectStrategy()).isEqualTo(ReconnectStrategy.FIXED_DELAY);
        assertThat(props.getReconnectDelay()).isEqualTo(Duration.ofSeconds(5));
        assertThat(props.getMaxReconnectAttempts()).isEqualTo(10);
        assertThat(props.getHeartbeatInterval()).isEqualTo(Duration.ofSeconds(10));
        assertThat(props.getHeaders()).isEmpty();
        assertThat(props.getInstances()).isEmpty();
    }

    @Test
    void getInstances_createsDefaultInstanceFromRootProperties() {
        WebSocketProperties props = new WebSocketProperties();
        props.setId("main");
        props.setUrl("ws://localhost:8080/ws");
        props.setAutoReconnect(false);
        props.setReconnectStrategy(ReconnectStrategy.EXPONENTIAL_BACKOFF);
        props.setReconnectDelay(Duration.ofSeconds(10));
        props.setMaxReconnectAttempts(5);
        props.setHeartbeatInterval(Duration.ofSeconds(20));

        List<WebSocketInstance> instances = props.getInstances();
        assertThat(instances).hasSize(1);

        WebSocketInstance instance = instances.get(0);
        assertThat(instance.getId()).isEqualTo("main");
        assertThat(instance.getUrl()).isEqualTo("ws://localhost:8080/ws");
        assertThat(instance.getAutoReconnect()).isFalse();
        assertThat(instance.getReconnectStrategy()).isEqualTo(ReconnectStrategy.EXPONENTIAL_BACKOFF);
        assertThat(instance.getReconnectDelay()).isEqualTo(Duration.ofSeconds(10));
        assertThat(instance.getMaxReconnectAttempts()).isEqualTo(5);
        assertThat(instance.getHeartbeatInterval()).isEqualTo(Duration.ofSeconds(20));
    }

    @Test
    void getInstances_returnsEmptyWhenNoUrlConfigured() {
        WebSocketProperties props = new WebSocketProperties();
        List<WebSocketInstance> instances = props.getInstances();
        assertThat(instances).isEmpty();
    }

    @Test
    void getInstances_mergesRootAndExplicitInstances() {
        WebSocketProperties props = new WebSocketProperties();
        props.setId("root");
        props.setUrl("ws://localhost:8080/ws1");

        WebSocketInstance extra = new WebSocketInstance();
        extra.setId("extra");
        extra.setUrl("ws://localhost:8080/ws2");

        props.setInstances(List.of(extra));

        List<WebSocketInstance> instances = props.getInstances();
        assertThat(instances).hasSize(2);
        assertThat(instances.get(0).getId()).isEqualTo("root");
        assertThat(instances.get(1).getId()).isEqualTo("extra");
    }

    @Test
    void getInstances_returnsOnlyExplicitInstancesWhenNoRootUrl() {
        WebSocketProperties props = new WebSocketProperties();

        WebSocketInstance extra = new WebSocketInstance();
        extra.setId("extra");
        extra.setUrl("ws://localhost:8080/ws2");

        props.setInstances(List.of(extra));

        List<WebSocketInstance> instances = props.getInstances();
        assertThat(instances).hasSize(1);
        assertThat(instances.get(0).getId()).isEqualTo("extra");
    }

    @Test
    void webSocketInstance_defaultValues() {
        WebSocketInstance instance = new WebSocketInstance();
        assertThat(instance.getEnabled()).isTrue();
        assertThat(instance.getAutoReconnect()).isTrue();
        assertThat(instance.getReconnectStrategy()).isEqualTo(ReconnectStrategy.FIXED_DELAY);
        assertThat(instance.getReconnectDelay()).isEqualTo(Duration.ofSeconds(5));
        assertThat(instance.getMaxReconnectAttempts()).isEqualTo(10);
        assertThat(instance.getHeartbeatInterval()).isEqualTo(Duration.ofSeconds(30));
        assertThat(instance.getHeaders()).isEmpty();
    }

}
