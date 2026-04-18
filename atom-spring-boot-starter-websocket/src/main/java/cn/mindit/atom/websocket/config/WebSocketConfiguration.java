/*
 * Copyright (c) 2022-2026 Catch(catchlife6@163.com).
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

import cn.mindit.atom.websocket.WebSocketClientManager;
import cn.mindit.atom.websocket.WebSocketClientManagerImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * WebSocket自动配置类
 *
 * @author Catch
 * @since 2024-12-17
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableScheduling
public class WebSocketConfiguration {

    @Bean(destroyMethod = "destroy")
    public WebSocketClientManager webSocketClientManager(ApplicationEventPublisher eventPublisher,
                                                         WebSocketProperties webSocketProperties) {
        return new WebSocketClientManagerImpl(eventPublisher, webSocketProperties);
    }

}
