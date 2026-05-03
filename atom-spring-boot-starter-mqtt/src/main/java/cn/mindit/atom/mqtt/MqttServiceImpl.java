package cn.mindit.atom.mqtt;

import cn.mindit.atom.mqtt.config.MqttProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.nio.charset.StandardCharsets;

/**
 * @author Catch
 * @since 2024-06-25
 */
@Slf4j
@RequiredArgsConstructor
public class MqttServiceImpl implements MqttService {

    private final ApplicationContext applicationContext;
    private final MqttProperties mqttProperties;

    @Override
    public String defaultId() {
        return mqttProperties.getId();
    }

    @Override
    public void send(String id, String topic, String message, int qos, boolean retained) {
        if (log.isDebugEnabled()) {
            log.debug("MQTT send: id={}, topic={}, qos: {}, retained: {}, message={}", id, topic, qos, retained, message);
        }
        try {
            Object mqttClient = applicationContext.getBean(MqttProperties.CLIENT_BEAN_PREFIX + id);
            byte[] payload = message.getBytes(StandardCharsets.UTF_8);
            if (mqttClient instanceof org.eclipse.paho.mqttv5.client.MqttClient v5) {
                v5.publish(topic, payload, qos, retained);
                return;
            }
            if (mqttClient instanceof org.eclipse.paho.client.mqttv3.MqttClient v3) {
                v3.publish(topic, payload, qos, retained);
                return;
            }
            throw new RuntimeException("MQTT client not supported: " + mqttClient.getClass().getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
