/*
 * Copyright (c) 2022-2023 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.kafka.config;

import lombok.RequiredArgsConstructor;
import net.wenzuo.atom.kafka.properties.KafkaProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;

/**
 * @author Catch
 * @since 2023-08-13
 */
@RequiredArgsConstructor
@ComponentScan("net.wenzuo.atom.kafka")
@ConfigurationPropertiesScan("net.wenzuo.atom.kafka.properties")
@ConditionalOnProperty(value = "atom.kafka.enabled", matchIfMissing = true)
public class KafkaAutoConfiguration {

	private final KafkaProperties kafkaProperties;
	private final GenericApplicationContext genericApplicationContext;

	@PostConstruct
	public void initKafkaTopics() {
		for (KafkaProperties.Topic topic : kafkaProperties.getTopics()) {
			genericApplicationContext.registerBean(topic.getName(), NewTopic.class, () -> new NewTopic(topic.getName(), topic.getNumPartitions(), topic.getReplicationFactor()));
		}
	}

}
