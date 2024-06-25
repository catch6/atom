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

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import net.wenzuo.atom.core.util.JsonUtils;
import org.jinterop.dcom.common.JISystem;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.AutoReconnectController;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.list.ServerList;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author Catch
 * @since 2024-06-21
 */
@RequiredArgsConstructor
@ComponentScan("net.wenzuo.atom.opc.da")
@EnableConfigurationProperties(OpcDaProperties.class)
@ConditionalOnProperty(value = "atom.opc.da.enabled", matchIfMissing = true)
public class OpcDaAutoConfiguration implements ApplicationListener<ApplicationStartedEvent> {

	private final OpcDaProperties opcDaProperties;

	@Override
	public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
		// 关闭 COM 对象的自动垃圾收集
		JISystem.setJavaCoClassAutoCollection(false);
		ConfigurableListableBeanFactory beanFactory = event.getApplicationContext().getBeanFactory();
		List<OpcDaProperties.OpcDaInstance> instances = opcDaProperties.getInstances();
		for (OpcDaProperties.OpcDaInstance instance : instances) {
			if (!instance.getEnabled()) {
				continue;
			}
			String id = instance.getId();
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
				AutoReconnectController autoReconnectController = new AutoReconnectController(server);
				autoReconnectController.connect();

				OpcDaListenerProcessor processor = beanFactory.getBean(OpcDaListenerProcessor.class);
				List<OpcDaMessageListener> opcDaListeners = processor.getOpcDaListeners(id);
				if (opcDaListeners != null) {
					for (OpcDaMessageListener listener : opcDaListeners) {
						AccessBase access;
						if (listener.isAsync()) {
							access = new Async20Access(server, listener.getPeriod(), true);
						} else {
							access = new SyncAccess(server, listener.getPeriod());
						}
						for (String tag : listener.getTags()) {
							access.addItem(tag, (item, itemState) -> {
								try {
									listener.getMethod().invoke(listener.getBean(), item.getId(), itemState.getValue() == null ? null : JsonUtils.toJson(itemState.getValue().getObject()));
								} catch (Exception e) {
									throw new RuntimeException(e);
								}
							});
						}
					}
				}
				AccessBase access = new SyncAccess(server, 1000);
				beanFactory.registerSingleton("opcDaAccessBase-" + id, access);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}
