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

package net.wenzuo.atom.opc.ua;

import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.opc.ua.config.OpcUaProperties;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Catch
 * @since 2024-08-31
 */
@Slf4j
public class OpcUaConsumerProcessor {

	public static Map<String, List<OpcUaConsumer>> processConsumerMap(ConfigurableApplicationContext applicationContext, OpcUaProperties properties, List<OpcUaSubscriber> subscribers) {
		Map<String, List<OpcUaConsumer>> consumerMap = new HashMap<>();
		processListener(consumerMap, applicationContext, properties);
		processSubscriber(consumerMap, subscribers, properties);
		return consumerMap;
	}

	private static void processListener(Map<String, List<OpcUaConsumer>> consumerMap, ConfigurableApplicationContext applicationContext, OpcUaProperties properties) {
		OpcUaListenerProcessor processor = applicationContext.getBean(OpcUaListenerProcessor.class);
		List<OpcUaConsumer> consumers = processor.getConsumers();
		if (consumers == null || consumers.isEmpty()) {
			return;
		}

		ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
		BeanExpressionContext expressionContext = new BeanExpressionContext(beanFactory, null);
		BeanExpressionResolver expressionResolver = beanFactory.getBeanExpressionResolver();

		for (OpcUaConsumer consumer : consumers) {
			String id = consumer.getId();
			if (id == null || id.isEmpty()) {
				consumer.setId(properties.getId());
			}
			String[] items = processExpression(consumer.getItems(), beanFactory, expressionContext, expressionResolver);
			consumer.setItems(items);
			consumer.initialize();
			List<OpcUaConsumer> list = consumerMap.computeIfAbsent(consumer.getId(), k -> new ArrayList<>());
			list.add(consumer);
		}
	}

	private static String[] processExpression(String[] items, ConfigurableListableBeanFactory beanFactory, BeanExpressionContext expressionContext, BeanExpressionResolver expressionResolver) {
		if (items == null || items.length == 0) {
			return null;
		}

		if (expressionResolver != null) {
			List<String> newItems = new ArrayList<>();
			for (String item : items) {
				Object object = expressionResolver.evaluate(beanFactory.resolveEmbeddedValue(item), expressionContext);
				if (object == null) {
					throw new IllegalArgumentException("OPC UA item must not be null");
				}
				if (object instanceof String str) {
					newItems.add(str);
				} else if (object instanceof String[] strs) {
					for (String str : strs) {
						if (str == null) {
							throw new IllegalArgumentException("OPC UA item must not be null");
						}
						newItems.add(str);
					}
				} else {
					throw new IllegalArgumentException("OPC UA item must be String or String[]");
				}
			}
			items = newItems.toArray(new String[0]);
		}
		return items;
	}

	private static void processSubscriber(Map<String, List<OpcUaConsumer>> consumerMap, List<OpcUaSubscriber> subscribers, OpcUaProperties properties) {
		if (subscribers == null || subscribers.isEmpty()) {
			return;
		}
		for (OpcUaSubscriber subscriber : subscribers) {
			String id = subscriber.id();
			if (id == null || id.isEmpty()) {
				id = properties.getId();
			}
			String[] items = subscriber.items();
			int[] namespaceIndices = subscriber.namespaceIndices();
			OpcUaConsumer consumer = new OpcUaConsumer(id, items, namespaceIndices, (item, value) -> {
				try {
					subscriber.message(item, value);
				} catch (Exception e) {
					log.error("OPC UA invoke error", e);
				}
			});
			consumer.initialize();
			List<OpcUaConsumer> list = consumerMap.computeIfAbsent(id, k -> new ArrayList<>());
			list.add(consumer);
		}
	}

}
