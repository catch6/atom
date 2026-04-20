package cn.mindit.atom.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catch
 * @since 2024-06-16
 */
@ConfigurationProperties(prefix = "atom.mqtt")
@Data
public class MqttProperties {

    public static final String CLIENT_BEAN_PREFIX = "mqttClient-";

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
     * 实例服务器地址
     */
    private String url;
    /**
     * 实例服务器用户名
     */
    private String username;
    /**
     * 实例服务器密码
     */
    private String password;
    /**
     * 实例客户端 ID, 默认为 ${spring.application.name}-${spring.profiles.active}
     */
    private String clientId;

    /**
     * 是否开启异步分发, 开启后 MQTT 消息将通过线程池分发到监听器, 避免阻塞 Paho 回调线程
     */
    private Boolean async = false;

    /**
     * 异步分发线程池配置, 仅在 async=true 时生效
     */
    private MqttExecutor executor = new MqttExecutor();

    /**
     * MQTT 实例配置
     */
    private List<MqttInstance> instances;

    @Data
    public static class MqttInstance {

        /**
         * 实例 ID
         */
        private String id;
        /**
         * 实例是否启用
         */
        private Boolean enabled = true;
        /**
         * 实例服务器地址
         */
        private String url;
        /**
         * 实例服务器用户名
         */
        private String username;
        /**
         * 实例服务器密码
         */
        private String password;
        /**
         * 实例客户端 ID, 默认为 ${spring.application.name}-${spring.profiles.active}-随机6位字符
         */
        private String clientId;

    }

    @Data
    public static class MqttExecutor {

        /**
         * 核心线程数
         */
        private Integer corePoolSize = 4;
        /**
         * 最大线程数
         */
        private Integer maxPoolSize = 16;
        /**
         * 队列容量
         */
        private Integer queueCapacity = 1024;
        /**
         * 线程名前缀
         */
        private String threadNamePrefix = "mqtt-listener-";

    }

    public List<MqttInstance> getInstances() {
        List<MqttInstance> instances = new ArrayList<>();
        if (id != null && url != null) {
            MqttInstance instance = new MqttInstance();
            instance.setId(id);
            instance.setEnabled(enabled);
            instance.setUrl(url);
            instance.setUsername(username);
            instance.setPassword(password);
            instance.setClientId(clientId);
            instances.add(instance);
        }
        if (this.instances != null && !this.instances.isEmpty()) {
            instances.addAll(this.instances);
        }
        return instances;
    }

}
