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

package net.wenzuo.atom.opc.da.config;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.opc.da.*;
import org.jinterop.dcom.common.JISystem;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.AutoReconnectController;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.list.ServerList;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

/**
 * @author Catch
 * @since 2024-07-24
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class OpcDaConfiguration implements ApplicationListener<ApplicationStartedEvent> {

	private final OpcDaProperties opcDaProperties;
	private final List<OpcDaSubscriber> opcDaSubscribers;

	@Override
	public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
		// 关闭 COM 对象的自动垃圾收集
		JISystem.setJavaCoClassAutoCollection(false);
		ConfigurableApplicationContext applicationContext = event.getApplicationContext();
		ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
		Map<String, List<OpcDaListenerSubscriber>> subscriberMap = new HashMap<>();
		processListener(subscriberMap, applicationContext);
		processSubscriber(subscriberMap, opcDaSubscribers);

		List<OpcDaProperties.OpcDaInstance> instances = opcDaProperties.getInstances();
		for (OpcDaProperties.OpcDaInstance instance : instances) {
			if (!instance.getEnabled()) {
				continue;
			}
			try {
				if (StrUtil.isEmpty(instance.getClsId())) {
					ServerList serverList = new ServerList(instance.getHost(), instance.getUser(), instance.getPassword(), instance.getDomain());
					instance.setClsId(serverList.getClsIdFromProgId(instance.getProgId()));
				}
				ConnectionInformation ci = new ConnectionInformation();
				ci.setHost(instance.getHost());
				ci.setDomain(instance.getDomain());
				ci.setUser(instance.getUser());
				ci.setPassword(instance.getPassword());
				ci.setProgId(instance.getProgId());
				ci.setClsid(instance.getClsId());

				Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());

				WriteableAccessBase access;
				if (instance.isAsync()) {
					access = new WriteableAsync20Access(server, instance.getPeriod(), instance.getInitialRefresh());
				} else {
					access = new WriteableSyncAccess(server, instance.getPeriod());
				}
				List<OpcDaListenerSubscriber> subscribers = subscriberMap.get(instance.getId());
				if (subscribers == null || subscribers.isEmpty()) {
					continue;
				}
				for (OpcDaListenerSubscriber subscriber : subscribers) {
					String[] items = subscriber.getItems();
					BiConsumer<Item, ItemState> consumer = subscriber.getConsumer();
					for (String item : items) {
						access.addItem(item, consumer::accept);
					}
				}
				access.bind();

				AutoReconnectController autoReconnectController = new AutoReconnectController(server);
				autoReconnectController.connect();

				beanFactory.registerSingleton(opcDaProperties.getBeanPrefix() + instance.getId(), access);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}
	}

	private void processSubscriber(Map<String, List<OpcDaListenerSubscriber>> subscriberMap, List<OpcDaSubscriber> opcDaSubscribers) {
		if (opcDaSubscribers == null || opcDaSubscribers.isEmpty()) {
			return;
		}
		for (OpcDaSubscriber opcDaSubscriber : opcDaSubscribers) {
			String id = opcDaSubscriber.id();
			if (id == null) {
				id = opcDaProperties.getId();
			}
			String[] items = opcDaSubscriber.items();
			Assert.notNull(id, "OPC DA id must not be null");
			Assert.notEmpty(items, "OPC DA items must not be empty");
			OpcDaListenerSubscriber subscriber = new OpcDaListenerSubscriber(id, items, (item, itemState) -> {
				try {
					opcDaSubscriber.message(item, itemState);
				} catch (Exception e) {
					log.error("OpcDaListenerSubscriber invoke error", e);
				}
			});
			List<OpcDaListenerSubscriber> list = subscriberMap.computeIfAbsent(id, k -> new ArrayList<>());
			list.add(subscriber);
		}
	}

	private void processListener(Map<String, List<OpcDaListenerSubscriber>> subscriberMap, ConfigurableApplicationContext applicationContext) {
		ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
		BeanExpressionContext expressionContext = new BeanExpressionContext(beanFactory, null);
		BeanExpressionResolver expressionResolver = beanFactory.getBeanExpressionResolver();

		OpcDaListenerProcessor processor = applicationContext.getBean(OpcDaListenerProcessor.class);
		List<OpcDaListenerSubscriber> subscribers = processor.getSubscribers();

		if (subscribers == null || subscribers.isEmpty()) {
			return;
		}

		for (OpcDaListenerSubscriber subscriber : subscribers) {
			String id = subscriber.getId();
			if (id == null) {
				id = opcDaProperties.getId();
			}
			String[] items = subscriber.getItems();
			Assert.notNull(id, "OPC DA id must not be null");
			Assert.notEmpty(items, "OPC DA items must not be empty");

			if (expressionResolver != null) {
				List<String> newItems = new ArrayList<>();
				for (String item : items) {
					Object object = expressionResolver.evaluate(beanFactory.resolveEmbeddedValue(item), expressionContext);
					if (object == null) {
						throw new IllegalArgumentException("OPC DA item must not be null");
					}
					if (object instanceof String str) {
						newItems.add(str);
					} else if (object instanceof String[] strs) {
						for (String str : strs) {
							if (str == null) {
								throw new IllegalArgumentException("OPC DA item must not be null");
							}
							newItems.add(str);
						}
					} else {
						throw new IllegalArgumentException("OPC DA item must be String or String[]");
					}
				}
				items = newItems.toArray(new String[0]);
				subscriber.setItems(items);
			}
			List<OpcDaListenerSubscriber> list = subscriberMap.computeIfAbsent(id, k -> new ArrayList<>());
			list.add(subscriber);
		}
	}

}
