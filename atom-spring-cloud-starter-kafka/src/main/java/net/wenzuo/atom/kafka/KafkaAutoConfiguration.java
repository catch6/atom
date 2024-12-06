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

package net.wenzuo.atom.kafka;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author Catch
 * @since 2023-08-13
 */
@Import(KafkaService.class)
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaProperties.class)
@ConditionalOnProperty(value = "atom.kafka.enabled", matchIfMissing = true)
public class KafkaAutoConfiguration {

	private final KafkaProperties kafkaProperties;
	private final GenericApplicationContext genericApplicationContext;

	@PostConstruct
	public void initKafkaTopics() {
		if (kafkaProperties.getTopics() != null) {
			for (KafkaProperties.Topic topic : kafkaProperties.getTopics()) {
				genericApplicationContext.registerBean(topic.getName(), NewTopic.class, () -> new NewTopic(topic.getName(), topic.getNumPartitions(), topic.getReplicationFactor()));
			}
		}
	}

}
