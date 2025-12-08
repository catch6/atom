/*
 * Copyright (c) 2022-2024 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.websocket.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * WebSocket消息接收事件
 * 
 * @author Catch
 * @since 2024-12-17
 */
@Getter
public class WebSocketMessageReceivedEvent extends ApplicationEvent {

    private final String clientId;
    private final String uri;
    private final String message;
    private final boolean isText;

    public WebSocketMessageReceivedEvent(Object source, String clientId, String uri, String message) {
        super(source);
        this.clientId = clientId;
        this.uri = uri;
        this.message = message;
        this.isText = true;
    }

    public WebSocketMessageReceivedEvent(Object source, String clientId, String uri, byte[] message) {
        super(source);
        this.clientId = clientId;
        this.uri = uri;
        this.message = new String(message);
        this.isText = false;
    }
}