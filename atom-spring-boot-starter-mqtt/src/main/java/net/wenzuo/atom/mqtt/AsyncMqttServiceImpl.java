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

package net.wenzuo.atom.mqtt;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.mqtt.config.MqttProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;

/**
 * @author Catch
 * @since 2024-06-25
 */
@Slf4j
@RequiredArgsConstructor
public class AsyncMqttServiceImpl implements MqttService {

    public static final int DEFAULT_QOS = 1;
    public static final boolean DEFAULT_RETAINED = false;

    private final ApplicationContext applicationContext;
    private final MqttProperties mqttProperties;

    @Lazy
    @Resource
    private MqttService mqttService;

    @Override
    public void send(String topic, String message) {
        mqttService.send(mqttProperties.getId(), topic, message, DEFAULT_QOS, DEFAULT_RETAINED);
    }

    @Override
    public void send(String topic, String message, int qos) {
        mqttService.send(mqttProperties.getId(), topic, message, qos, DEFAULT_RETAINED);
    }

    @Override
    public void send(String topic, String message, boolean retained) {
        mqttService.send(mqttProperties.getId(), topic, message, DEFAULT_QOS, retained);
    }

    @Override
    public void send(String topic, String message, int qos, boolean retained) {
        mqttService.send(mqttProperties.getId(), topic, message, qos, retained);
    }

    @Override
    public void send(String id, String topic, String message) {
        mqttService.send(id, topic, message, DEFAULT_QOS, DEFAULT_RETAINED);
    }

    @Override
    public void send(String id, String topic, String message, int qos) {
        mqttService.send(id, topic, message, qos, DEFAULT_RETAINED);
    }

    @Override
    public void send(String id, String topic, String message, boolean retained) {
        mqttService.send(id, topic, message, DEFAULT_QOS, retained);
    }

    @Async
    @Override
    public void send(String id, String topic, String message, int qos, boolean retained) {
        if (log.isDebugEnabled()) {
            log.debug("MQTT send: id={}, topic={}, qos: {}, retained: {}, message={}", id, topic, qos, retained, message);
        }
        try {
            Object mqttClient = applicationContext.getBean(MqttProperties.CLIENT_BEAN_PREFIX + id);
            String className = mqttClient.getClass().getName();
            if ("org.eclipse.paho.mqttv5.client.MqttClient".equals(className)) {
                ((org.eclipse.paho.mqttv5.client.MqttClient) mqttClient).publish(topic, message.getBytes(), qos, retained);
                return;
            }
            if ("org.eclipse.paho.client.mqttv3.MqttClient".equals(className)) {
                ((org.eclipse.paho.client.mqttv3.MqttClient) mqttClient).publish(topic, message.getBytes(), qos, retained);
                return;
            }
            throw new RuntimeException("MQTT client not supported: " + className);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
