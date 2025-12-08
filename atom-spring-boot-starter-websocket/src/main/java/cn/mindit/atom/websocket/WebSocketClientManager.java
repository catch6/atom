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

import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket客户端管理器接口
 * 
 * @author Catch
 * @since 2024-12-17
 */
public interface WebSocketClientManager {

    /**
     * 获取指定ID的WebSocket会话
     * 
     * @param id 客户端ID
     * @return WebSocket会话，如果不存在或未连接则返回null
     */
    WebSocketSession getSession(String id);

    /**
     * 发送文本消息到指定ID的WebSocket连接
     * 
     * @param id 客户端ID
     * @param message 文本消息
     * @throws Exception 发送失败时抛出异常
     */
    void sendMessage(String id, String message) throws Exception;

    /**
     * 发送二进制消息到指定ID的WebSocket连接
     * 
     * @param id 客户端ID
     * @param message 二进制消息
     * @throws Exception 发送失败时抛出异常
     */
    void sendMessage(String id, byte[] message) throws Exception;

    /**
     * 检查指定ID的WebSocket连接是否活跃
     * 
     * @param id 客户端ID
     * @return 如果连接活跃返回true，否则返回false
     */
    boolean isConnected(String id);

    /**
     * 获取所有已配置的客户端ID列表
     * 
     * @return 客户端ID列表
     */
    java.util.Set<String> getClientIds();

    /**
     * 手动连接指定ID的WebSocket
     * 
     * @param id 客户端ID
     * @throws Exception 连接失败时抛出异常
     */
    void connect(String id) throws Exception;

    /**
     * 手动断开指定ID的WebSocket连接
     * 
     * @param id 客户端ID
     * @throws Exception 断开失败时抛出异常
     */
    void disconnect(String id) throws Exception;
}