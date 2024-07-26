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

package net.wenzuo.atom.opc.da;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Catch
 * @since 2024-06-16
 */
@Slf4j
@Getter
@Component
public class OpcDaListenerProcessor implements BeanPostProcessor {

	private final List<OpcDaSubscriber> subscribers;

	public OpcDaListenerProcessor() {
		subscribers = new ArrayList<>();
	}

	@Override
	public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
		Method[] methods = bean.getClass().getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(OpcDaListener.class)) {
				OpcDaListener listener = method.getAnnotation(OpcDaListener.class);
				OpcDaSubscriber subscriber = new OpcDaSubscriber(listener.id(), listener.items(), (item, value) -> {
					try {
						method.invoke(bean, item, value);
					} catch (Exception e) {
						log.error("OpcDaListenerSubscriber invoke error", e);
					}
				});
				subscribers.add(subscriber);
			}
		}
		return bean;
	}

}
