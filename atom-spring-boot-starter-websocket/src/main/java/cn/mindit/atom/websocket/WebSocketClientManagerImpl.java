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

package cn.mindit.atom.websocket;

import cn.mindit.atom.websocket.config.WebSocketProperties;
import cn.mindit.atom.websocket.event.WebSocketConnectedEvent;
import cn.mindit.atom.websocket.event.WebSocketDisconnectedEvent;
import cn.mindit.atom.websocket.event.WebSocketMessageReceivedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * WebSocket客户端管理器实现类
 *
 * @author Catch
 * @since 2024-12-17
 */
@Slf4j
public class WebSocketClientManagerImpl implements WebSocketClientManager, DisposableBean {

    private final ApplicationEventPublisher eventPublisher;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, WebSocketProperties.WebSocketInstance> instanceConfigs = new ConcurrentHashMap<>();
    private final Map<String, Integer> reconnectAttempts = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    public WebSocketClientManagerImpl(ApplicationEventPublisher eventPublisher,
                                      WebSocketProperties webSocketProperties) {
        this.eventPublisher = eventPublisher;
        // 初始化所有配置的实例
        for (WebSocketProperties.WebSocketInstance instance : webSocketProperties.getInstances()) {
            if (instance.getEnabled()) {
                instanceConfigs.put(instance.getId(), instance);
                reconnectAttempts.put(instance.getId(), 0);
                // 异步连接
                scheduler.submit(() -> {
                    try {
                        connect(instance.getId());
                    } catch (Exception e) {
                        log.error("Failed to connect WebSocket instance: {}", instance.getId(), e);
                    }
                });
            }
        }
    }

    @Override
    public WebSocketSession getSession(String id) {
        return sessions.get(id);
    }

    @Override
    public void sendMessage(String id, String message) throws Exception {
        WebSocketSession session = sessions.get(id);
        if (session == null || !session.isOpen()) {
            throw new IllegalStateException("WebSocket session is not available for id: " + id);
        }
        session.sendMessage(new TextMessage(message));
        log.debug("Sent text message to WebSocket {}: {}", id, message);
    }

    @Override
    public void sendMessage(String id, byte[] message) throws Exception {
        WebSocketSession session = sessions.get(id);
        if (session == null || !session.isOpen()) {
            throw new IllegalStateException("WebSocket session is not available for id: " + id);
        }
        session.sendMessage(new BinaryMessage(message));
        log.debug("Sent binary message to WebSocket {}: {} bytes", id, message.length);
    }

    @Override
    public boolean isConnected(String id) {
        WebSocketSession session = sessions.get(id);
        return session != null && session.isOpen();
    }

    @Override
    public java.util.Set<String> getClientIds() {
        return instanceConfigs.keySet();
    }

    @Override
    public void connect(String id) throws Exception {
        WebSocketProperties.WebSocketInstance config = instanceConfigs.get(id);
        if (config == null) {
            throw new IllegalArgumentException("No configuration found for WebSocket id: " + id);
        }

        WebSocketClient client = new StandardWebSocketClient();
        WebSocketSession session = client.execute(new WebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                log.info("WebSocket connected: {} -> {}", id, config.getUrl());
                sessions.put(id, session);
                reconnectAttempts.put(id, 0);
                eventPublisher.publishEvent(new WebSocketConnectedEvent(this, id, config.getUrl()));
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                if (message instanceof TextMessage) {
                    String payload = ((TextMessage) message).getPayload();
                    log.debug("Received text message from WebSocket {}: {}", id, payload);
                    eventPublisher.publishEvent(new WebSocketMessageReceivedEvent(this, id, config.getUrl(), payload));
                } else if (message instanceof BinaryMessage) {
                    byte[] payload = ((BinaryMessage) message).getPayload().array();
                    log.debug("Received binary message from WebSocket {}: {} bytes", id, payload.length);
                    eventPublisher.publishEvent(new WebSocketMessageReceivedEvent(this, id, config.getUrl(), payload));
                }
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                log.error("WebSocket transport error for {}: {}", id, exception.getMessage(), exception);
                handleDisconnection(id, exception);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                log.info("WebSocket connection closed: {} -> {}, status: {}", id, config.getUrl(), closeStatus);
                handleDisconnection(id, null);
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        }, null, URI.create(config.getUrl())).get();

        log.info("WebSocket connection established for id: {}", id);
    }

    @Override
    public void disconnect(String id) throws Exception {
        WebSocketSession session = sessions.get(id);
        if (session != null && session.isOpen()) {
            session.close();
            sessions.remove(id);
            log.info("WebSocket disconnected for id: {}", id);
        }
    }

    private void handleDisconnection(String id, Throwable exception) {
        sessions.remove(id);
        eventPublisher.publishEvent(new WebSocketDisconnectedEvent(this, id, instanceConfigs.get(id).getUrl(),
            exception instanceof Exception ? (Exception) exception : new Exception("Connection closed")));

        // 自动重连逻辑
        WebSocketProperties.WebSocketInstance config = instanceConfigs.get(id);
        if (config != null && config.getAutoReconnect()) {
            Integer attempts = reconnectAttempts.get(id);
            if (attempts == null) {
                attempts = 0;
            }

            if (attempts < config.getMaxReconnectAttempts()) {
                reconnectAttempts.put(id, attempts + 1);

                long delay = calculateDelay(config, attempts);
                log.info("Scheduling reconnect attempt {} for WebSocket {} in {} ms", attempts + 1, id, delay);

                scheduler.schedule(() -> {
                    try {
                        connect(id);
                    } catch (Exception e) {
                        log.error("Failed to reconnect WebSocket: {}", id, e);
                    }
                }, delay, TimeUnit.MILLISECONDS);
            } else {
                log.warn("Max reconnect attempts ({}) reached for WebSocket: {}", config.getMaxReconnectAttempts(), id);
            }
        }
    }

    private long calculateDelay(WebSocketProperties.WebSocketInstance config, int attempts) {
        switch (config.getReconnectStrategy()) {
            case EXPONENTIAL_BACKOFF:
                return config.getReconnectDelay().toMillis() * (long) Math.pow(2, attempts);
            case FIXED_DELAY:
            default:
                return config.getReconnectDelay().toMillis();
        }
    }

    /**
     * 定时心跳检查
     */
    @Scheduled(fixedDelayString = "#{@webSocketProperties.heartbeatInterval}")
    public void heartbeatCheck() {
        for (Map.Entry<String, WebSocketProperties.WebSocketInstance> entry : instanceConfigs.entrySet()) {
            String id = entry.getKey();
            WebSocketProperties.WebSocketInstance config = entry.getValue();

            if (isConnected(id) && config.getHeartbeatInterval() != null &&
                config.getHeartbeatInterval().toMillis() > 0) {
                try {
                    sendMessage(id, "{\"type\":\"ping\",\"timestamp\":" + System.currentTimeMillis() + "}");
                } catch (Exception e) {
                    log.warn("Failed to send heartbeat to WebSocket: {}", id, e);
                }
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        scheduler.shutdown();
        for (String id : getClientIds()) {
            try {
                disconnect(id);
            } catch (Exception e) {
                log.warn("Failed to disconnect WebSocket during shutdown: {}", id, e);
            }
        }
    }

}