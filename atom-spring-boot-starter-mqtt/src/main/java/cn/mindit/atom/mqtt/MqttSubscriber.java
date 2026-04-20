package cn.mindit.atom.mqtt;

/**
 * @author Catch
 * @since 2024-06-25
 */
public interface MqttSubscriber {

    /**
     * 实例 ID, 为空则使用 MqttProperties.getId()
     *
     * @return 实例 ID
     */
    default String id() {
        return "";
    }

    /**
     * 订阅主题
     *
     * @return 主题列表
     */
    String[] topics();

    /**
     * QoS
     *
     * @return QoS列表, 如果length为1, 则所有主题的QoS都为第一个值, 默认所有QoS为0
     */
    default int[] qos() {
        return new int[]{0};
    }

    /**
     * 订阅消息
     *
     * @param topic   主题
     * @param message 消息
     */
    void message(String topic, String message);

}
