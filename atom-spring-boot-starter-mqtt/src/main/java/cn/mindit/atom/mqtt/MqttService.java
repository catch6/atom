package cn.mindit.atom.mqtt;

/**
 * @author Catch
 * @since 2024-12-17
 */
public interface MqttService {

    void send(String topic, String message);

    void send(String topic, String message, int qos);

    void send(String topic, String message, boolean retained);

    void send(String topic, String message, int qos, boolean retained);

    void send(String id, String topic, String message);

    void send(String id, String topic, String message, int qos);

    void send(String id, String topic, String message, boolean retained);

    void send(String id, String topic, String message, int qos, boolean retained);

}
