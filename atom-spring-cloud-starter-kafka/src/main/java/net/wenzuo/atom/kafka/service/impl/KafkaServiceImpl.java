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

package net.wenzuo.atom.kafka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.util.JsonUtils;
import net.wenzuo.atom.kafka.service.KafkaService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @author Catch
 * @since 2023-08-13
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class KafkaServiceImpl implements KafkaService {

	private final KafkaTemplate<String, String> kafkaTemplate;

	@Override
	public void send(String topic, Object message) {
		send(topic, null, message);
	}

	@Override
	public void send(String topic, String key, Object message) {
		// 分区编号为 null，交给 Kafka 自己去分配
		String json = JsonUtils.toJson(message);
		ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, null, System.currentTimeMillis(), key, json);
		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);
		future.addCallback(
			result -> {
				log.info("Kafka 消息发送成功, TOPIC:{}, DATA:{}", topic, json);
			},
			ex -> {
				log.error("Kafka 消息发送失败, {}", ex.getMessage());
			});
	}

}
