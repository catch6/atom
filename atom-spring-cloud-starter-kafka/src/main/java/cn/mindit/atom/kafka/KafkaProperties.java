package cn.mindit.atom.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Catch
 * @since 2023-08-13
 */
@Data
@ConfigurationProperties(prefix = "atom.kafka")
public class KafkaProperties {

    /**
     * 是否启用 kafka 模块
     */
    private Boolean enabled = true;
    /**
     * 自动创建的 topic
     */
    private Topic[] topics;

    @Data
    public static class Topic {

        /**
         * topic 名称
         */
        private String name;
        /**
         * 分区数
         */
        private Integer numPartitions = -1;
        /**
         * 副本数
         */
        private Short replicationFactor = -1;

    }

}
