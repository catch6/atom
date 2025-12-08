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

package cn.mindit.atom.websocket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WebSocket配置属性类
 *
 * @author Catch
 * @since 2024-12-17
 */
@ConfigurationProperties(prefix = "atom.websocket")
@Data
public class WebSocketProperties {

    public static final String CLIENT_BEAN_PREFIX = "webSocketClient-";

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 加载顺序, 默认 Ordered.LOWEST_PRECEDENCE
     */
    private Integer order = Ordered.LOWEST_PRECEDENCE;

    /**
     * 实例 ID
     */
    private String id = "default";

    /**
     * 目标URL，如 ws://localhost:8080/websocket
     */
    private String url;

    /**
     * 是否在连接断开时自动重连
     */
    private Boolean autoReconnect = true;

    /**
     * 重连策略
     */
    private ReconnectStrategy reconnectStrategy = ReconnectStrategy.FIXED_DELAY;

    /**
     * 重连基础延迟时间
     */
    private Duration reconnectDelay = Duration.ofSeconds(5);

    /**
     * 最大重试次数
     */
    private Integer maxReconnectAttempts = 10;

    /**
     * 心跳间隔
     */
    private Duration heartbeatInterval = Duration.ofSeconds(10);

    /**
     * 连接时附加的请求头
     */
    private Map<String, String> headers = new HashMap<>();

    /**
     * WebSocket 实例配置列表
     */
    private List<WebSocketInstance> instances;

    /**
     * 重连策略枚举
     */
    public enum ReconnectStrategy {
        /**
         * 固定延迟
         */
        FIXED_DELAY,
        /**
         * 指数退避
         */
        EXPONENTIAL_BACKOFF
    }

    @Data
    public static class WebSocketInstance {

        /**
         * 实例 ID
         */
        private String id;

        /**
         * 实例是否启用
         */
        private Boolean enabled = true;

        /**
         * 目标URL，如 ws://localhost:8080/websocket
         */
        private String url;

        /**
         * 是否在连接断开时自动重连
         */
        private Boolean autoReconnect = true;

        /**
         * 重连策略
         */
        private ReconnectStrategy reconnectStrategy = ReconnectStrategy.FIXED_DELAY;

        /**
         * 重连基础延迟时间
         */
        private Duration reconnectDelay = Duration.ofSeconds(5);

        /**
         * 最大重试次数
         */
        private Integer maxReconnectAttempts = 10;

        /**
         * 心跳间隔
         */
        private Duration heartbeatInterval = Duration.ofSeconds(30);

        /**
         * 连接时附加的请求头
         */
        private Map<String, String> headers = new HashMap<>();

    }

    public List<WebSocketInstance> getInstances() {
        List<WebSocketInstance> instances = new ArrayList<>();
        if (id != null && url != null) {
            WebSocketInstance instance = new WebSocketInstance();
            instance.setId(id);
            instance.setEnabled(enabled);
            instance.setUrl(url);
            instance.setAutoReconnect(autoReconnect);
            instance.setReconnectStrategy(reconnectStrategy);
            instance.setReconnectDelay(reconnectDelay);
            instance.setMaxReconnectAttempts(maxReconnectAttempts);
            instance.setHeartbeatInterval(heartbeatInterval);
            instance.setHeaders(headers);
            instances.add(instance);
        }
        if (this.instances != null && !this.instances.isEmpty()) {
            instances.addAll(this.instances);
        }
        return instances;
    }

}