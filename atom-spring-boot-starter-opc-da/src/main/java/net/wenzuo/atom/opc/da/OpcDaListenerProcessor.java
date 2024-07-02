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

import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.opc.da.config.OpcDaProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
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
public class OpcDaListenerProcessor implements BeanPostProcessor {

	private final Set<String> enabledIds;
	private final Map<String, List<OpcDaMessageListener>> opcDaListeners = new HashMap<>();

	public OpcDaListenerProcessor(OpcDaProperties opcDaProperties) {
		enabledIds = opcDaProperties.getInstances()
									.stream()
									.filter(OpcDaProperties.OpcDaInstance::getEnabled)
									.map(OpcDaProperties.OpcDaInstance::getId)
									.collect(Collectors.toSet());
	}

	public List<OpcDaMessageListener> getOpcDaListeners(String id) {
		return opcDaListeners.get(id);
	}

	@Override
	public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
		Method[] methods = bean.getClass().getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(OpcDaListener.class)) {
				OpcDaListener listener = method.getAnnotation(OpcDaListener.class);
				String id = listener.id();
				if (!enabledIds.contains(id)) {
					continue;
				}
				String[] tags = listener.tags();
				Assert.notNull(id, "OpcDaListener.id() must not be null");
				Assert.notNull(tags, "OpcDaListener.tags() must not be null");
				OpcDaMessageListener opcDaMessageListener = new OpcDaMessageListener(bean, method, tags);
				opcDaListeners.computeIfAbsent(listener.id(), k -> new ArrayList<>()).add(opcDaMessageListener);
			}
		}
		return bean;
	}

}
