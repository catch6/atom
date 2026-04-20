package cn.mindit.atom.mqtt.config;

import cn.mindit.atom.mqtt.MqttListenerProcessor;
import cn.mindit.atom.mqtt.MqttServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * @author Catch
 * @since 2024-06-16
 */
@Import({Mqttv3Configuration.class, Mqttv5Configuration.class, MqttListenerProcessor.class, MqttServiceImpl.class})
@EnableConfigurationProperties(MqttProperties.class)
@ConditionalOnProperty(value = "atom.mqtt.enabled", matchIfMissing = true)
@AutoConfiguration
public class MqttAutoConfiguration {

}
