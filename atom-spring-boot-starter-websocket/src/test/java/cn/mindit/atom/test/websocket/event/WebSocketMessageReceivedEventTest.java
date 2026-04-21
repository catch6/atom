package cn.mindit.atom.test.websocket.event;

import cn.mindit.atom.websocket.event.WebSocketMessageReceivedEvent;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class WebSocketMessageReceivedEventTest {

    @Test
    void textMessageConstructor() {
        WebSocketMessageReceivedEvent event = new WebSocketMessageReceivedEvent(
            this, "client-1", "ws://host/ws", "hello");

        assertThat(event.getClientId()).isEqualTo("client-1");
        assertThat(event.getUri()).isEqualTo("ws://host/ws");
        assertThat(event.getMessage()).isEqualTo("hello");
        assertThat(event.isText()).isTrue();
    }

    @Test
    void binaryMessageConstructor() {
        byte[] data = "binary-data".getBytes(StandardCharsets.UTF_8);
        WebSocketMessageReceivedEvent event = new WebSocketMessageReceivedEvent(
            this, "client-2", "ws://host/ws", data);

        assertThat(event.getClientId()).isEqualTo("client-2");
        assertThat(event.getMessage()).isEqualTo("binary-data");
        assertThat(event.isText()).isFalse();
    }

}
