package cn.mindit.atom.websocket.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * WebSocket连接断开事件
 * 
 * @author Catch
 * @since 2024-12-17
 */
@Getter
public class WebSocketDisconnectedEvent extends ApplicationEvent {

    private final String clientId;
    private final String uri;
    private final Exception exception;

    public WebSocketDisconnectedEvent(Object source, String clientId, String uri, Exception exception) {
        super(source);
        this.clientId = clientId;
        this.uri = uri;
        this.exception = exception;
    }
}