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

package cn.mindit.atom.mqtt;

/**
 * MQTT 消息处理异常处理器
 * <p>
 * 使用方式:
 * <ul>
 *   <li>全局: 注册一个 {@link MqttListenerErrorHandler} Bean, 所有未指定 errorHandler 的 @MqttListener 都会使用</li>
 *   <li>局部: 在 @MqttListener(errorHandler = "myHandler") 中指定 Bean 名称</li>
 * </ul>
 *
 * @author Catch
 * @since 2024-08-31
 */
@FunctionalInterface
public interface MqttListenerErrorHandler {

    /**
     * 处理 MQTT 消息消费异常
     *
     * @param topic     消息主题
     * @param message   消息内容 (原始字符串)
     * @param exception 异常
     */
    void handleError(String topic, String message, Exception exception);

}
