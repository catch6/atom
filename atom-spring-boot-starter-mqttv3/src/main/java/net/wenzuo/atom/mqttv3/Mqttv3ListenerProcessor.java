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

package net.wenzuo.atom.mqttv3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Catch
 * @since 2024-06-16
 */
@Slf4j
@Component
public class Mqttv3ListenerProcessor implements BeanPostProcessor, ApplicationContextAware {

	private final Set<String> enabledIds;
	private final Map<String, List<Mqttv3MessageListener>> mqttListeners = new HashMap<>();
	private BeanFactory beanFactory;
	private BeanExpressionResolver resolver = new StandardBeanExpressionResolver();
	private BeanExpressionContext expressionContext;

	public Mqttv3ListenerProcessor(Mqttv3Properties mqttv3Properties) {
		enabledIds = mqttv3Properties.getInstances()
									 .stream()
									 .filter(Mqttv3Properties.MqttInstance::getEnabled)
									 .map(Mqttv3Properties.MqttInstance::getId)
									 .collect(Collectors.toSet());
	}

	public List<Mqttv3MessageListener> getMqttListeners(String id) {
		return mqttListeners.get(id);
	}

	@Override
	public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
		Method[] methods = bean.getClass().getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(Mqttv3Listener.class)) {
				Mqttv3Listener listener = method.getAnnotation(Mqttv3Listener.class);
				String id = listener.id();
				if (!enabledIds.contains(id)) {
					continue;
				}
				String[] topics = listener.topics();
				int[] qos = listener.qos();
				Assert.notNull(id, "MqttListener.id() must not be null");
				Assert.notNull(topics, "MqttListener.topics() must not be null");
				Assert.notNull(listener.qos(), "MqttListener.qos() must not be null");
				List<String> finalTopics = new ArrayList<>();
				for (String topic : topics) {
					Object object = resolveExpression(topic);
				}
				if (topics.length != qos.length) {
					if (qos.length == 1) {
						int qos0 = qos[0];
						qos = new int[topics.length];
						Arrays.fill(qos, qos0);
					} else {
						throw new IllegalArgumentException("MqttListener.qos() must be same length as MqttListener.topics()");
					}
				}
				Mqttv3MessageListener mqttMessageListener = new Mqttv3MessageListener(bean, method, topics, qos);
				mqttListeners.computeIfAbsent(listener.id(), k -> new ArrayList<>()).add(mqttMessageListener);
			}
		}
		return bean;
	}

	private Object resolveExpression(String value) {
		return this.resolver.evaluate(resolve(value), this.expressionContext);
	}

	private String resolve(String value) {
		if (this.beanFactory instanceof ConfigurableBeanFactory cbf) {
			return cbf.resolveEmbeddedValue(value);
		}
		return value;
	}

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		if (applicationContext instanceof ConfigurableApplicationContext cac) {
			setBeanFactory(cac.getBeanFactory());
		} else {
			setBeanFactory(applicationContext);
		}
	}

	public synchronized void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		if (beanFactory instanceof ConfigurableListableBeanFactory clbf) {
			this.resolver = clbf.getBeanExpressionResolver();
			this.expressionContext = new BeanExpressionContext((ConfigurableListableBeanFactory) beanFactory, null);
		}
	}

}
