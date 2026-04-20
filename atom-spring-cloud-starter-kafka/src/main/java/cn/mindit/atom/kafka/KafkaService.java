package cn.mindit.atom.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.mindit.atom.core.util.JsonUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @author Catch
 * @since 2023-08-13
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, Object message) {
        send(topic, null, message);
    }

    public void send(String topic, String key, Object message) {
        String json = JsonUtils.toJson(message);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, null, System.currentTimeMillis(), key, json);
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Kafka send error: {}", ex.getMessage());
            }
        });
    }

}
