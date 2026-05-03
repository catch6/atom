package cn.mindit.atom.mqtt;

/**
 * @author Catch
 * @since 2024-12-17
 */
public interface MqttService {

    int DEFAULT_QOS = 1;
    boolean DEFAULT_RETAINED = false;

    String defaultId();

    void send(String id, String topic, String message, int qos, boolean retained);

    default void send(String topic, String message) {
        send(defaultId(), topic, message, DEFAULT_QOS, DEFAULT_RETAINED);
    }

    default void send(String topic, String message, int qos) {
        send(defaultId(), topic, message, qos, DEFAULT_RETAINED);
    }

    default void send(String topic, String message, boolean retained) {
        send(defaultId(), topic, message, DEFAULT_QOS, retained);
    }

    default void send(String topic, String message, int qos, boolean retained) {
        send(defaultId(), topic, message, qos, retained);
    }

    default void send(String id, String topic, String message) {
        send(id, topic, message, DEFAULT_QOS, DEFAULT_RETAINED);
    }

    default void send(String id, String topic, String message, int qos) {
        send(id, topic, message, qos, DEFAULT_RETAINED);
    }

    default void send(String id, String topic, String message, boolean retained) {
        send(id, topic, message, DEFAULT_QOS, retained);
    }

}
