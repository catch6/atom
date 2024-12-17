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

package net.wenzuo.atom.mqtt.config;

import net.wenzuo.atom.mqtt.MqttListenerProcessor;
import net.wenzuo.atom.mqtt.AsyncMqttServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * @author Catch
 * @since 2024-06-16
 */
@Import({Mqttv3Configuration.class, Mqttv5Configuration.class, MqttListenerProcessor.class, AsyncMqttServiceImpl.class})
@EnableConfigurationProperties(MqttProperties.class)
@ConditionalOnProperty(value = "atom.mqtt.enabled", matchIfMissing = true)
public class MqttAutoConfiguration {

}
