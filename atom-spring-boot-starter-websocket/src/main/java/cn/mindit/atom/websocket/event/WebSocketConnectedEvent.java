package cn.mindit.atom.websocket.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * WebSocket连接建立事件
 * 
 * @author Catch
 * @since 2024-12-17
 */
@Getter
public class WebSocketConnectedEvent extends ApplicationEvent {

    private final String clientId;
    private final String uri;

    public WebSocketConnectedEvent(Object source, String clientId, String uri) {
        super(source);
        this.clientId = clientId;
        this.uri = uri;
    }
}