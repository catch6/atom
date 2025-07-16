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

package cn.mindit.atom.mqtt;

import lombok.Data;
import cn.mindit.atom.mqtt.config.MqttProperties;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.function.BiConsumer;

/**
 * @author Catch
 * @since 2024-08-31
 */
@Data
public class MqttConsumer {

    /**
     * 实例 ID, 为空则使用 {@link MqttProperties#getId()}
     */
    private String id;

    /**
     * 订阅主题
     */
    private String[] topics;

    /**
     * QoS列表, 如果length为1, 则所有主题的QoS都为第一个值, 默认所有QoS为1
     */
    private int[] qos;
    /**
     * 消费对象
     */
    private BiConsumer<String, String> consumer;

    public MqttConsumer() {
    }

    public MqttConsumer(String id, String[] topics, int[] qos, BiConsumer<String, String> consumer) {
        this.id = id;
        this.topics = topics;
        this.qos = qos;
        this.consumer = consumer;
    }

    public void initialize() {
        check();
        alignLength();
    }

    public void check() {
        Assert.notNull(id, "MQTT id must not be null");
        Assert.notEmpty(topics, "MQTT topics must not be empty");
        Assert.isTrue(qos != null && qos.length > 0, "MQTT qos must not be empty");
    }

    public void alignLength() {
        if (qos.length == topics.length) {
            return;
        }
        if (qos.length == 1) {
            int[] targetQos = new int[topics.length];
            Arrays.fill(targetQos, qos[0]);
            qos = targetQos;
            return;
        }
        throw new RuntimeException("MQTT qos length must be 1 or equal to topics length");
    }

}
