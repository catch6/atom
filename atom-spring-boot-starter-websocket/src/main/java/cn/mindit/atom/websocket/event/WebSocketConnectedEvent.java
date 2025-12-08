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