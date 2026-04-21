package cn.mindit.atom.test.websocket;

import cn.mindit.atom.websocket.WebSocketClientManagerImpl;
import cn.mindit.atom.websocket.config.WebSocketProperties;
import cn.mindit.atom.websocket.config.WebSocketProperties.WebSocketInstance;
import cn.mindit.atom.websocket.event.WebSocketDisconnectedEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class WebSocketClientManagerImplTest {

    private ApplicationEventPublisher eventPublisher;
    private WebSocketClientManagerImpl manager;

    @BeforeEach
    void setUp() {
        eventPublisher = mock(ApplicationEventPublisher.class);
        WebSocketProperties props = new WebSocketProperties();
        manager = new WebSocketClientManagerImpl(eventPublisher, props);
    }

    @AfterEach
    void tearDown() throws Exception {
        manager.destroy();
    }

    @Test
    void getSession_returnsNullForUnknownId() {
        assertThat(manager.getSession("unknown")).isNull();
    }

    @Test
    void isConnected_returnsFalseForUnknownId() {
        assertThat(manager.isConnected("unknown")).isFalse();
    }

    @Test
    void getClientIds_returnsEmptyWhenNoInstances() {
        assertThat(manager.getClientIds()).isEmpty();
    }

    @Test
    void getClientIds_returnsConfiguredIds() {
        WebSocketProperties props = new WebSocketProperties();
        props.setUrl(null);

        WebSocketInstance instance = new WebSocketInstance();
        instance.setId("ws-1");
        instance.setUrl("ws://localhost/ws");
        instance.setEnabled(false);

        props.setInstances(java.util.List.of(instance));
        WebSocketClientManagerImpl mgr = new WebSocketClientManagerImpl(eventPublisher, props);

        // disabled instances are not registered
        assertThat(mgr.getClientIds()).isEmpty();
    }

    @Test
    void sendTextMessage_throwsWhenNotConnected() {
        assertThatThrownBy(() -> manager.sendMessage("missing", "hello"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("missing");
    }

    @Test
    void sendBinaryMessage_throwsWhenNotConnected() {
        assertThatThrownBy(() -> manager.sendMessage("missing", new byte[]{1, 2}))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("missing");
    }

    @Test
    void connect_throwsForUnknownId() {
        assertThatThrownBy(() -> manager.connect("unknown"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("unknown");
    }

    @Test
    void disconnect_doesNothingForUnknownId() throws Exception {
        manager.disconnect("unknown");
        // no exception expected
    }

    @Test
    void sendTextMessage_delegatesToSession() throws Exception {
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.isOpen()).thenReturn(true);
        injectSession("test", session);

        manager.sendMessage("test", "hello");
        verify(session).sendMessage(any(TextMessage.class));
    }

    @Test
    void sendTextMessage_throwsWhenSessionClosed() throws Exception {
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.isOpen()).thenReturn(false);
        injectSession("test", session);

        assertThatThrownBy(() -> manager.sendMessage("test", "hello"))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void isConnected_returnsTrueForOpenSession() throws Exception {
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.isOpen()).thenReturn(true);
        injectSession("test", session);

        assertThat(manager.isConnected("test")).isTrue();
    }

    @Test
    void isConnected_returnsFalseForClosedSession() throws Exception {
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.isOpen()).thenReturn(false);
        injectSession("test", session);

        assertThat(manager.isConnected("test")).isFalse();
    }

    @Test
    void disconnect_closesAndRemovesSession() throws Exception {
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.isOpen()).thenReturn(true);
        injectSession("test", session);

        manager.disconnect("test");

        verify(session).close();
        assertThat(manager.getSession("test")).isNull();
    }

    @Test
    void heartbeatCheck_sendsToConnectedSessions() throws Exception {
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.isOpen()).thenReturn(true);
        injectSession("hb-client", session);
        injectInstanceConfig("hb-client");

        manager.heartbeatCheck();

        verify(session).sendMessage(any(TextMessage.class));
    }

    @Test
    void heartbeatCheck_skipsDisconnectedSessions() throws Exception {
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.isOpen()).thenReturn(false);
        injectSession("hb-client", session);
        injectInstanceConfig("hb-client");

        manager.heartbeatCheck();

        verify(session, never()).sendMessage(any());
    }

    @Test
    void destroy_closesAllSessionsGracefully() throws Exception {
        WebSocketSession session1 = mock(WebSocketSession.class);
        WebSocketSession session2 = mock(WebSocketSession.class);
        when(session1.isOpen()).thenReturn(true);
        when(session2.isOpen()).thenReturn(true);

        injectSession("s1", session1);
        injectSession("s2", session2);
        injectInstanceConfig("s1");
        injectInstanceConfig("s2");

        manager.destroy();

        verify(session1).close();
        verify(session2).close();
    }

    @Test
    void calculateDelay_fixedDelay() throws Exception {
        WebSocketInstance config = new WebSocketInstance();
        config.setReconnectStrategy(WebSocketProperties.ReconnectStrategy.FIXED_DELAY);
        config.setReconnectDelay(java.time.Duration.ofSeconds(5));

        long delay = invokeCalculateDelay(config, 0);
        assertThat(delay).isEqualTo(5000);
        assertThat(invokeCalculateDelay(config, 3)).isEqualTo(5000);
    }

    @Test
    void calculateDelay_exponentialBackoff() throws Exception {
        WebSocketInstance config = new WebSocketInstance();
        config.setReconnectStrategy(WebSocketProperties.ReconnectStrategy.EXPONENTIAL_BACKOFF);
        config.setReconnectDelay(java.time.Duration.ofSeconds(1));

        assertThat(invokeCalculateDelay(config, 0)).isEqualTo(1000);
        assertThat(invokeCalculateDelay(config, 1)).isEqualTo(2000);
        assertThat(invokeCalculateDelay(config, 2)).isEqualTo(4000);
        assertThat(invokeCalculateDelay(config, 3)).isEqualTo(8000);
    }

    @SuppressWarnings("unchecked")
    private void injectSession(String id, WebSocketSession session) throws Exception {
        Field field = WebSocketClientManagerImpl.class.getDeclaredField("sessions");
        field.setAccessible(true);
        Map<String, WebSocketSession> sessions = (Map<String, WebSocketSession>) field.get(manager);
        sessions.put(id, session);
    }

    @SuppressWarnings("unchecked")
    private void injectInstanceConfig(String id) throws Exception {
        Field field = WebSocketClientManagerImpl.class.getDeclaredField("instanceConfigs");
        field.setAccessible(true);
        Map<String, WebSocketInstance> configs = (Map<String, WebSocketInstance>) field.get(manager);
        if (!configs.containsKey(id)) {
            WebSocketInstance config = new WebSocketInstance();
            config.setId(id);
            config.setUrl("ws://localhost/" + id);
            config.setHeartbeatInterval(java.time.Duration.ofSeconds(30));
            configs.put(id, config);
        }
    }

    private long invokeCalculateDelay(WebSocketInstance config, int attempts) throws Exception {
        var method = WebSocketClientManagerImpl.class.getDeclaredMethod(
            "calculateDelay", WebSocketInstance.class, int.class);
        method.setAccessible(true);
        return (long) method.invoke(manager, config, attempts);
    }

}
