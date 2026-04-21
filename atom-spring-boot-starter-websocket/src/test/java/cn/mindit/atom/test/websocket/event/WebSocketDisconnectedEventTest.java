package cn.mindit.atom.test.websocket.event;

import cn.mindit.atom.websocket.event.WebSocketDisconnectedEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WebSocketDisconnectedEventTest {

    @Test
    void holdsClientIdUriAndException() {
        RuntimeException cause = new RuntimeException("connection lost");
        Object source = new Object();
        WebSocketDisconnectedEvent event = new WebSocketDisconnectedEvent(
            source, "client-2", "ws://host/ws", cause);

        assertThat(event.getClientId()).isEqualTo("client-2");
        assertThat(event.getUri()).isEqualTo("ws://host/ws");
        assertThat(event.getException()).isSameAs(cause);
        assertThat(event.getSource()).isSameAs(source);
    }

    @Test
    void acceptsNullException() {
        WebSocketDisconnectedEvent event = new WebSocketDisconnectedEvent(
            this, "client-3", "ws://host/ws", null);
        assertThat(event.getException()).isNull();
    }

}
