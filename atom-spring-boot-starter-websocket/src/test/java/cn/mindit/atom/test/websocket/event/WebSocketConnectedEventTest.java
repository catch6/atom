package cn.mindit.atom.test.websocket.event;

import cn.mindit.atom.websocket.event.WebSocketConnectedEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WebSocketConnectedEventTest {

    @Test
    void holdsClientIdAndUri() {
        Object source = new Object();
        WebSocketConnectedEvent event = new WebSocketConnectedEvent(source, "client-1", "ws://localhost/ws");

        assertThat(event.getClientId()).isEqualTo("client-1");
        assertThat(event.getUri()).isEqualTo("ws://localhost/ws");
        assertThat(event.getSource()).isSameAs(source);
    }

}
